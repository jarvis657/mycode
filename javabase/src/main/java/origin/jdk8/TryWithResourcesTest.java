package origin.jdk8;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import org.junit.Test;

/**
 * @Author:lmq
 * @Date: 2022/4/15
 * @Desc:
 **/
public class TryWithResourcesTest {

    @Test
    public void testD(){
        LocalDate d = LocalDate.now();
        System.out.println(d.toString());
    }

    @Test
    public void simpleRead() {
        /**
         * This is a simple test of reading file with try-with-resources statement. Tested file contains:
         * <pre>
         * French accentuated letter: étrangère
         * </pre>
         *
         * This sample shows how to use try-with-resources statement. Note that you can use inputStream object only inside try{} block - even if you predefine inputStream before try block as null:
         * <pre>
         * FileInputStream inputStream = null;
         * try {inputStream = new FileInputStream(file)) { // <==== won't work
         * </pre>
         *
         * Note also that we can work on multiple files implementing AutoCloseable interface.
         *
         */
        File testFile = new File("/home/bartosz/tmp/testFile.txt");
        File testFile2 = new File("/home/bartosz/tmp/testFile.txt");
        try (FileInputStream inputStream = new FileInputStream(
                testFile); FileInputStream inputStream2 = new FileInputStream(testFile2)) {
            byte[] result = new byte[inputStream.available()];
            inputStream.read(result);
            String resultText = new String(result, "UTF-8");

            byte[] result2 = new byte[inputStream2.available()];
            inputStream2.read(result2);
            String resultText2 = new String(result2, "UTF-8");

            String expectedText = "French accentuated letter: étrangère";
            assertTrue("Read text (" + resultText + ") should be the same as expected (" + expectedText + ")",
                    resultText.equals(expectedText));
            assertTrue("Text read by the first inputStream (" + resultText
                            + ") should be the same as the text read by the second input stream (" + resultText2 + ")",
                    resultText2.equals(resultText));
        } catch (Exception e) {
            fail("The test shouldn't fail because of exception: " + e.getMessage());
        }
    }

    @Test
    public void implementedAutoCloseable() {
        /**
         * Here we'll implement our own AutoCloseable object.
         */
        CloseChecker checker = new CloseChecker();
        CloseChecker checker2 = new CloseChecker();
        assertFalse("Checker should return false on isClosed but it returns true", checker.isClosed());
        try (CustomAutoCloseable custom = new CustomAutoCloseable(checker)) {
            // do nothing, just for test close() invocation
        } catch (Exception e) {
            fail("Test failed but it shouldn't: " + e.getMessage());
        }
        assertTrue("close() method of CustomAutoCloseable should be called but it wasn't", checker.isClosed());
    }

    @Test
    public void suppressedExceptions() {
        /**
         * In this test case we'll show the new concept of "suppressed exceptions". Suppressed exceptions can occur in try-with-resources statements when two or more exceptions are thrown inside the same
         * try-with-resources block. In our case, 3 exceptions will be thrown:
         * - 1 on closing anotherSuppressed (IllegalAccessError)
         * - 1 on closing suppressed (UnsupportedOperationException)
         * - 1 (this exception is catched) on suppressed.throwException() invocation
         *
         * To resume this situation, try-with-resources can push exceptions occurred on close() method to appropriate catch() block and append them into Throwable instance of this block. Inside the block
         * we can get appended exceptions thanks to Exception.getSuppressed method. Because the try-with-resources AutoCloseable objects are closed in the opposite order of their creation
         * (last defined object is closed as the first), we retrieve the exception from anotherSuppressed object at the first position in getSuppressed() array.
         *
         */
        try (SuppressedExceptionResources suppressed = new SuppressedExceptionResources(); SuppressedExceptionResources anotherSuppressed = new AnotherSuppressedExceptionResources()) {
            suppressed.throwException();
            suppressed.throwAnotherException();
            anotherSuppressed.throwException();
            fail("Test shouldn't passe here - Exception should be catched instead");
        } catch (Exception e) {
            assertTrue("One suppressed exceptions is expected, " + e.getSuppressed().length + " were got",
                    e.getSuppressed().length == 2);
            Throwable suppressedExc = e.getSuppressed()[0];
            assertTrue("Supressed exception should be IllegalAccessError but is " + suppressedExc.getClass(),
                    suppressedExc.getClass() == IllegalAccessError.class);
            suppressedExc = e.getSuppressed()[1];
            assertTrue("Supressed exception should be UnsupportedOperationException but is " + suppressedExc.getClass(),
                    suppressedExc.getClass() == UnsupportedOperationException.class);
            assertTrue("Catched exception should be IllegalStateException but is " + e.getClass(),
                    e.getClass() == IllegalStateException.class);
        }

        /**
         * Here we'll test the same code as previously but only with NullPointerException defined. It means that suppressed exceptions can't find appropriated catch() block. So they can't be
         * catched properly, as previously. Instead, they will be propagate and break down the application.
         */
        boolean wasPropagated = false;
        try {
            try (SuppressedExceptionResources suppressed = new SuppressedExceptionResources(); SuppressedExceptionResources anotherSuppressed = new AnotherSuppressedExceptionResources()) {
                suppressed.throwException();
                suppressed.throwAnotherException();
                anotherSuppressed.throwException();
                fail("Test shouldn't passe here - Exception should be catched instead");
            } catch (NullPointerException e) {
                fail("NullPointerException shouldn't be thrown");
            }
        } catch (Exception e) {
            wasPropagated = true;
        }
        assertTrue("Exception doesn't catch in try-with-resources catch block should be propagated but it wasn't",
                wasPropagated);
    }

    /**
     * If you wish, decomment this code to be sure that classes aren't implementing AutoCloseable interface can't be used in try-with-resources blocks.
     */
//	@Test
//	public void notAutoCloseable() {
//		try (NotAutoCloseable notAutoCloseable = new NotAutoCloseable()) {
//
//		}
//	}
}

class NotAutoCloseable {

}

class CloseChecker {

    private boolean closed;

    public boolean isClosed() {
        return this.closed;
    }

    public void setClosed(boolean c) {
        this.closed = c;
    }
}

class CustomAutoCloseable implements AutoCloseable {

    private CloseChecker checker;

    public CustomAutoCloseable(CloseChecker checker) {
        this.checker = checker;
    }

    @Override
    public void close() {
        System.out.println("================auto close==================");
        this.checker.setClosed(true);
    }

}

class SuppressedExceptionResources implements AutoCloseable {

    public void throwException() throws IllegalStateException {
        throw new IllegalStateException("IllegalStateException is thrown here");
    }

    public void throwAnotherException() throws IllegalArgumentException {
        throw new IllegalArgumentException("IllegalArgumentException is thrown now");
    }

    @Override
    public void close() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Close operation isn't implemented");
    }

}

class AnotherSuppressedExceptionResources extends SuppressedExceptionResources {

    @Override
    public void close() throws IllegalAccessError {
        throw new IllegalAccessError("Close operation isn't implemented in AnotherSuppressedExceptionResources");
    }

}

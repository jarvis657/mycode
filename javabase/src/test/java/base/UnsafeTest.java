package base;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Before;
import org.junit.Test;
import sun.misc.Unsafe;
import sun.reflect.ReflectionFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UnsafeTest {

    private Unsafe unsafe;

    @Before
    public void prepareUnsafe() throws Exception {
        unsafe = makeInstance();
    }

    private Unsafe makeInstance() throws Exception {
        Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
        unsafeConstructor.setAccessible(true);
        unsafe = unsafeConstructor.newInstance();
        return unsafe;
    }

    private Unsafe fetchInstance() throws Exception {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        unsafe = (Unsafe) theUnsafe.get(null);
        return unsafe;
    }

    @Test
    public void testRetrieval() throws Exception {
        fetchInstance();
    }

    @Test(expected = SecurityException.class)
    public void testSingletonGetter() throws Exception {
        Unsafe.getUnsafe();
    }

    @Test
    public void testObjectCreation() throws Exception {
        ClassWithExpensiveConstructor instance = (ClassWithExpensiveConstructor) unsafe.allocateInstance(ClassWithExpensiveConstructor.class);
        assertEquals(0, instance.getValue());
    }

    @Test
    public void testReflectionFactory() throws Exception {
        @SuppressWarnings("unchecked")
        Constructor<ClassWithExpensiveConstructor> silentConstructor = (Constructor<ClassWithExpensiveConstructor>) ReflectionFactory.getReflectionFactory()
                .newConstructorForSerialization(ClassWithExpensiveConstructor.class, Object.class.getConstructor());
        silentConstructor.setAccessible(true);
        assertEquals(0, silentConstructor.newInstance().getValue());
    }

    @Test
    public void testStrangeReflectionFactory() throws Exception {
        @SuppressWarnings("unchecked")
        Constructor<ClassWithExpensiveConstructor> silentConstructor = (Constructor<ClassWithExpensiveConstructor>) ReflectionFactory.getReflectionFactory()
                .newConstructorForSerialization(ClassWithExpensiveConstructor.class, OtherClass.class.getDeclaredConstructor());
        silentConstructor.setAccessible(true);
        ClassWithExpensiveConstructor instance = silentConstructor.newInstance();
        /**
         * 调用其他的构造器 是命中第一个跟自己类型一样的属性赋值
         */
        assertEquals(20, instance.getValue());
        assertEquals(ClassWithExpensiveConstructor.class, instance.getClass());
        assertEquals(Object.class, instance.getClass().getSuperclass());
    }

    @Test
    public void testDirectIntArray() throws Exception {
        long maximum = Integer.MAX_VALUE + 1L;
        DirectIntArray directIntArray = new DirectIntArray(maximum);
        directIntArray.setValue(0L, 2);
        directIntArray.setValue(maximum, 1);
        assertEquals(2, directIntArray.getValue(0L));
        assertEquals(0, directIntArray.getValue(1L));
        assertEquals(1, directIntArray.getValue(maximum));
        directIntArray.destroy();
    }

    @Test
    public void testMallaciousAllocation() throws Exception {
        long address = unsafe.allocateMemory(2L * 4);
        unsafe.setMemory(address, 8L, (byte) 0);
        assertEquals(0, unsafe.getInt(address));
        assertEquals(0, unsafe.getInt(address + 4));
        unsafe.putInt(address + 1, 0xffffffff);
        assertEquals(0xffffff00, unsafe.getInt(address));
        assertEquals(0x000000ff, unsafe.getInt(address + 4));
    }

    @Test
    public void testObjectAllocation() throws Exception {
        long containerSize = sizeOf(Container.class);
        long address = unsafe.allocateMemory(containerSize);
        Container c1 = new Container(10, 1000L);
        Container c2 = new Container(5, -10L);
        place(c1, address);
        place(c2, address + containerSize);
        Container newC1 = (Container) read(Container.class, address);
        Container newC2 = (Container) read(Container.class, address + containerSize);
        assertEquals(c1, newC1);
        assertEquals(c2, newC2);
    }

    public void place(Object o, long address) throws Exception {
        Class<?> clazz = o.getClass();
        do {
            for (Field f : clazz.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    long offset = unsafe.objectFieldOffset(f);
                    if (f.getType() == long.class) {
                        unsafe.putLong(address + offset, unsafe.getLong(o, offset));
                    } else if (f.getType() == int.class) {
                        unsafe.putInt(address + offset, unsafe.getInt(o, offset));
                    } else {
                        throw new UnsupportedOperationException();
                    }
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);
    }

    public Object read(Class<?> clazz, long address) throws Exception {
        Object instance = unsafe.allocateInstance(clazz);
        do {
            for (Field f : clazz.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    long offset = unsafe.objectFieldOffset(f);
                    if (f.getType() == long.class) {
                        unsafe.putLong(instance, offset, unsafe.getLong(address + offset));
                    } else if (f.getType() == int.class) {
                        unsafe.putInt(instance, offset, unsafe.getInt(address + offset));
                    } else {
                        throw new UnsupportedOperationException();
                    }
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);
        return instance;
    }

    public long sizeOf(Class<?> clazz) {
        long maximumOffset = 0;
        do {
            for (Field f : clazz.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    maximumOffset = Math.max(maximumOffset, unsafe.objectFieldOffset(f));
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);
        return maximumOffset + 8;
    }

    @Test(expected = Exception.class)
    public void testThrowChecked() throws Exception {
        throwChecked();
    }

    public void throwChecked() {
        unsafe.throwException(new Exception());
    }

    @Test
    public void testPark() throws Exception {
        final boolean[] run = new boolean[1];
        Thread thread = new Thread() {
            @Override
            public void run() {
                unsafe.park(true, 100000L);
                run[0] = true;
            }
        };
        thread.start();
        unsafe.unpark(thread);
        thread.join(100L);
        assertTrue(run[0]);
    }

    @Test
    public void testCopy() throws Exception {
        long address = unsafe.allocateMemory(4L);
        unsafe.putInt(address, 100);
        long otherAddress = unsafe.allocateMemory(4L);
        unsafe.copyMemory(address, otherAddress, 4L);
        assertEquals(100, unsafe.getInt(otherAddress));
    }

    private static class ClassWithExpensiveConstructor {

        private final int value;

        private ClassWithExpensiveConstructor() {
            value = doExpensiveLookup();
        }

        private int doExpensiveLookup() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }

        public int getValue() {
            return value;
        }
    }

    private static class OtherClass {

        private final double value_othername;
        private final int unknownValue;

        private OtherClass() {
            System.out.println("invoke otherclass test constructor");
            this.value_othername = 10D;
            this.unknownValue = 20;
        }
    }

    private static class SuperContainer {

        protected int i;

        private SuperContainer(int i) {
            this.i = i;
        }

        public int getI() {
            return i;
        }
    }

    private static class Container extends SuperContainer {

        private long l;

        private Container(int i, long l) {
            super(i);
            this.l = l;
        }

        public long getL() {
            return l;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Container container = (Container) o;
            return l == container.l && i == container.i;
        }
    }

    private class DirectIntArray {

        private final static long INT_SIZE_IN_BYTES = 4;

        private final long startIndex;

        public DirectIntArray(long size) {
            startIndex = unsafe.allocateMemory(size * INT_SIZE_IN_BYTES);
            unsafe.setMemory(startIndex, size * INT_SIZE_IN_BYTES, (byte) 0);
        }

        public void setValue(long index, int value) {
            unsafe.putInt(index(index), value);
        }

        public int getValue(long index) {
            return unsafe.getInt(index(index));
        }

        private long index(long offset) {
            return startIndex + offset * INT_SIZE_IN_BYTES;
        }

        public void destroy() {
            unsafe.freeMemory(startIndex);
        }
    }
}

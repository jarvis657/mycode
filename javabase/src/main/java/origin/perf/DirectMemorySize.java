package origin.perf;

//import sun.jvm.hotspot.debugger.AddressException;
//import sun.jvm.hotspot.oops.*;
//import sun.jvm.hotspot.runtime.VM;
//import sun.jvm.hotspot.tools.Tool;
//import sun.jvm.hotspot.utilities.SystemDictionaryHelper;

import java.util.Arrays;
import org.apache.poi.util.LongField;
//import sun.misc.VM;

/**
 * @Author:qishan
 * @Date: 2019-05-09
 * @Desc: works on core dumps  java -cp .:$JAVA_HOME/lib/sa-jdi.jar DirectMemorySize $JAVA_HOME/bin/java core.xxx
 * or  sudo -u admin java -cp .:$JAVA_HOME/lib/sa-jdi.jar DirectMemorySize -e `pgrep -u admin java`
 **/

//public class DirectMemorySize extends Tool {
//    private boolean exactMallocMode;
//    private boolean verbose;
//
//    public DirectMemorySize(boolean exactMallocMode, boolean verbose) {
//        this.exactMallocMode = exactMallocMode;
//        this.verbose = verbose;
//    }
//
//    public static long getStaticLongFieldValue(String className, String fieldName) {
//        InstanceKlass klass = SystemDictionaryHelper.findInstanceKlass(className);
//        LongField field = (LongField) klass.findField(fieldName, "J");
//        return field.getValue(klass.getJavaMirror()); // on JDK7 use: field.getValue(klass.getJavaMirror());
//    }
//
//    public static int getStaticIntFieldValue(String className, String fieldName) {
//        InstanceKlass klass = SystemDictionaryHelper.findInstanceKlass(className);
//        IntField field = (IntField) klass.findField(fieldName, "I");
//        return field.getValue(klass.getJavaMirror()); // on JDK7 use: field.getValue(klass.getJavaMirror());
//    }
//
//    public static double toM(long value) {
//        return value / (1024 * 1024.0);
//    }
//
//    public static void main(String[] args) {
//        boolean exactMallocMode = false;
//        boolean verbose = false;
//
//        // argument processing logic copied from sun.jvm.hotspot.tools.JStack
//        int used = 0;
//        for (String arg : args) {
//            if ("-e".equals(arg)) {
//                exactMallocMode = true;
//                used++;
//            } else if ("-v".equals(arg)) {
//                verbose = true;
//                used++;
//            }
//        }
//
//        if (used != 0) {
//            args = Arrays.copyOfRange(args, used, args.length);
//        }
//
//        DirectMemorySize tool = new DirectMemorySize(exactMallocMode, verbose);
//        tool.execute(args);
////        tool.start(args);
////        tool.stop();
//    }
//
//    public void run() {
//        // Ready to go with the database...
//        try {
//            long reservedMemory = getStaticLongFieldValue("java.nio.Bits", "reservedMemory");
//            long directMemory = getStaticLongFieldValue("sun.misc.VM", "directMemory");
//
//            System.out.println("NIO direct memory: (in bytes)");
//            System.out.printf("  reserved size = %f MB (%d bytes)\n", toM(reservedMemory), reservedMemory);
//            System.out.printf("  max size      = %f MB (%d bytes)\n", toM(directMemory), directMemory);
//
//            if (verbose) {
//                System.out.println("Currently allocated direct buffers:");
//            }
//
//            if (exactMallocMode || verbose) {
//                final long pageSize = getStaticIntFieldValue("java.nio.Bits", "pageSize");
//                ObjectHeap heap = VM.getVM().getObjectHeap();
//                InstanceKlass deallocatorKlass =
//                        SystemDictionaryHelper.findInstanceKlass("java.nio.DirectByteBuffer$Deallocator");
//                final LongField addressField = (LongField) deallocatorKlass.findField("address", "J");
//                final IntField capacityField = (IntField) deallocatorKlass.findField("capacity", "I");
//                final int[] countHolder = new int[1];
//
//                heap.iterateObjectsOfKlass(new DefaultHeapVisitor() {
//                    public boolean doObj(Oop oop) {
//                        long address = addressField.getValue(oop);
//                        if (address == 0) return false; // this deallocator has already been run
//
//                        long capacity = capacityField.getValue(oop);
//                        long mallocSize = capacity + pageSize;
//                        countHolder[0]++;
//
//                        if (verbose) {
//                            System.out.printf("  0x%016x: capacity = %f MB (%d bytes),"
//                                            + " mallocSize = %f MB (%d bytes)\n",
//                                    address, toM(capacity), capacity,
//                                    toM(mallocSize), mallocSize);
//                        }
//
//                        return false;
//                    }
//                }, deallocatorKlass, false);
//
//                if (exactMallocMode) {
//                    long totalMallocSize = reservedMemory + pageSize * countHolder[0];
//                    System.out.printf("NIO direct memory malloc'd size: %f MB (%d bytes)\n",
//                            toM(totalMallocSize), totalMallocSize);
//                }
//            }
//        } catch (AddressException e) {
//            System.err.println("Error accessing address 0x"
//                    + Long.toHexString(e.getAddress()));
//            e.printStackTrace();
//        }
//    }
//
//    public String getName() {
//        return "directMemorySize";
//    }
//
//    protected void printFlagsUsage() {
//        System.out.println("    -e\tto print the actual size malloc'd");
//        System.out.println("    -v\tto print verbose info of every live DirectByteBuffer allocated from Java");
//        super.printFlagsUsage();
//    }
//}

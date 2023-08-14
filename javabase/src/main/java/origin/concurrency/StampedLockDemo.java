package origin.concurrency;

import java.util.concurrent.locks.StampedLock;

/**
 * @Author:jarvis
 * @Date: 2023/8/2
 * @Desc:
 **/
public class StampedLockDemo {

    private double x, y;
    private final StampedLock lock = new StampedLock();

    public void move(double deltaX, double deltaY) {
        long stamp = lock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public double distanceFromOrigin() {
        long stamp = lock.tryOptimisticRead();
        double currentX = x;
        double currentY = y;
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    public static void main(String[] args) {
        StampedLockDemo example = new StampedLockDemo();
        example.move(3, 4);
        double distance = example.distanceFromOrigin();
        System.out.println("Distance from origin: " + distance);
    }
}

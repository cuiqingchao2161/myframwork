package com.cui.mvvmdemo.demos.lockTest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * description : TODO:类的作用
 * author : cuiqingchao
 * date : 2020/9/14 10:59
 */
class LockTest {
//    Lock lock = new ReentrantLock();//缺省为非公平锁 synchronized为非公平锁
//    Lock lock = new ReentrantLock(true);//公平性 即等待时间长的线程优先获取锁资源
    public static void testlock() {
        Lock lock = new ReentrantLock();
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                lock.lock();
                try {
                    Thread.sleep(1000);
                    System.out.println("goon");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }

        });

        t.start();
        System.out.println("start");
        lock.lock();
        System.out.println("over");
        lock.unlock();
    }


    public static void testtry() {
        Lock lock = new ReentrantLock();
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                lock.lock();
                System.out.println("get");
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                    System.out.println("release");
                }
            }

        });

        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                while (true) {
                    if (lock.tryLock()) {
                        System.out.println("get success");
                        lock.unlock();
                        break;
                    }else {
                        System.out.println("get faile ... ");
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }

        });

        t.start();
        t1.start();

    }



    public static void testinterrupt() {
        Lock lock = new ReentrantLock();
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                lock.lock();
                try {
                    Thread.sleep(10000);
                    System.out.println("goon ...");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }

        });

        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    lock.lockInterruptibly();
                    System.out.println("get ...");
                    lock.unlock();
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    //e1.printStackTrace();

                    System.out.println("interrupt ... ");
                }

            }

        });

        t.start();
        t1.start();

        try {
            Thread.sleep(5000);
        }catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("to interrupt ");
        t1.interrupt();
    }


    public static void testdelay() {
        Lock lock = new ReentrantLock();
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                lock.lock();
                try {
                    Thread.sleep(10000);
                    System.out.println("goon ...");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }

        });

        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    if(lock.tryLock(5, TimeUnit.SECONDS)) {
                        System.out.println("get ...");
                        lock.unlock();
                    }else {
                        System.out.println("have not get  ...");
                    }
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    //e1.printStackTrace();

                    System.out.println("interrupt ... ");
                }

            }

        });

        t.start();
        t1.start();
    }


    public static void main(String[] args) {
        LockTest lt = new LockTest();
//        testlock();
//        testtry();
        testinterrupt();
//        testdelay();
    }
}

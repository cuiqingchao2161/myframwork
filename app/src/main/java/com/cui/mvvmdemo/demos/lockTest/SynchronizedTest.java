package com.cui.mvvmdemo.demos.lockTest;

/**
 * description : 证明synchronized是可重入锁 非公平锁
 * author : cuiqingchao
 * date : 2020/9/14 10:59
 */
class SynchronizedTest {

    public synchronized void test(String name) {
        System.out.println(name + " get the lock");
        System.out.println(name + " release the lock");
    }

    public void test1(String name) {
       System.out.println(name + " get the lock");
       System.out.println(name + " release the lock");
    }



    public static void main(String[] args) {
        SynchronizedTest lt = new SynchronizedTest();
        new Thread(() -> lt.test("A")).start();
        new Thread(() -> lt.test("B")).start();
        new Thread(() -> lt.test("C")).start();
        new Thread(() -> lt.test("D")).start();
        new Thread(() -> lt.test("E")).start();
        new Thread(() -> lt.test("F")).start();
    }
}

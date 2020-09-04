package com.cui.mvvmdemo.demos.threadTest;


/**
 * 测试结果：thread.start前调用thread的方法都是在调用线程（主线程）而非此thread中执行；
 * 调用start后run方法内的代码是在此thread中执行；
 * run方法执行结束再次调用此thread中其他方法是在调用者线程（主线程）中执行；
 * run方法执行过程中再次调用此thread中其他方法是在调用者线程（主线程）中执行；
 */
public class ThreadTest {

    public static void main(String[] args) {
        Mythread thread = new Mythread("ss");
        thread.testM2();
        thread.start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.testM3();
    }


    private static class Mythread extends Thread{
        String TAG = Mythread.class.getSimpleName();
        String name;

        public Mythread(String n) {
            this.name = n;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("run");

            testM1();
        }

        public void testM1(){
            int count = 0;
            while(true){
                count++;
                if(count>100){
                    break;
                }
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("testM1,threadName=" + Thread.currentThread().getName() + ",threadId="+Thread.currentThread().getId());
        }

        public void testM2(){
            System.out.println("testM2,threadName=" + Thread.currentThread().getName() + ",threadId="+Thread.currentThread().getId());
        }

        public void testM3(){
            System.out.println("testM3,threadName=" + Thread.currentThread().getName() + ",threadId="+Thread.currentThread().getId());
        }
    }
}

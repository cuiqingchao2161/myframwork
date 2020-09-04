package com.cui.mvvmdemo.demos.ftp;


/**
 * 测试结果：thread.start前调用thread的方法都是在调用线程（主线程）而非此thread中执行；
 * 调用start后run方法内的代码是在此thread中执行；
 * run方法执行结束再次调用此thread中其他方法是在调用者线程（主线程）中执行；
 * run方法执行过程中再次调用此thread中其他方法是在调用者线程（主线程）中执行；
 */
public class FtpTest {



    public static void main(String[] args) {
        String host = "";
        int port = 21;
        FtpUtil.getInstance().login(host,port,"","");
//        FtpUtil.getInstance().uploadFile()
    }



}

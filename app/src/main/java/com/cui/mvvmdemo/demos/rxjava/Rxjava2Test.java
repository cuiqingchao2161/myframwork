package com.cui.mvvmdemo.demos.rxjava;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * description : TODO:类的作用
 * author : cuiqingchao
 * date : 2020/8/24 14:22
 */
public class Rxjava2Test {

    static String TAG = Rxjava2Test.class.getSimpleName();
    static int count = 0;
    static int count1 = 0;

    public static void main(String[] args) {
        System.out.print("count = "+ count);
        Observable.just("openVoice")
                .delay(2000, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.newThread())
                .onTerminateDetach()
                .subscribe(s -> {
                    System.out.print("count3 = "+ count);
                        });


        Observable.just("a").repeatUntil(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() throws Exception {
                count++ ;
                System.out.print("count = "+ count);
                if(count > 20){
                    return true;
                }
                return false;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        count1 ++ ;
                        System.out.print("count1="+count1);
                    }
                });
    }
}

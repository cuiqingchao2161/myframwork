package com.cui.mvvmdemo.demos.rxjava;

import android.util.Log;
import android.widget.TextView;

import com.cui.mvvmdemo.R;
import com.cui.lib.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 测试结果：thread.start前调用thread的方法都是在调用线程（主线程）而非此thread中执行；
 * 调用start后run方法内的代码是在此thread中执行；
 * run方法执行结束再次调用此thread中其他方法是在调用者线程（主线程）中执行；
 * run方法执行过程中再次调用此thread中其他方法是在调用者线程（主线程）中执行；
 */
public class TestRxjava2Activity extends BaseActivity {
    static String TAG = TestRxjava2Activity.class.getSimpleName();
    static String TAG1 = "repeatWhen";
    static int count = 0;
    static int count1 = 0;
    boolean isCompleted = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        ((TextView) findViewById(R.id.title)).setText(TAG);
    }

    @Override
    protected void initData() {
        Log.d(TAG, "initData");

        repeatWhen();
    }

    private void repeatUntil() {
        count = 0;
        Observable.just("a").repeatUntil(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() throws Exception {
                count++;
                Log.d(TAG1, "count = " + count);
                if (count > 20) {
                    return true;
                }
                return false;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG1, "s = " + s);
                    }
                });
    }

    static boolean hasCondition = false;
    private Object lock = new Object();

    private void repeatWhen() {
        count = 0;
        hasCondition = false;

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                Log.i(TAG1, "count:" + count);
                if (count < 10) {
                    if (hasCondition) {
                        e.onNext(true);
                        Log.i(TAG1, "轮询成功 轮询次数：" + count);
                    } else {
                        e.onNext(false);
                        Log.i(TAG1, "onComplete1");
                        e.onComplete();
                    }
                } else {
                    e.onError(new Throwable("达到最大轮询次数 终止轮询"));
                }

            }
        })
                .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                        return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(@NonNull Object throwable) throws Exception {
                                Log.i(TAG1, "pollingRecordState repeat");
                                // 加入判断条件：当轮询次数 = 5次后，就停止轮询
                                if (count > 20) {
                                    Log.i(TAG1, "pollingRecordState failed");
                                    // 此处选择发送onError事件以结束轮询，因为可触发下游观察者的onError（）方法回调
                                    return Observable.error(new Throwable("轮询结束"));
                                }
                                synchronized (lock) {
                                    lock.wait();
                                    Log.i(TAG1, "pollingRecordState repeat1");
                                    // 若轮询次数＜4次，则发送1Next事件以继续轮询
                                    // 注：此处加入了delay操作符，作用 = 延迟一段时间发送（此处设置 = 2s），以实现轮询间间隔设置
                                    return Observable.just(1).delay(50, TimeUnit.MILLISECONDS);
                                }

                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG1, "onSubscribe");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Log.i(TAG1, "onNext");
                        if (count >= 5) {
                            hasCondition = true;
                        }
                        count++;
                        if (aBoolean) {
                            Log.i(TAG1, "pollingRecordState success");
                        } else {
                            Log.i(TAG1, "音频不可用");
                        }
                        synchronized (lock) {
                            lock.notify();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG1, "onError");
                        // 获取轮询结束信息
                        Log.d(TAG1, e.toString());
                        synchronized (lock) {
                            lock.notify();
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG1, "onComplete");
                    }
                });
    }


    private void takeUntil() {
        Observable.just("a").repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {

            @Override
            public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                count++;
                Log.d(TAG, "count = " + count);
                return objectObservable.delay(5, TimeUnit.SECONDS);
            }
        })
                .takeUntil(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return isCompleted;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "count1=" + count1);
                        if (count1 >= 3) {
                            Log.d(TAG, "isCompleted=true");
                            isCompleted = true;
                        } else {
                            Log.d(TAG, "isCompleted=false");
                        }
                        count1++;
                    }
                });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void refreshView() {

    }


}

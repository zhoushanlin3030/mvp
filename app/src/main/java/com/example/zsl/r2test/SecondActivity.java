package com.example.zsl.r2test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zsl on 2017/11/10.
 */

public class SecondActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "SUB";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.btn1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                btn1();
                break;
        }
    }

    /**
     * 创建观察者和订阅者
     */
    private void btn1() {
        //被观察者
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("被观察者1");
                e.onNext("被观察者2");
                e.onError(new Throwable("出错了"));
                e.onComplete();
            }
        });
        //观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG,"onSub");         //Disposable 可以提供观察者和被观察者连接和中断
            }
            @Override
            public void onNext(@NonNull String o) {
                Log.d(TAG,o);           //接收上游数据
            }
            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG,e.getLocalizedMessage());
            }
            @Override
            public void onComplete() {
                Log.d(TAG,"完成");
            }
        };
        //实现订阅关联
        observable.subscribe(observer);
    }

    private void btn2(){

        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                e.onNext("产生数据！");
            }
        }, BackpressureStrategy.BUFFER);

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {}
            @Override
            public void onNext(String s) {}
            @Override
            public void onError(Throwable t) {}
            @Override
            public void onComplete() {}
        };
        flowable.map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception { return "变换后"+s;}
                })
                .subscribe(subscriber);


        //map 操作符
        Flowable.just(1)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(@NonNull Integer integer) throws Exception {
                        return "变换成String类型："+integer;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG,"结果："+s);
                    }
                });

    }


}

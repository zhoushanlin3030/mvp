package com.example.zsl.r2test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends Activity implements View.OnClickListener {

    private CompositeDisposable mCompositeDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);
        findViewById(R.id.btn8).setOnClickListener(this);
        mCompositeDis = new CompositeDisposable();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                btn10();
                break;
            case R.id.btn2:
                btn2();
                break;
            case R.id.btn3:
                btn3();
                break;
            case R.id.btn4:
                btn4();
                break;
            case R.id.btn5:
                btn5();
                break;
            case R.id.btn6:
                btn6();
                break;
            case R.id.btn7:
                btn7();
                break;
            case R.id.btn8:
                btn8();
                break;
        }

    }

    /**
     * 简单的just操作符
     */
    private void btn1() {
        mCompositeDis.add(Flowable.just("123")
                .flatMap(new Function<String, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(String s) throws Exception {
                        return Flowable.just(s + "456");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s){
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    /**
     *
     */
    private void btn2(){

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String o) {
                Toast.makeText(MainActivity.this,o,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
            }
        };

        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                e.onNext("djdjjjd");
            }
        }, BackpressureStrategy.BUFFER)
                .flatMap(new Function<String, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(@NonNull String s) throws Exception {
                        return Flowable.just(s+";;;;");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void btn3() {
        mCompositeDis.add(getFlowable()
                .flatMap(new Function<String, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(@NonNull String s) throws Exception {
                        return Flowable.just(s+"......");
                    }
                })
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("",throwable.getLocalizedMessage());
                    }
                }));
    }

    private Flowable<String> getFlowable(){
        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                e.onNext("hhhhh");
            }
        },BackpressureStrategy.BUFFER);
    }

    private void btn4() {
        getFlowable()
                .flatMap(new Function<String, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(@NonNull String s) throws Exception {
                        return Flowable.just(s + "/////");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(String s) {
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void btn5() {
        mCompositeDis.add(Flowable.interval(100,1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Long, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(@NonNull Long aLong) throws Exception {
                        return Flowable.just(String.valueOf(aLong));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("err1:",throwable.getLocalizedMessage());
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d("index-->", s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("err:",throwable.getLocalizedMessage());
                    }
                }));

        mCompositeDis.add(Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        // TODO
                    }
                }));

    }

    private void btn6() {
        mCompositeDis.add(Observable.just(1,2,3,4)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer % 2 == 0;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d("integer:",String.valueOf(integer));
                    }
                }));
    }

    private void btn7() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onNext(4);
                e.onNext(5);
            }
        },BackpressureStrategy.BUFFER)
                .take(1)
                .repeat(2)
                .distinct()
                .subscribeOn(Schedulers.newThread())
                .concatMap(new Function<Integer, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(@NonNull Integer s) throws Exception {
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0;i<5;i++){
                            list.add("变换:"+s);
                        }
                        return Flowable.fromIterable(list);
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d("out:", s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("out:",throwable.getLocalizedMessage());
            }
        });
    }

    private void btn8() {
        Observable observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
            }
        });

        Observable observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("是");
            }
        });

        Observable observable3 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("dd");
            }
        });

        Observable.zip(observable1,observable2,observable3, new Function3<Integer, String, String, String>() {
            @Override
            public String apply(@NonNull Integer integer, @NonNull String s, @NonNull String s2) throws Exception {
                return null;
            }
        }).subscribe();

    }

    private void btn9(){
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {

            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

            }
        });
        mCompositeDis.add(subscribe);

        Flowable<Object> objectFlowable = Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Object> e) throws Exception {

            }
        }, BackpressureStrategy.BUFFER);

        Single<Object> objectSingle = Single.create(new SingleOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Object> e) throws Exception {

            }
        });
        objectSingle.subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }

    private void btn10(){
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Object> e) throws Exception {
                e.onNext(null);
            }
        },BackpressureStrategy.BUFFER).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("error:",throwable.getLocalizedMessage());
            }
        });

    }

    private void btn11(){
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Object> e) throws Exception {
                e.onNext("string");
            }
        },BackpressureStrategy.BUFFER)
                .subscribe(new FlowableSubscriber<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });




    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mCompositeDis != null && !mCompositeDis.isDisposed()){
            mCompositeDis.dispose();
        }
    }
}

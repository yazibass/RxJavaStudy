package com.sweet.rxjavastudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.image_view);

        //subscribe();
        subscribe1();
    }

    private void subscribe() {
        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                emitter.onNext("aaa");
                emitter.onNext("aaaa");
                emitter.onNext("aaaaa");
                emitter.onNext("aaaaaa");
                emitter.onNext("aaaaaaa");
                emitter.onComplete();
            }
        });
        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                //
            }

            @Override
            public void onNext(String str) {
                Log.d("aaa", "onNext str = " + str);
            }

            @Override
            public void onError(Throwable e) {
                //
            }

            @Override
            public void onComplete() {
                //
            }
        });
    }

    private void subscribe1() {
        Log.d("aaa", "subscribe1 thread = " + Thread.currentThread());
        //Observable.just("bbb", "bbbb", "bbbbb", "bbbbbb", "bbbbbbb")
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter emitter) throws Exception {
                        Log.d("aaa", "create.ObservableOnSubscribe-subscribe emitter = " + emitter + ", thread = " + Thread.currentThread());
                        emitter.onNext("aaa");
                        emitter.onNext("aaaa");
                        emitter.onNext("aaaaa");
                        emitter.onNext("aaaaaa");
                        emitter.onNext("aaaaaaa");
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.d("aaa", "doOnSubscribe.Consumer-accept disposable = " + disposable + ", thread = " + Thread.currentThread());
                    }
                })
                .subscribeOn(Schedulers.newThread()/*AndroidSchedulers.mainThread()*/)
                .observeOn(Schedulers.io())
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String str) throws Exception {
                        Log.d("aaa", "map.Function-apply str = " + str + ", thread = " + Thread.currentThread());
                        return str.length();
                    }
                })
                .observeOn(Schedulers.computation())
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        Log.d("aaa", "flatMap.Function-apply integer = " + integer + ", thread = " + Thread.currentThread());
                        String[] strs = new String[2];
                        for(int i = 0; i < 2; i++) {
                            strs[i] = String.valueOf((char)(97 + i));
                        }
                        return Observable.fromArray(strs);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d("aaa", "doOnComplete.Action-run thread = " + Thread.currentThread());
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d("aaa", "doOnTerminate.Action-run thread = " + Thread.currentThread());
                    }
                })
                .observeOn(Schedulers.newThread())
                /*.subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String str) throws Exception {
                        Log.d("aaa", "accept str = " + str + ", thread = " + Thread.currentThread());
                    }
                })*/
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("aaa", "Observer-onSubscribe thread = " + Thread.currentThread());
                    }

                    @Override
                    public void onNext(String str) {
                        Log.d("aaa", "Observer-onNext str = " + str + ", thread = " + Thread.currentThread());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("aaa", "Observer-onError thread = " + Thread.currentThread());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("aaa", "Observer-onComplete thread = " + Thread.currentThread());
                    }
                });
    }
}

package sm.brain.com.rxjavabasic;

import android.app.ProgressDialog;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import api.ApiInterface;
import api.RetroClient;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import notification.NotificationHelper;
import progress.ProgressBarDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retromodels.Download;
import retromodels.DownloadAPI;
import retromodels.DownloadProgressListener;
import retromodels.Lists;

import static notification.NotificationHelper.NOTIFICATION_CHANNEL_ID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView id,percentage;
    ProgressBar progressBar,progressBars;
    ProgressBar progressBarNew;
    NotificationHelper notificationHelper;
    boolean isDownloaded=false;
    long bytesReadOne,tempBytesRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button run=findViewById(R.id.run);
        id=findViewById(R.id.id);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarNew= (ProgressBar)findViewById(R.id.progress_bar_test);
        progressBars = (ProgressBar) findViewById(R.id.progressBars);
        percentage = (TextView) findViewById(R.id.txtProgress);
        notificationHelper=new NotificationHelper(MainActivity.this);
        ProgressBarDrawable bgProgress= new ProgressBarDrawable(8);
        progressBarNew.setProgressDrawable(bgProgress);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                getNetworkCall();
//                callNetwork();
//                retroCall();
                getTwoNetworkCall();
            }
        });



       /* Observable<String> animalsObservable = Observable.just("Ant", "Bee", "Cat", "Dog", "Fox");

        // observable
        Observable<String> animalsObservable = getAnimalsObservable();


        // observer
        Observer<String> animalsObserver = getAnimalsObserver();



        Observer<String> animalsObserver = getAnimalsObserver();
         observer subscribing to observable
        animalsObservable
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(animalsObserver);

        animalsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(animalsObserver);*/

        Observable<String> animalsObservable = getAnimalsObservable();
        Observer<String> animalsObserver = getAnimalsObserver();
//        observer subscribing to observable

        animalsObservable
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(new Function<String, Object>() {
                    @Override
                    public Object apply(String s) throws Exception {
                        return null;
                    }
                });
//                .subscribe(animalsObserver);

        animalsObservable.subscribe(animalsObserver);

    }

    private void callNetwork()
    {
        DownloadProgressListener listener = new DownloadProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                Log.d(TAG, "update: ");
                Download download = new Download();
                download.setTotalFileSize(contentLength);
                download.setCurrentFileSize(bytesRead);
                final int progress = (int) ((bytesRead * 100) / contentLength);
                download.setProgress(progress);


                Log.d(TAG, "updatesss: ");
//                sendNotification(download);
            }
        };


        final String ROOT_URL = "";

        new DownloadAPI(ROOT_URL, listener).downloadAPK("Root_Url", new Observer<Lists>() {
                     @Override
            public void onError(Throwable e) {
                e.printStackTrace();
//                downloadCompleted();
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(Lists o) {
                Log.d(TAG, "onNext: "+o.toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isDownloaded)
        {
            notificationHelper.createNotification("RxJava","Download Interupted");
            notificationHelper.mBuilder.setProgress((int) 0, 0, false);
            notificationHelper.mNotificationManager.notify(0,notificationHelper.mBuilder.build());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();
        if(!isDownloaded)
        {
//            notificationHelper.createNotification("RxJava","Download Interupted");
//            notificationHelper.mBuilder.setProgress(0,0,false);
//            notificationHelper.mNotificationManager.notify(0,notificationHelper.mBuilder.build());
            Log.d(TAG, "onStop: ");
            notificationHelper.mNotificationManager.cancel(0);
        }
    }

    private void getNetworkCall()
    {
        notificationHelper.createNotification("RxJava","Downloading...");
        DownloadProgressListener listener = new DownloadProgressListener() {
            @Override
            public void update(long bytesRead, final long contentLength, boolean done) {
                Log.d(TAG, "update: ");
                Download download = new Download();
                download.setTotalFileSize(contentLength);
                download.setCurrentFileSize(bytesRead);
                final int progress = (int) ((bytesRead * 100) / contentLength);
                download.setProgress(progress);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(progress);
                        id.setText(progress+"/100");

                        progressBars.setProgress(progress);
                        progressBars.setSecondaryProgress(100);
                        percentage.setText(progress+"%");

                        progressBarNew.setProgress(progress);

//
//                        Log.d(TAG, "run: "+progress);
//                        notificationHelper.mBuilder.setProgress((int) 100, progress, false);
//
//                        notificationHelper.mNotificationManager.notify(0,notificationHelper.mBuilder.build());

                    }
                });
//                sendNotification(download);
            }
        };
        ApiInterface service = RetroClient.getApiService(listener);
        Observable<Lists> observable=service.GetDealerCode();
        Observable<Lists> observables=service.GetDealersCode();



        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Lists>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(Lists lists) {
                        Log.d(TAG, "onNext: "+lists.getData());
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG, "onError: "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                        isDownloaded=true;
                        notificationHelper.createNotification("RxJava","Download Completed");
                        notificationHelper.mBuilder.setProgress((int) 0, 0, false);
                        notificationHelper.mNotificationManager.notify(0,notificationHelper.mBuilder.build());
                        progressBar.setProgress(100);
                    }
                });

    }

    private void getTwoNetworkCall()
    {
        notificationHelper.createNotification("RxJava","Downloading...");

        DownloadProgressListener listener = new DownloadProgressListener() {
            @Override
            public void update(final long bytesRead, final long contentLength, boolean done) {
                Log.d(TAG, "update: ");
                Download download = new Download();
                download.setTotalFileSize(contentLength);
                download.setCurrentFileSize(bytesRead);
                final int progress = (int) ((bytesRead * 100) / contentLength);
                download.setProgress(progress);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        progressBar.setProgress(progress);
                        id.setText(progress+"/100");
                        Log.d(TAG, "run: "+progress);

                        if(bytesRead==tempBytesRead)
                        {
                            progressBarNew.setProgress(progress);
                            progressBars.setProgress(progress);
                            progressBars.setSecondaryProgress(100);
                            percentage.setText(progress+"%");

                            progressBarNew.setProgress(progress);
                        }
                        tempBytesRead=bytesRead;


//
//                        Log.d(TAG, "run: "+progress);
//                        notificationHelper.mBuilder.setProgress((int) 100, progress, false);
//
//                        notificationHelper.mNotificationManager.notify(0,notificationHelper.mBuilder.build());

                    }
                });
//                sendNotification(download);
            }
        };
        ApiInterface service = RetroClient.getApiService(listener);
        Observable<Lists> observable=service.GetDealerCode();
        Observable<Lists> observables=service.GetDealersCode();


//        Observable<List<String>> result =Observable.zip(observable.subscribeOn(Schedulers.io()),observables.subscribeOn(Schedulers.io()),observables.subscribeOn(Schedulers.io()
//        ), new Function3<Lists, Lists, Lists, List<String>>() {
//            @Override
//            public List<String> apply(Lists lists, Lists lists2, Lists lists3) throws Exception {
//                List<String> list = new ArrayList();
//                list.add(lists.getResult());
//                list.add(lists.getResult());
//                list.add(lists.getResult());
//                return list;
//            }
//        });

        Observable<List<String>> result =Observable.zip(observable.subscribeOn(Schedulers.newThread()),observables.subscribeOn(Schedulers.newThread())
        , new BiFunction<Lists, Lists, List<String>>() {
        @Override
        public List<String> apply(Lists lists, Lists lists2) throws Exception {
            List<String> list = new ArrayList();
            list.add(lists.getResult());
            list.add(lists2.getResult());
            return list;
        }
    });

        result.observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        Log.d(TAG, "onNext: "+strings.size());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

//        observable.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Lists>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                        Log.d(TAG, "onSubscribe: ");
//                    }
//
//                    @Override
//                    public void onNext(Lists lists) {
//                        Log.d(TAG, "onNext: "+lists.getData());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                        Log.d(TAG, "onError: "+e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete: ");
//                        isDownloaded=true;
//                        notificationHelper.createNotification("RxJava","Download Completed");
//                        notificationHelper.mBuilder.setProgress((int) 0, 0, false);
//                        notificationHelper.mNotificationManager.notify(0,notificationHelper.mBuilder.build());
//                        progressBar.setProgress(100);
//                    }
//                });

    }


    private Observable<String> getAnimalsObservable() {
        return Observable.just("Ant", "Bee", "Cat", "Dog", "Fox");
    }

    private Observer<String> getAnimalsObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");

            }
            @Override
            public void onNext(String s) {
                Log.d(TAG, "Name: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "All items are emitted!");
            }
        };
    }

    private void retroCall(){
        DownloadProgressListener listener = new DownloadProgressListener() {
            @Override
            public void update(long bytesRead, final long contentLength, boolean done) {
                Log.d(TAG, "update: ");
                Download download = new Download();
                download.setTotalFileSize(contentLength);
                download.setCurrentFileSize(bytesRead);
                final int progress = (int) ((bytesRead * 100) / contentLength);
                download.setProgress(progress);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(progress);
                        id.setText(progress+"/100");
                    }
                });
//
//                sendNotification(download);
            }
        };

        ApiInterface service = RetroClient.getApiService(listener);
        Call<Lists> observable=service.GetDealerCodes();

        observable.enqueue(new Callback<Lists>() {
            @Override
            public void onResponse(Call<Lists> call, Response<Lists> response) {
                Log.d(TAG, "onResponse: "+response);
            }

            @Override
            public void onFailure(Call<Lists> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }
}

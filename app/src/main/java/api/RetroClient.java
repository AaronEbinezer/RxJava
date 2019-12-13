package api;

import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retromodels.DownloadProgressInterceptor;
import retromodels.DownloadProgressListener;

public class RetroClient {
    private static final String ROOT_URL = "";

    private static Retrofit getRetrofitInstance(DownloadProgressListener listener) {

        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(
                new GsonBuilder()
                        .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                        .create());

        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okClient(listener))
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    private static OkHttpClient okClient(DownloadProgressListener listener) {
        DownloadProgressInterceptor interceptor = new DownloadProgressInterceptor(listener);
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .connectTimeout(15, TimeUnit.MINUTES)
                .writeTimeout(15, TimeUnit.MINUTES)
                .readTimeout(15, TimeUnit.MINUTES)
                .build()
                ;
    }

    public static ApiInterface getApiService(DownloadProgressListener listener) {
        return getRetrofitInstance(listener).create(ApiInterface.class);
    }
}

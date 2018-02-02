package me.bakumon.ugank.network;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import me.bakumon.ugank.BuildConfig;
import me.bakumon.ugank.network.api.GankApi;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络层
 *
 * @author bakumon
 * @date 16-12-1
 */

public class NetWork {
    private static GankApi gankApi;
    private static OkHttpClient okHttpClient;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static GankApi getGankApi() {
        if (okHttpClient == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.addInterceptor(new LoggingInterceptor.Builder()
                    .loggable(BuildConfig.DEBUG)
                    .setLevel(Level.BASIC)
                    .log(Platform.INFO)
                    .request("Request")
                    .response("Response")
                    .build());
            okHttpClient = client.build();
        }
        if (gankApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://gank.io/api/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            gankApi = retrofit.create(GankApi.class);
        }
        return gankApi;
    }

}

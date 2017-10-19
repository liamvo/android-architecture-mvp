package com.liveteamvn.archmvp.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.liveteamvn.archmvp.BuildConfig;
import com.liveteamvn.archmvp.constant.CommonConstant;
import com.liveteamvn.archmvp.data.local.LiveCoreDatabase;
import com.liveteamvn.archmvp.data.local.dao.FeedDao;
import com.liveteamvn.archmvp.data.remote.LiveCoreApiServices;
import com.liveteamvn.archmvp.base.data.RequestInterceptor;
import com.liveteamvn.archmvp.di.module.PresenterModule;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liam on 09/10/2017.
 */
@Module(includes = PresenterModule.class)
public class AppModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(CommonConstant.TIMEOUT_IN_SEC, TimeUnit.SECONDS);
        okHttpClient.readTimeout(CommonConstant.TIMEOUT_IN_SEC, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(new RequestInterceptor());
        return okHttpClient.build();
    }

    @Provides
    @Singleton
    LiveCoreApiServices provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(LiveCoreApiServices.class);
    }

    @Provides
    @Singleton
    LiveCoreDatabase provideLiveCoreDatabase(Application application) {
        return Room.databaseBuilder(application, LiveCoreDatabase.class, BuildConfig.BASE_DB_NAME).build();
    }

    @Provides
    @Singleton
    FeedDao provideMovieDao(LiveCoreDatabase liveCoreDatabase) {
        return liveCoreDatabase.liveCoreDao();
    }

}

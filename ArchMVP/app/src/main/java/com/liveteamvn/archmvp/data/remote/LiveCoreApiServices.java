package com.liveteamvn.archmvp.data.remote;

import com.liveteamvn.archmvp.data.local.entity.FeedEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by liam on 09/10/2017.
 */

public interface LiveCoreApiServices {

    @GET("BaoMoiCrawler/GetFeed/{page}")
    Call<List<FeedEntity>> getListFeed(@Path("page") int page);
}

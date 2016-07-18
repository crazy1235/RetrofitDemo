package com.jacksen.retrofitdemo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Admin on 2016/7/18.
 */

public interface ApiStore {

    @Headers({"apikey: 48b0fc4c8047a43a9da5d5c1f65df2e9"})
    @GET(Constants.BASE_URL + Constants.QUERY_MOBILE_URL)
    Call<MobileInfo> getMobileInfo(@Query("phone") String mobileNum);
}

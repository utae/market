package kr.co.t_woori.market.main;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-25.
 */

public interface SplashAPIService {

    @FormUrlEncoded
    @POST("/market/isUser")
    Call<HashMap<String, Object>> isUser(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("/market/signUp")
    Call<HashMap<String, Object>> signup(@Field("name") String name, @Field("phone") String phone, @Field("address") String address);
}

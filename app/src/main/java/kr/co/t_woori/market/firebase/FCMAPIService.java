package kr.co.t_woori.market.firebase;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-25.
 */

public interface FCMAPIService {

    @FormUrlEncoded
    @POST("/market/fcm")
    Call<HashMap<String, Object>> insertFCMToken(@Field("phone") String phone, @Field("token") String token);
}

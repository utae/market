package kr.co.t_woori.market.drawer;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-25.
 */

public interface MyInfoAPIService {

    @FormUrlEncoded
    @POST("/market/user/info")
    Call<HashMap<String, Object>> modMyInfo(@Field("name") String name, @Field("phone") String phone, @Field("address") String address);
}

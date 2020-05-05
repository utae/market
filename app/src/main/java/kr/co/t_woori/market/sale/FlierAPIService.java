package kr.co.t_woori.market.sale;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-25.
 */

public interface FlierAPIService {

    @GET("/market/flier")
    Call<HashMap<String, Object>> getFlierList();

    @FormUrlEncoded
    @POST("/market/flier/goods")
    Call<HashMap<String, Object>> getFlierGoods(@Field("id") String id);

    @GET("/market/flier/goods")
    Call<HashMap<String, Object>> getLatestFlierGoods();
}

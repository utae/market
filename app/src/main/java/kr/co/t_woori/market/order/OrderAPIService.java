package kr.co.t_woori.market.order;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-25.
 */

public interface OrderAPIService {

    @FormUrlEncoded
    @POST("/market/user/get/point")
    Call<HashMap<String, Object>> getUserPoint(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("/market/user/point/phone")
    Call<HashMap<String, Object>> modUserPoint(@Field("phone") String phone, @Field("point") String point);

    @FormUrlEncoded
    @POST("/market/order")
    Call<HashMap<String, Object>> order(@Field("phone") String phone, @Field("totalPrice") String totalPrice, @Field("address") String address, @Field("deliveryTime") String deliveryTime, @Field("request") String request, @Field("payment") String payment, @Field("goodsList") String goodsList, @Field("recipient") String recipient, @Field("phone2") String phone2);

    @FormUrlEncoded
    @POST("/market/order/list")
    Call<HashMap<String, Object>> getOrderList(@Field("phone") String phone);

    @GET("/market/order/list")
    Call<HashMap<String, Object>> getAllOrderList();

    @FormUrlEncoded
    @POST("/market/order/done")
    Call<HashMap<String, Object>> completeOrder(@Field("orderNum") String orderNum);

    @FormUrlEncoded
    @POST("/market/user/point/add")
    Call<HashMap<String, Object>> addUserPoint(@Field("phone") String phone, @Field("point") String point);

    @FormUrlEncoded
    @POST("/market/order/goods")
    Call<HashMap<String, Object>> getOrderGoodsList(@Field("orderNum") String orderNum);
}

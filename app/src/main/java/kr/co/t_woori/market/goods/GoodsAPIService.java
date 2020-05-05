package kr.co.t_woori.market.goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by utae on 2017-07-25.
 */

public interface GoodsAPIService {

    @FormUrlEncoded
    @POST("/market/goods/category")
    Call<HashMap<String, Object>> getGoodsFromCategory(@Field("categoryList") String categoryList);

    @GET("/market/goods/popular")
    Call<HashMap<String, Object>> getPopularGoods();

    @GET("/market/goods/bargain")
    Call<HashMap<String, Object>> getBargainGoods();

    @GET("/market/goods/sale")
    Call<HashMap<String, Object>> getSaleGoods();

    @GET("/market/goods/event")
    Call<HashMap<String, Object>> getEventGoods();

    @FormUrlEncoded
    @POST("/market/goods/popular")
    Call<HashMap<String, Object>> insertPopularGoods(@Field("barcode") String barcode);

    @FormUrlEncoded
    @POST("/market/goods/bargain")
    Call<HashMap<String, Object>> insertBargainGoods(@Field("barcode") String barcode);

    @FormUrlEncoded
    @POST("/market/goods/sale")
    Call<HashMap<String, Object>> insertSaleGoods(@Field("barcode") String barcode);

    @FormUrlEncoded
    @POST("/market/goods/event")
    Call<HashMap<String, Object>> insertEventGoods(@Field("barcode") String barcode);

    @FormUrlEncoded
    @POST("/market/goods/popular/del")
    Call<HashMap<String, Object>> delPopularGoods(@Field("barcode") String barcode);

    @FormUrlEncoded
    @POST("/market/goods/bargain/del")
    Call<HashMap<String, Object>> delBargainGoods(@Field("barcode") String barcode);

    @FormUrlEncoded
    @POST("/market/goods/sale/del")
    Call<HashMap<String, Object>> delSaleGoods(@Field("barcode") String barcode);

    @FormUrlEncoded
    @POST("/market/goods/event/del")
    Call<HashMap<String, Object>> delEventGoods(@Field("barcode") String barcode);

    @FormUrlEncoded
    @POST("/market/goods/discount")
    Call<HashMap<String, Object>> modGoodsDiscount(@Field("barcode") String barcode, @Field("discount") String discount);

    @Multipart
    @POST("/market/goods/img")
    Call<ResponseBody> uploadGoodsImg(@Part("barcode") RequestBody idNum, @Part List<MultipartBody.Part> fileList);

    @FormUrlEncoded
    @POST("/market/goods/img/del")
    Call<HashMap<String, Object>> delGoodsImg(@Field("barcode") String barcode);

    @FormUrlEncoded
    @POST("/market/goods/search")
    Call<HashMap<String, Object>> searchGoods(@Field("word") String word);
}

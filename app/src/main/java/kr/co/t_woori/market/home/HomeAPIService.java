package kr.co.t_woori.market.home;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by utae on 2017-07-25.
 */

public interface HomeAPIService {

    @Multipart
    @POST("/market/banner/img")
    Call<ResponseBody> uploadBannerImg(@Part("bannerName") RequestBody bannerName, @Part List<MultipartBody.Part> fileList);
}

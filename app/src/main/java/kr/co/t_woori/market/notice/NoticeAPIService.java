package kr.co.t_woori.market.notice;

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

public interface NoticeAPIService {

    @GET("/market/notice")
    Call<HashMap<String, Object>> getNotice();

    @FormUrlEncoded
    @POST("/market/notice")
    Call<HashMap<String, Object>> insertNotice(@Field("title") String title, @Field("content") String content);

    @FormUrlEncoded
    @POST("/market/notice/del")
    Call<HashMap<String, Object>> delNotice(@Field("noticeNum") String noticeNum);

    @FormUrlEncoded
    @POST("/market/notice/detail")
    Call<HashMap<String, Object>> getNoticeById(@Field("noticeNum") String noticeNum);
}

package kr.co.t_woori.market.communication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.co.t_woori.market.goods.GoodsAPIService;
import kr.co.t_woori.market.utilities.Utilities;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rladn on 2017-08-30.
 */

public class GoodsImageUploader extends AppCompatActivity {

    private String barcode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilities.clearGlideCache(this);
        barcode = getIntent().getStringExtra("barcode");
        if(barcode == null){
            finish();
        }else{
            getFileFromGallery();
        }
    }

    private void getFileFromGallery() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(false)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }


    private void readyToUpload(String barcode, List<String> imgList) {

        List<MultipartBody.Part> fileList = new ArrayList<>();

        RequestBody barcodeBody = RequestBody.create(MediaType.parse("text/plain"), barcode);

        for(String img : imgList){
            // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
            // use the FileUtils to get the actual file by uri
            File file = new File(img);

            Uri uri = Uri.fromFile(file);

            // create RequestBody instance from file
            String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            RequestBody requestFile = RequestBody.create(MediaType.parse(MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body = MultipartBody.Part.createFormData(barcode + "_img", file.getName(), requestFile);

            fileList.add(body);
        }

        uploadGoodsImg(barcodeBody, fileList);
    }

    private void uploadGoodsImg(RequestBody barcodeBody, List<MultipartBody.Part> fileList){
        APICreator.create(GoodsAPIService.class).uploadGoodsImg(barcodeBody, fileList).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utilities.showToast(GoodsImageUploader.this, "업로드 성공");
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utilities.showToast(GoodsImageUploader.this, "업로드 실패");
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PhotoPicker.REQUEST_CODE && data != null) {
                ArrayList<String> imgList = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                readyToUpload(barcode, imgList);
            }
        }else{
            finish();
        }
    }
}
package kr.co.t_woori.market.utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.t_woori.market.BuildConfig;
import kr.co.t_woori.market.goods.Goods;


/**
 * Created by Utae on 2017-07-18.
 */

public class Utilities {

    public static void logD (String TAG, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }

    public static long getCurrentTimeMinutes(){
        return System.currentTimeMillis()/60000;
    }

    public static long convertMinutesToTimeMillis(long minutes){
        return TimeUnit.MINUTES.toMillis(minutes);
    }

    public static String convertTimeMillisToTimeFormat(long timeMillis){
        return convertTimeMillisToTimeFormat(timeMillis, null);
    }

    public static String convertTimeMillisToTimeFormat(long timeMillis, String dateFormat){
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance();
        if(dateFormat == null){
            simpleDateFormat.applyPattern("yyyyMMddHHmmss");
        }else{
            simpleDateFormat.applyPattern(dateFormat);
        }
        return simpleDateFormat.format(new Date(timeMillis));
    }

    public static String convertStringToDateFormat(String dateString){
        SimpleDateFormat format = (SimpleDateFormat)SimpleDateFormat.getDateInstance();
        format.applyPattern("yyyyMMdd");
        try {
            return SimpleDateFormat.getDateInstance(DateFormat.MEDIUM).format(format.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertStringToDateFormat(String dateString, String currentFormatString, String convertFormatString){
        SimpleDateFormat currentFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance();
        currentFormat.applyPattern(currentFormatString);
        SimpleDateFormat convertFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance();
        convertFormat.applyPattern(convertFormatString);
        try {
            return convertFormat.format(currentFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertStringToNumberFormat(String numberString){
        return NumberFormat.getInstance().format(Integer.parseInt(numberString));
    }

    public static float getDip(Context context, int value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidPassword(CharSequence password){
        String Passwrod_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^&*()+=-]|.*[0-9]+).{6,20}$";
        Pattern pattern = Pattern.compile(Passwrod_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isOnlyKorean(CharSequence input){
        String Passwrod_PATTERN = "^[가-힣]+$";
        Pattern pattern = Pattern.compile(Passwrod_PATTERN);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static String byteAsString(byte[] dataBytes) throws Exception {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < dataBytes.length; i++) {
            String hex = Integer.toHexString(0xFF & dataBytes[i]);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }

    public static void hideKeyboard(Context context, EditText editText){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showKeyboard(Context context, EditText editText){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    public static ArrayList<int[]> getSpans(String body, char prefix) {
        ArrayList<int[]> spans = new ArrayList<>();

        Pattern pattern = Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);

        // Check all occurrences
        while (matcher.find()) {
            int[] currentSpan = new int[2];
            currentSpan[0] = matcher.start();
            currentSpan[1] = matcher.end();
            spans.add(currentSpan);
        }

        return  spans;
    }

    public static ArrayList<String> getSpanStrings(String body, char prefix) {
        ArrayList<String> spans = new ArrayList<>();

        Pattern pattern = Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);

        // Check all occurrences
        while (matcher.find()) {
            spans.add(matcher.group());
        }

        return spans;
    }

    static public BitmapFactory.Options getBitmapSize(File imageFile){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        return options;
    }

    static public void showToast(Context context, CharSequence message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    static public String getGoodsImgDir(){
        return "http://112.217.229.162/image/market/";
    }

    static public void clearGlideCache(Context context){
        Glide.get(context).clearMemory();
        new AsyncTask<Context, Void, Void>() {
            @Override
            protected Void doInBackground(Context... params) {
                Glide.get(params[0]).clearDiskCache();
                return null;
            }
        }.execute(context);
    }

    static public void addGoodsInCart(Context context, Goods goods){
        if(getCartList(context).contains(goods)){
            showToast(context, "이미 추가된 상품입니다.");
        }else{
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            JSONArray jsonArray;
            try {
                if(prefs.getString("cart", null) != null){
                    jsonArray = new JSONArray(prefs.getString("cart", null));
                }else{
                    jsonArray = new JSONArray();
                }
                jsonArray.put(goods.toJSONObject());
                prefs.edit().putString("cart", jsonArray.toString()).apply();
                showToast(context, "해당상품이 장바구니에 추가되었습니다.");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public ArrayList<Goods> getCartList(Context context){
        ArrayList<Goods> goodsList = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            if(prefs.getString("cart", null) != null){
                JSONArray jsonArray = new JSONArray(prefs.getString("cart", null));
                JSONObject jsonObject;
                Goods goods;
                for(int i = 0; i < jsonArray.length(); i++){
                    jsonObject = jsonArray.optJSONObject(i);
                    if(jsonObject != null){
                        String barcode = jsonObject.optString("barcode");
                        String name = jsonObject.optString("name");
                        String price = jsonObject.optString("price");
                        String discount = jsonObject.optString("discount");
                        String capacity = jsonObject.optString("capacity");
                        int amount = jsonObject.optInt("amount");
                        boolean hasImg = jsonObject.optBoolean("img");
                        if(discount == null || "".equals(discount.trim())){
                            goods = new Goods(barcode, name, price, capacity, hasImg);
                        }else{
                            goods = new Goods(barcode, name, price, capacity, hasImg, discount);
                        }
                        goods.setAmount(amount);
                        goodsList.add(goods);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return goodsList;
    }

    static public void saveCartList(Context context, List<Goods> goodsList){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        JSONArray jsonArray = new JSONArray();
        for(Goods goods : goodsList){
            jsonArray.put(goods.toJSONObject());
        }
        prefs.edit().putString("cart", jsonArray.toString()).apply();
    }

    static public void showSimpleAlertDialog(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    static public void popAllofBackStack(FragmentManager fragmentManager){
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); i++){
            fragmentManager.popBackStack();
        }
    }

    static public ArrayList<Goods> getLastOrder(Context context){
        ArrayList<Goods> goodsList = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            if(prefs.getString("lastOrder", null) != null){
                JSONArray jsonArray = new JSONArray(prefs.getString("lastOrder", null));
                JSONObject jsonObject;
                Goods goods;
                for(int i = 0; i < jsonArray.length(); i++){
                    jsonObject = jsonArray.optJSONObject(i);
                    if(jsonObject != null){
                        String barcode = jsonObject.optString("barcode");
                        String name = jsonObject.optString("name");
                        String price = jsonObject.optString("price");
                        String discount = jsonObject.optString("discount");
                        String capacity = jsonObject.optString("capacity");
                        int amount = jsonObject.optInt("amount");
                        boolean hasImg = jsonObject.optBoolean("img");
                        if(discount == null || "".equals(discount.trim())){
                            goods = new Goods(barcode, name, price, capacity, hasImg);
                        }else{
                            goods = new Goods(barcode, name, price, capacity, hasImg, discount);
                        }
                        goods.setAmount(amount);
                        goodsList.add(goods);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return goodsList;
    }

    static public void saveLastOrder(Context context, ArrayList<Goods> goodsList){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        JSONArray jsonArray = new JSONArray();
        for(Goods goods : goodsList){
            jsonArray.put(goods.toJSONObject());
        }
        prefs.edit().putString("lastOrder", jsonArray.toString()).remove("cart").apply();
    }
}

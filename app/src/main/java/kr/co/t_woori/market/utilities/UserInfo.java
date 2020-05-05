package kr.co.t_woori.market.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import java.util.HashMap;

/**
 * Created by Utae on 2015-11-06.
 */
public class UserInfo {

    static private HashMap<String, Object> userInfo = new HashMap<>();

    @SuppressLint("HardwareIds")
    static public String getUserAndroidId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    static public void setPhoneNum(String phoneNum){
        userInfo.put("phoneNum", phoneNum);
    }

    static public String getPhoneNum(){
        if(userInfo.get("phoneNum") == null){
            return null;
        }else{
            return (String)userInfo.get("phoneNum");
        }
    }

    static public void setName(String name){
        userInfo.put("name", name);
    }

    static public String getName(){
        if(userInfo.get("name") == null){
            return null;
        }else{
            return (String)userInfo.get("name");
        }
    }

    static public void setAddress(String address){
        userInfo.put("address", address);
    }

    static public String getAddress(){
        if(userInfo.get("address") == null){
            return null;
        }else{
            return (String)userInfo.get("address");
        }
    }

    static public void setMaster(){
        userInfo.put("class", true);
    }

    static public boolean isMaster() {
        return userInfo.get("class") != null && (boolean) userInfo.get("class");
    }

    static public void clearUserInfo(){
        userInfo.clear();
    }
}

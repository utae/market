package kr.co.t_woori.market.goods;

import android.support.annotation.IdRes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by rladn on 2017-08-16.
 */

public class Goods implements Serializable {

    private String barcode;
    private String name;
    private String price;
    private String discountPrice;
    private String capacity;
    private boolean hasImg;
    private int amount = 0;

    public Goods(String barcode, String name, String price, String capacity, boolean hasImg) {
        this(barcode, name, price, capacity, hasImg, null);
    }

    public Goods(String barcode, String name, String price, String capacity, boolean hasImg, String discountPrice) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.capacity = capacity;
        this.hasImg = hasImg;
        this.discountPrice = discountPrice;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public String getCapacity() {
        return capacity;
    }

    public boolean hasImg() {
        return hasImg;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public JSONObject toJSONObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("barcode", barcode);
            jsonObject.put("name", name);
            jsonObject.put("amount", amount);
            jsonObject.put("img", hasImg);
            jsonObject.put("price", price);
            jsonObject.put("capacity", capacity);
            if(discountPrice != null){
                jsonObject.put("discount", discountPrice);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Goods && this.barcode.equals(((Goods) obj).getBarcode());
    }

    public int getTotalPrice(){
        if(discountPrice == null){
            return Integer.parseInt(price) * amount;
        }else{
            return Integer.parseInt(discountPrice) * amount;
        }
    }
}

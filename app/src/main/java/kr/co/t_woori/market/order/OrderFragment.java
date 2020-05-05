package kr.co.t_woori.market.order;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.APICreator;
import kr.co.t_woori.market.communication.ServerCommunicator;
import kr.co.t_woori.market.databinding.OrderFragmentBinding;
import kr.co.t_woori.market.goods.Goods;
import kr.co.t_woori.market.utilities.UserInfo;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-25.
 */

public class OrderFragment extends Fragment {

    private OrderFragmentBinding binding;
    private ArrayList<Goods> goodsList;

    private int point, usingPoint = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.order_fragment, container, false);

        getUserPoint();

        goodsList = Utilities.getCartList(getContext());

        binding.addressEdt.setText(UserInfo.getAddress());

        binding.phoneEdt.setText(UserInfo.getPhoneNum());

        binding.deliveryTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.create().setOnSelectedListener(new TimePickerDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int hour, int min) {
                        if(hour < 8 || hour > 19 || (hour == 19 && min > 30)){
                            Utilities.showToast(getContext(), "배송불가시간입니다.");
                        }else{
                            binding.deliveryTimeTextView.setText(hour + ":" + min);
                        }
                    }
                }).show(getFragmentManager(), "time picker dialog");
            }
        });

        binding.pointUseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(binding.pointUseEdt.getText().toString().trim())){
                    Utilities.showToast(getContext(), "사용할 포인트를 입력하세요.");
                }else if(point < Integer.parseInt(binding.pointUseEdt.getText().toString().trim())){
                    Utilities.showToast(getContext(), "사용가능 포인트를 초과합니다.");
                }else{
                    usingPoint = Integer.parseInt(binding.pointUseEdt.getText().toString().trim());
                    binding.pointUseTextView.setText("-" + usingPoint + "원");
                    binding.totalPriceTextView.setText(getTotalPrice() + "원");
                }
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        binding.orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllFormFilled()){
                    String point = Integer.toString(OrderFragment.this.point - usingPoint);
                    String phone = UserInfo.getPhoneNum();
                    String totalPrice = Integer.toString(getTotalPrice());
                    String address = binding.addressEdt.getText().toString().trim();
                    String deliveryTime = binding.deliveryTimeTextView.getText().toString().replace(":", "");
                    String request = binding.requestEdt.getText().toString().trim();
                    String payment = getPayment();
                    String barcodeList = getBarcodeList(goodsList);
                    String recipient = binding.recipientEdt.getText().toString().trim();
                    String phone2 = binding.phoneEdt.getText().toString().trim();
                    modUserPoint(point, phone, totalPrice, address, deliveryTime, request, payment, barcodeList, recipient, phone2);
                }
            }
        });

        binding.orderPriceTextView.setText(getTotalPrice() + "원");
        binding.totalPriceTextView.setText(getTotalPrice() + "원");

        return binding.getRoot();
    }

    private boolean isAllFormFilled(){
        if(!isFilled(binding.addressEdt)){
            Utilities.showToast(getContext(), "배송지를 입력하세요.");
        }else if("선택".equals(binding.deliveryTimeTextView.getText().toString())){
            Utilities.showToast(getContext(), "배송시간을 선택하세요.");
        }else if(!isCheckedPayment()){
            Utilities.showToast(getContext(), "결제 방식을 선택하세요.");
        }else{
            return true;
        }
        return false;
    }

    private boolean isCheckedPayment(){
        return binding.paymentRadioGroup.getCheckedRadioButtonId() != -1;
    }

    private boolean isFilled(EditText editText){
        return editText.getText() != null && !"".equals(editText.getText().toString().trim());
    }

    private int getTotalPrice(){
        int totalPrice = 0;
        for(Goods goods : goodsList){
            totalPrice += goods.getTotalPrice();
        }
        if(usingPoint != 0){
            return totalPrice - usingPoint;
        }
        return totalPrice;
    }

    private String getPayment(){
        if(binding.paymentRadioGroup.getCheckedRadioButtonId() == binding.cashBtn.getId()){
            return "CASH";
        }else{
            return "CARD";
        }
    }

    private String getBarcodeList(List<Goods> goodsList){
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for(Goods goods : goodsList){
            jsonObject = new JSONObject();
            try {
                jsonObject.put("barcode", goods.getBarcode());
                jsonObject.put("amount", goods.getAmount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }

    private void getUserPoint(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(OrderAPIService.class).getUserPoint(UserInfo.getPhoneNum())
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                point = Integer.parseInt((String) results.get("point"));
                binding.pointTextView.setText(point + "원");
            }
        };
        serverCommunicator.execute();
    }

    private void modUserPoint(String point, final String phone, final String totalPrice, final String address, final String deliveryTime, final String request, final String payment, final String orderGoodsList, final String recipient, final String phone2){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(OrderAPIService.class).modUserPoint(UserInfo.getPhoneNum(), point)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                order(phone, totalPrice, address, deliveryTime, request, payment, orderGoodsList, recipient, phone2);
            }
        };
        serverCommunicator.execute();
    }

    private void order(String phone, String totalPrice, String address, String deliveryTime, String request, String payment, String orderGoodsList, String recipient, String phone2){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(OrderAPIService.class).order(phone, totalPrice, address, deliveryTime, request, payment, orderGoodsList, recipient, phone2)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.saveLastOrder(getContext(), goodsList);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("주문이 완료되었습니다.\n주문내역을 확인하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utilities.popAllofBackStack(getFragmentManager());
                                getFragmentManager().beginTransaction().add(R.id.main_container, new OrderHistoryListFragment()).addToBackStack(null).commit();
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utilities.popAllofBackStack(getFragmentManager());
                            }
                        })
                        .show();
            }
        };
        serverCommunicator.execute();
    }
}

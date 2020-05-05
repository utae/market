package kr.co.t_woori.market.goods;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.HashMap;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.APICreator;
import kr.co.t_woori.market.communication.GoodsImageUploader;
import kr.co.t_woori.market.communication.ServerCommunicator;
import kr.co.t_woori.market.databinding.ManageGoodsDialogBinding;
import kr.co.t_woori.market.utilities.Utilities;

/**
 * Created by rladn on 2017-10-23.
 */

public class ManageGoodsDialog extends DialogFragment {

    private ManageGoodsDialogBinding binding;
    private String type;
    private Goods goods;

    private OnManageGoodsDialogDismissListener dismissListener;

    public static ManageGoodsDialog create(String type, Goods goods){
        ManageGoodsDialog dialog = new ManageGoodsDialog();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putSerializable("goods", goods);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        goods = (Goods) getArguments().getSerializable("goods");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.manage_goods_dialog, container, false);

        switch (type){
            case "P" :
                binding.popularBtn.setText("인기상품에서\n제거");
                binding.popularBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delPopularGoods(goods.getBarcode());
                    }
                });
                binding.bargainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBargainGoods(goods.getBarcode());
                    }
                });
                binding.saleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addSaleGoods(goods.getBarcode());
                    }
                });
                binding.eventBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addEventGoods(goods.getBarcode());
                    }
                });
                break;

            case "B" :
                binding.bargainBtn.setText("한정특가에서\n제거");
                binding.popularBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addPopularGoods(goods.getBarcode());
                    }
                });
                binding.bargainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delBargainGoods(goods.getBarcode());
                    }
                });
                binding.saleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addSaleGoods(goods.getBarcode());
                    }
                });
                binding.eventBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addEventGoods(goods.getBarcode());
                    }
                });
                break;

            case "S" :
                binding.saleBtn.setText("세일상품에서\n제거");
                binding.popularBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addPopularGoods(goods.getBarcode());
                    }
                });
                binding.bargainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBargainGoods(goods.getBarcode());
                    }
                });
                binding.saleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delSaleGoods(goods.getBarcode());
                    }
                });
                binding.eventBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addEventGoods(goods.getBarcode());
                    }
                });
                break;

            case "E" :
                binding.eventBtn.setText("행사상품에서\n제거");
                binding.popularBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addPopularGoods(goods.getBarcode());
                    }
                });
                binding.bargainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBargainGoods(goods.getBarcode());
                    }
                });
                binding.saleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addSaleGoods(goods.getBarcode());
                    }
                });
                binding.eventBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delEventGoods(goods.getBarcode());
                    }
                });
                break;

            default :
                binding.popularBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addPopularGoods(goods.getBarcode());
                    }
                });
                binding.bargainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBargainGoods(goods.getBarcode());
                    }
                });
                binding.saleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addSaleGoods(goods.getBarcode());
                    }
                });
                binding.eventBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addEventGoods(goods.getBarcode());
                    }
                });
                break;
        }

        binding.modImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GoodsImageUploader.class);
                intent.putExtra("barcode", goods.getBarcode());
                startActivity(intent);
            }
        });

        binding.discountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert);
                builder.setMessage("수정할 할인가를 입력하세요.")
                        .setView(R.layout.discount_dialog)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(dialog instanceof AlertDialog){
                                    EditText editText = (EditText)((AlertDialog)dialog).findViewById(R.id.edit_text);
                                    if(editText != null){
                                        if(Integer.parseInt(editText.getText().toString().trim()) < Integer.parseInt(goods.getPrice())){
                                            if(goods.getDiscountPrice() != null && editText.getText().toString().trim().equals(goods.getDiscountPrice())){
                                                Utilities.showToast(getContext(), "할인가가 수정되었습니다.");
                                                dialog.dismiss();
                                                dismiss();
                                            }else{
                                                dialog.dismiss();
                                                modDiscountPrice(goods.getBarcode(), editText.getText().toString().trim());
                                            }
                                        }else{
                                            Utilities.showToast(getContext(), "정상가보다 낮은 가격으로 설정해주세요.");
                                        }
                                    }else{
                                        dialog.dismiss();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        binding.delImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delGoodsImg(goods.getBarcode());
            }
        });

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return binding.getRoot();
    }

    private void addPopularGoods(String barcode){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(GoodsAPIService.class).insertPopularGoods(barcode)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "인기상품에 등록되었습니다.");
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    private void addBargainGoods(String barcode){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(GoodsAPIService.class).insertBargainGoods(barcode)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "한정특가에 등록되었습니다.");
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    private void addSaleGoods(String barcode){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(GoodsAPIService.class).insertSaleGoods(barcode)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "세일상품에 등록되었습니다.");
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    private void addEventGoods(String barcode){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(GoodsAPIService.class).insertEventGoods(barcode)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "행사상품에 등록되었습니다.");
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    private void delPopularGoods(String barcode){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(GoodsAPIService.class).delPopularGoods(barcode)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "인기상품에서 삭제제되었습니다.");
                if(dismissListener != null){
                    dismissListener.onDismiss();
                }
                dismiss();
            }
       };
        serverCommunicator.execute();
    }

    private void delBargainGoods(String barcode){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(GoodsAPIService.class).delBargainGoods(barcode)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "한정특가에서 삭제제되었습니다.");
                if(dismissListener != null){
                    dismissListener.onDismiss();
                }
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    private void delSaleGoods(String barcode){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(GoodsAPIService.class).delSaleGoods(barcode)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "세일상품에서 삭제제되었습니다.");
                if(dismissListener != null){
                    dismissListener.onDismiss();
                }
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    private void delEventGoods(String barcode){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(GoodsAPIService.class).delEventGoods(barcode)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "행사상품에서 삭제제되었습니다.");
                if(dismissListener != null){
                    dismissListener.onDismiss();
                }
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    private void modDiscountPrice(String barcode, String discount){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(GoodsAPIService.class).modGoodsDiscount(barcode, discount)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "할인가가 수정되었습니다.");
                if(dismissListener != null){
                    dismissListener.onDismiss();
                }
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    private void delGoodsImg(String barcode){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(GoodsAPIService.class).delGoodsImg(barcode)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "상품이미지가 삭제되었습니다.");
                if(dismissListener != null){
                    dismissListener.onDismiss();
                }
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    public ManageGoodsDialog setDismissListener(OnManageGoodsDialogDismissListener dismissListener) {
        this.dismissListener = dismissListener;
        return this;
    }

    public interface OnManageGoodsDialogDismissListener{
        void onDismiss();
    }
}

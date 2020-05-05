package kr.co.t_woori.market.home;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import kr.co.t_woori.market.R;

/**
 * Created by rladn on 2017-09-08.
 */

public class Category {

    public static LinkedHashMap<String, ArrayList<Integer>> getCategory(){
        LinkedHashMap<String, ArrayList<Integer>> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("야채", new ArrayList<>(Arrays.asList(10101)));
        linkedHashMap.put("청과", new ArrayList<>(Arrays.asList(10202)));
        linkedHashMap.put("(냉장식품)\n햄 어묵 유부 소세지\n냉면 우동\n칼국수 떡국떡\n맛살 단무지", new ArrayList<>(Arrays.asList(99900)));
        linkedHashMap.put("(냉동식품)\n만두 동그랑땡\n떡갈비 돈까스\n치킨너겟 핫도그", new ArrayList<>(Arrays.asList(20108)));
        linkedHashMap.put("정육", new ArrayList<>(Arrays.asList(10303, 10808)));
        linkedHashMap.put("생선 육계", new ArrayList<>(Arrays.asList(10404, 11010)));
        linkedHashMap.put("(조미료)\n다시다 맛소금 후추\n미원 연두\n식초 물엿\n소금 설탕\n고추가루 파스타", new ArrayList<>(Arrays.asList(20404)));
        linkedHashMap.put("(가공식품)\n국수 당면\n죽 즉석밥\n스프 라면\n카레 짜장 씨리얼", new ArrayList<>(Arrays.asList(20104)));
        linkedHashMap.put("반찬", new ArrayList<>(Arrays.asList(10606)));
        linkedHashMap.put("(건어류)\n미역 김 다시마\n멸치 북어채 맛진미\n쥐포 건새우\n육포 견과류\n황태포 강정 약과\n제수용품 곶감", new ArrayList<>(Arrays.asList(10505)));
        linkedHashMap.put("(장류)\n고추장 된장\n액젓 간장\n쌈장 식용유\n갈비양념 소스", new ArrayList<>(Arrays.asList(20105)));
        linkedHashMap.put("참치 햄\n통조림\n보리차\n커피 차 꿀", new ArrayList<>(Arrays.asList(20106)));
        linkedHashMap.put("콩나물 두부 묵\n깨\n젓갈 찌개용된장\n계란 메추리알", new ArrayList<>(Arrays.asList(20403, 21001, 21101)));
        linkedHashMap.put("음료 생수 요구르트\n우유 두유 치즈\n두유세트 유제품\n음료세트\n아이스크림", new ArrayList<>(Arrays.asList(20102, 21201, 21202)));
        linkedHashMap.put("샴푸 린스\n치약 칫솔\n비누 염색약\n바디용품", new ArrayList<>(Arrays.asList(20204, 20205, 20210)));
        linkedHashMap.put("주방세제\n세탁세제\n섬유유연제\n표백제", new ArrayList<>(Arrays.asList(20206, 20207, 20208, 20211)));
        linkedHashMap.put("영양곡\n잡곡\n쌀", new ArrayList<>(Arrays.asList(20301, 20302, 20303)));
        linkedHashMap.put("과자 사탕\n스낵 초콜릿\n비스킷", new ArrayList<>(Arrays.asList(20103)));
        linkedHashMap.put("락스\n공기탈취제\n건전지\n면도기", new ArrayList<>(Arrays.asList(20214)));
        linkedHashMap.put("화장지 기저귀\n생리용품 키친타올\n물티슈 미용티슈\n분유 유아용품", new ArrayList<>(Arrays.asList(20109, 20202, 20203)));
        linkedHashMap.put("빵 떡\n쨈류\n케찹 마요네즈\n드레싱소스", new ArrayList<>(Arrays.asList(20110, 21401)));
        linkedHashMap.put("주방잡화\n청소도구\n전기용품 양초 실\n일회용품 향초\n종이컵 부탄가스\n애완견용품", new ArrayList<>(Arrays.asList(20601, 20501, 21301)));
        linkedHashMap.put("밀가루\n튀김 부침\n빵가루\n믹스\n미숫가루", new ArrayList<>(Arrays.asList(99901)));
        linkedHashMap.put(null, null);
        return linkedHashMap;
    }

    public static ArrayList<Integer> getCategoryList(int index){
        return getCategory().get((getCategory().keySet().toArray())[index]);
    }

    public static ArrayList<Integer> getCategoryImgList(){
        ArrayList<Integer> imgList = new ArrayList<>();
        imgList.add(R.drawable.img_main_category_01);
        imgList.add(R.drawable.img_main_category_02);
        imgList.add(R.drawable.img_main_category_03);
        imgList.add(R.drawable.img_main_category_04);
        imgList.add(R.drawable.img_main_category_05);
        imgList.add(R.drawable.img_main_category_06);
        imgList.add(R.drawable.img_main_category_07);
        imgList.add(R.drawable.img_main_category_08);
        imgList.add(R.drawable.img_main_category_09);
        imgList.add(R.drawable.img_main_category_10);
        imgList.add(R.drawable.img_main_category_11);
        imgList.add(R.drawable.img_main_category_12);
        imgList.add(R.drawable.img_main_category_13);
        imgList.add(R.drawable.img_main_category_14);
        imgList.add(R.drawable.img_main_category_15);
        imgList.add(R.drawable.img_main_category_16);
        imgList.add(R.drawable.img_main_category_17);
        imgList.add(R.drawable.img_main_category_18);
        imgList.add(R.drawable.img_main_category_19);
        imgList.add(R.drawable.img_main_category_20);
        imgList.add(R.drawable.img_main_category_21);
        imgList.add(R.drawable.img_main_category_22);
        imgList.add(R.drawable.img_main_category_23);
        imgList.add(R.drawable.logo);
        return imgList;
    }
}

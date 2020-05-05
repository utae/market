package kr.co.t_woori.market.main;


import android.content.Intent;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;


import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;


import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.APICreator;
import kr.co.t_woori.market.communication.ServerCommunicator;
import kr.co.t_woori.market.databinding.HomeGridHeaderBinding;
import kr.co.t_woori.market.databinding.MainActivityBinding;
import kr.co.t_woori.market.databinding.TabItemBinding;
import kr.co.t_woori.market.drawer.HelpFragment;
import kr.co.t_woori.market.drawer.MarketInfoFragment;
import kr.co.t_woori.market.goods.Goods;
import kr.co.t_woori.market.goods.GoodsDetailFragment;
import kr.co.t_woori.market.goods.GoodsGridFragment;
import kr.co.t_woori.market.goods.GoodsListFragment;
import kr.co.t_woori.market.home.Category;
import kr.co.t_woori.market.notice.Notice;
import kr.co.t_woori.market.notice.NoticeDetailFragment;
import kr.co.t_woori.market.notice.NoticeListFragment;
import kr.co.t_woori.market.order.OrderHistoryListFragment;
import kr.co.t_woori.market.search.SearchActivity;
import kr.co.t_woori.market.utilities.UserInfo;
import kr.co.t_woori.market.utilities.Utilities;

public class MainActivity extends AppCompatActivity{

    private MainActivityBinding binding;
    private HomeGridHeaderBinding homeGridHeaderBinding;

    private final int REQUEST_CODE_NOTICE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        FirebaseMessaging.getInstance().subscribeToTopic("notice");
        FirebaseMessaging.getInstance().subscribeToTopic("flier");

        setSupportActionBar(binding.appBarMain.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.setDrawerListener(toggle);

        toggle.syncState();

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        addTab(binding.appBarMain.tabLayout, "전체상품", R.drawable.ic_home);
//        addTab(binding.appBarMain.tabLayout, "이전주문", R.drawable.ic_last_order);
        addTab(binding.appBarMain.tabLayout, "인기상품", R.drawable.ic_popular);
        addTab(binding.appBarMain.tabLayout, "한정특가", R.drawable.ic_bargain);
        addTab(binding.appBarMain.tabLayout, "전단지", R.drawable.ic_sale);
        addTab(binding.appBarMain.tabLayout, "공지사항", R.drawable.ic_notice);

        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), binding.appBarMain.tabLayout.getTabCount());
        binding.appBarMain.viewPager.setAdapter(mainViewPagerAdapter);
        binding.appBarMain.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.appBarMain.tabLayout));

        binding.appBarMain.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Utilities.popAllofBackStack(getSupportFragmentManager());
                binding.appBarMain.viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()){
                    case 0 :
                        binding.appBarMain.toolbarTitle.setImageResource(R.drawable.img_main_title);
                        break;
//                    case 1 :
//                        binding.appBarMain.toolbarTitle.setImageResource(R.drawable.img_last_order_title);
//                        break;
                    case 1 :
                        binding.appBarMain.toolbarTitle.setImageResource(R.drawable.img_popular_title);
                        break;
                    case 2 :
                        binding.appBarMain.toolbarTitle.setImageResource(R.drawable.img_bargain_title);
                        break;
                    case 3 :
                        binding.appBarMain.toolbarTitle.setImageResource(R.drawable.img_sale_title);
                        break;
                    case 4 :
                        binding.appBarMain.toolbarTitle.setImageResource(R.drawable.img_notice_title);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Utilities.popAllofBackStack(getSupportFragmentManager());
                if(binding.appBarMain.viewPager.getAdapter() instanceof MainViewPagerAdapter){
                    ((MainViewPagerAdapter)binding.appBarMain.viewPager.getAdapter()).refreshFragment(tab.getPosition());
                }
            }
        });

        initDrawerCategory();

        if("notice".equals(getIntent().getStringExtra("tag"))){
            binding.appBarMain.viewPager.setCurrentItem(5);
            getSupportFragmentManager().beginTransaction().add(R.id.main_container, NoticeDetailFragment.createById(getIntent().getStringExtra("id"))).addToBackStack(null).commit();
        }else if("flier".equals(getIntent().getStringExtra("tag"))){
            Goods goods = (Goods)getIntent().getSerializableExtra("goods");
            if(goods != null){
                getSupportFragmentManager().beginTransaction().add(R.id.main_container, GoodsDetailFragment.create(goods)).addToBackStack(null).commit();
            }
        }

        String curDate = Utilities.convertTimeMillisToTimeFormat(System.currentTimeMillis());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if(!curDate.equals(prefs.getString("popup", null))){
            PopupDialog.create(curDate).show(getSupportFragmentManager(), "popup");
        }
    }

    private void initDrawerCategory(){
        binding.mainDrawer.drawerEvent.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.drawerSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.appBarMain.viewPager.setCurrentItem(4, true);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        binding.mainDrawer.category1.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category2.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category3.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category4.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category5.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category6.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category7.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category8.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category9.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category10.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category11.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category12.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category13.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category14.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category15.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category16.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category17.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category18.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category19.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category20.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category21.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category22.setOnClickListener(new OnMainDrawerCategoryClickListener());
        binding.mainDrawer.category23.setOnClickListener(new OnMainDrawerCategoryClickListener());

        initDrawerMenu();
    }

    private void initDrawerMenu(){
        binding.mainDrawer.drawerMarketInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().add(R.id.main_container, new MarketInfoFragment()).addToBackStack(null).commit();
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        binding.mainDrawer.drawerNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().add(R.id.main_container, new NoticeListFragment()).addToBackStack(null).commit();
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        binding.mainDrawer.drawerOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().add(R.id.main_container, new OrderHistoryListFragment()).addToBackStack(null).commit();
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        binding.mainDrawer.drawerHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().add(R.id.main_container, new HelpFragment()).addToBackStack(null).commit();
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void addTab(TabLayout tabLayout, String title, int icResource){
        TabItemBinding tabItemBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.tab_item, null, false);
        tabItemBinding.imageView.setImageResource(icResource);
        tabItemBinding.titleTextView.setText(title);
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabItemBinding.getRoot()));
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        switch (id){
            case R.id.action_cart :
                getSupportFragmentManager().beginTransaction().add(R.id.main_container, GoodsGridFragment.createCartFragment()).addToBackStack(null).commit();
                break;
            case R.id.action_search :
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;

        }
        return true;
    }

    public ViewPager getMainViewPager(){
        return binding.appBarMain.viewPager;
    }

    private class OnMainDrawerCategoryClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            switch (view.getId()){
                case R.id.drawer_event :
                    transaction.add(R.id.main_container, GoodsListFragment.createEventList());
                    break;

                case R.id.category1 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(0)));
                    break;

                case R.id.category2 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(1)));
                    break;

                case R.id.category3 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(2)));
                    break;

                case R.id.category4 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(3)));
                    break;

                case R.id.category5 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(4)));
                    break;

                case R.id.category6 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(5)));
                    break;

                case R.id.category7 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(6)));
                    break;

                case R.id.category8 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(7)));
                    break;

                case R.id.category9 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(8)));
                    break;

                case R.id.category10 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(9)));
                    break;

                case R.id.category11 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(10)));
                    break;

                case R.id.category12 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(11)));
                    break;

                case R.id.category13 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(12)));
                    break;

                case R.id.category14 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(13)));
                    break;

                case R.id.category15 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(14)));
                    break;

                case R.id.category16 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(15)));
                    break;

                case R.id.category17 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(16)));
                    break;

                case R.id.category18 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(17)));
                    break;

                case R.id.category19 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(18)));
                    break;

                case R.id.category20 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(19)));
                    break;

                case R.id.category21 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(20)));
                    break;

                case R.id.category22 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(21)));
                    break;

                case R.id.category23 :
                    transaction.add(R.id.main_container, GoodsListFragment.createFromCategory(Category.getCategoryList(22)));
                    break;
            }
            transaction.addToBackStack(null).commit();
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}

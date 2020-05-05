package kr.co.t_woori.market.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import kr.co.t_woori.market.goods.GoodsGridFragment;
import kr.co.t_woori.market.goods.GoodsListFragment;
import kr.co.t_woori.market.home.HomePageFragment;
import kr.co.t_woori.market.notice.NoticeListFragment;
import kr.co.t_woori.market.sale.FlierListFragment;
import kr.co.t_woori.market.utilities.RefreshFragment;

/**
 * Created by rladn on 2017-10-23.
 */

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;
    private SparseArray<Fragment> fragmentSparseArray;

    public MainViewPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
        this.fragmentSparseArray = new SparseArray<>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position){
            case 0 :
                fragment = new HomePageFragment();
                break;
//            case 1 :
//                fragment = GoodsGridFragment.createLastOrderFragment();
//                break;
            case 1 :
                fragment = GoodsListFragment.createPopularList();
                break;
            case 2 :
                fragment = GoodsListFragment.createBargainList();
                break;
            case 3 :
                fragment = new FlierListFragment();
                break;
            case 4 :
                fragment = new NoticeListFragment();
                break;
            default:
                fragment = null;
                break;
        }

        fragmentSparseArray.append(position, fragment);

        return fragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public void refreshFragment(int position){
        if(fragmentSparseArray.get(position) instanceof RefreshFragment){
            ((RefreshFragment)fragmentSparseArray.get(position)).refreshFragment();
        }
    }

    public Fragment getFragmentByPosition(int position){
        if(fragmentSparseArray.get(position) != null){
            return fragmentSparseArray.get(position);
        }
        return null;
    }
}

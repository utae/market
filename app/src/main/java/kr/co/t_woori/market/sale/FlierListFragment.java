package kr.co.t_woori.market.sale;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.market.R;
import kr.co.t_woori.market.communication.APICreator;
import kr.co.t_woori.market.communication.ServerCommunicator;
import kr.co.t_woori.market.databinding.FlierListFragmentBinding;
import kr.co.t_woori.market.main.MainActivity;

/**
 * Created by rladn on 2017-11-24.
 */

public class FlierListFragment extends Fragment {

    private FlierListFragmentBinding binding;
    private ArrayList<Flier> flierList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.flier_list_fragment, container, false);

        flierList = new ArrayList<>();

        binding.emptyView.emptyTextView.setText("등록된 전단지가 없습니다.");

        getFlier();

        return binding.getRoot();
    }

    private void initListView(){
        FlierListAdapter adapter = new FlierListAdapter(getContext(), flierList);
        binding.listView.setAdapter(adapter);

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getAdapter().getItem(position) instanceof Flier){
                    Flier flier = (Flier) parent.getAdapter().getItem(position);
                    FlierFragment.create(flier).show(getFragmentManager(), "Flier");
                }
            }
        });
    }

    private void getFlier(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(FlierAPIService.class).getFlierList()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(!flierList.isEmpty()){
                    flierList.clear();
                }
                if("0".equals(results.get("count"))){
                    binding.listView.setEmptyView(binding.emptyView.emptyTextView);
                }else{
                    if(results.get("flier") instanceof List){
                        for(Object object : (List)results.get("flier")){
                            if(object instanceof Map){
                                Map map = (Map)object;
                                flierList.add(new Flier((String)map.get("id"), (String)map.get("title"), (String)map.get("time"), (String)map.get("content")));
                            }
                        }
                    }
                    initListView();
                }
            }
        };
        serverCommunicator.execute();
    }
}

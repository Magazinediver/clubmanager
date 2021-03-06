package clubmanage.ui.club;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import clubmanage.model.Club;
import clubmanage.ui.R;
import clubmanage.util.BaseException;
import clubmanage.util.ClubManageUtil;

public class Club_TabFragment1 extends Fragment {
    private List<Club> clubList = new ArrayList<>();
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            List<Club> clubs=(List<Club>) msg.obj;
            clubList.clear();
            clubList.addAll(clubs);
            adapter.notifyDataSetChanged();
        }
    };
    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ClubAdapter adapter;
    private String mTitle;

    //这个构造方法是便于各导航同时调用一个fragment
    public Club_TabFragment1(String title){
        mTitle=title;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.club_frament_tab1,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout=(SwipeRefreshLayout)getActivity().findViewById(R.id.club_refresh1);
        swipeRefreshLayout.setColorSchemeResources(R.color.titlecolorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                initActs();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        initActs();
        recyclerView = (RecyclerView)getActivity().findViewById(R.id.act_body1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ClubAdapter(clubList);
        recyclerView.setAdapter(adapter);
    }

    private void initActs(){
        new Thread(){
            @Override
            public void run() {
                List<Club> clubList=new ArrayList<Club>();
                try {
                    clubList = ClubManageUtil.clubManage.searchAllClub(false);
                } catch (BaseException e) {
                    e.printStackTrace();
                }
                Message message=new Message();
                message.obj=clubList;
                handler.sendMessage(message);
//                Log.i("Club_TabFragment1","***************"+mTitle+"  1");
            }
        }.start();
    }

}

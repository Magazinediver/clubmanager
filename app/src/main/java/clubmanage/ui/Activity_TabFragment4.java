package clubmanage.ui;

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

import clubmanage.model.Activity;
import clubmanage.task.ActivityTask;
import clubmanage.task.DBCallBack;
import clubmanage.ui.home.Home;
import clubmanage.ui.home.HomeAdapter;
import clubmanage.util.ClubManageUtil;

public class Activity_TabFragment4 extends Fragment {
    private List<Activity> homeList = new ArrayList<>();
    private String mTitle;
    private HomeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            List<Activity> activityList=(List<Activity>) msg.obj;
            homeList.clear();
            homeList.addAll(activityList);
            adapter.notifyDataSetChanged();
        }
    };

    //这个构造方法是便于各导航同时调用一个fragment
    public Activity_TabFragment4(String title){
        mTitle=title;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.activity_frament_tab4,container,false);
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout=(SwipeRefreshLayout)getActivity().findViewById(R.id.activity_refresh4);
        swipeRefreshLayout.setColorSchemeResources(R.color.titlecolorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                initHomes();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        initHomes();
        RecyclerView recyclerView = (RecyclerView)getActivity().findViewById(R.id.activity_body4);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HomeAdapter(homeList);
        recyclerView.setAdapter(adapter);

    }

    private void initHomes(){
//        for(int i=0;i<10;i++){
//            Home one = new Home(R.drawable.enrollment,"招新游园会","2019年11月24日9:00-11:00","南二食堂门口","火热报名中");
//            homeList.add(one);
//            Home two = new Home(R.drawable.drama_club,"话剧社招新","2019年11月24日9:00-11:00","南操场","火热报名中");
//            homeList.add(two);
//        }
        new Thread(){
            @Override
            public void run() {
                List<Activity> activityList= ClubManageUtil.activityManage.searchActivityByCategory(mTitle);
                Message message=new Message();
                message.obj=activityList;
                handler.sendMessage(message);
            }
        }.start();
    }
}

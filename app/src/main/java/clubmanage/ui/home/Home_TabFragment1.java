package clubmanage.ui.home;

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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import clubmanage.model.Activity;
import clubmanage.model.User;
import clubmanage.task.ActivityTask;
import clubmanage.task.DBCallBack;
import clubmanage.ui.R;
import clubmanage.util.ClubManageUtil;

public class Home_TabFragment1 extends Fragment {
    private List<Activity> homeList = new ArrayList<>();
    private String mTitle;
    private HomeAdapter adapter;
    private RecyclerView recyclerView;
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
    public Home_TabFragment1(String title){
        this.mTitle=title;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.home_frament_tab1,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout=(SwipeRefreshLayout)getActivity().findViewById(R.id.home_refresh1);
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
        recyclerView = (RecyclerView)getActivity().findViewById(R.id.home_body1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HomeAdapter(homeList);
        recyclerView.setAdapter(adapter);
    }

    private void initHomes(){
//        for(int i=0;i<10;i++){
//            Activity one = new Activity();
//            one.setActivity_name("招新游园会");
//            one.setActivity_place("南二食堂门口");
//            one.setActivity_start_time(Timestamp.valueOf("2019-11-24 00:00:00"));
//            one.setActivity_end_time(Timestamp.valueOf("2019-11-24 00:00:00"));
//            homeList.add(one);
//        }
        new Thread(){
            @Override
            public void run() {
                List<Activity> activityList=new ArrayList<>();
                activityList.addAll(ClubManageUtil.activityManage.searchMyActivity(User.currentLoginUser.getUid()));
                Message message=new Message();
                message.obj=activityList;
                handler.sendMessage(message);
            }
        }.start();
    }
}

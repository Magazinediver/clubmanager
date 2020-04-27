package clubmanage.ui.manage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import clubmanage.model.Club;
import clubmanage.model.User;
import clubmanage.ui.CheckResult;
import clubmanage.ui.CreateClub;
import clubmanage.ui.R;
import clubmanage.ui.SpaceItemDecoration;
import clubmanage.util.ClubManageUtil;

import static android.app.Activity.RESULT_OK;

public class Manage_Fragement extends Fragment implements View.OnClickListener {
    private List<Manage> manageList = new ArrayList<>();
    private RelativeLayout create;
    private Button result;
    private Club club;
    private ManageAdapter adapter;
    private RecyclerView recyclerView;
    private boolean haveCLubAppli;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            club=(Club) msg.obj;
            if(User.currentLoginUser.getUser_category().equals("学生")){
                if (club==null||club.getClub_name()==null){
                    manageList.clear();
                    result.setVisibility(View.VISIBLE);
                }else {
                    manageList.clear();
                    Manage three = new Manage(R.mipmap.name, R.drawable.application2, R.drawable.editactivity, R.mipmap.set,"我管理的社团","社员管理","申请活动","活动编辑","社团管理");
                    manageList.add(three);
                    result.setVisibility(View.VISIBLE);
                    create.setVisibility(View.INVISIBLE);
                }
            }
            adapter.notifyDataSetChanged();
        }
    };
    private Handler handler2=new Handler(){
        public void handleMessage(Message msg){
            haveCLubAppli=(boolean)msg.obj;
            if (haveCLubAppli==true){
                create.setVisibility(View.INVISIBLE);
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.manage,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        recyclerView = (RecyclerView)getActivity().findViewById(R.id.manage_body);
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ManageAdapter(manageList);
        recyclerView.setAdapter(adapter);
        create =getActivity().findViewById(R.id.manage_btn1);
        create.setOnClickListener(this);
        result=(Button)getActivity().findViewById(R.id.manage_head_btn1);
        result.setOnClickListener(this);
        result.setVisibility(View.INVISIBLE);
        create.setVisibility(View.VISIBLE);
        initManages();
        ifHaveClubAppli();
        searchClub();
    }

    private void initManages(){
        if(User.currentLoginUser.getUser_category().equals("社团管理员")){
            manageList.clear();
            Manage one = new Manage(R.drawable.applicatin1, R.drawable.disimiss,0,0,"社团审核","申请社团","解散社团",null,null);
            manageList.add(one);
            Manage two = new Manage(R.drawable.application2, R.drawable.disimiss,0,0,"活动审核","申请活动","取消活动",null,null);
            manageList.add(two);
            create.setVisibility(View.INVISIBLE);
        }
    }

    private void ifHaveClubAppli(){
        new Thread(){
            @Override
            public void run() {
                boolean haveClubAppli= ClubManageUtil.applicationManage.ifHaveClubAppli(User.currentLoginUser.getUid());
                Message message=new Message();
                message.obj=haveClubAppli;
                handler2.sendMessage(message);
            }
        }.start();
    }

    private void searchClub(){
        new Thread(){
            @Override
            public void run() {
                Club club= ClubManageUtil.clubManage.searchClubByProprieter(User.currentLoginUser.getUid());
                Message message=new Message();
                message.obj=club;
                handler.sendMessage(message);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.manage_btn1:
                Intent intent1=new Intent(getContext(), CreateClub.class);
                startActivityForResult(intent1,10);
                break;
            case R.id.manage_head_btn1:
                Intent intent2=new Intent(getContext(), CheckResult.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden){
        super.onHiddenChanged(hidden);
        if (hidden) {
            return;
        }else{
            initManages();
            ifHaveClubAppli();
            searchClub();
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if(resultCode==RESULT_OK){
                    create.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }
}

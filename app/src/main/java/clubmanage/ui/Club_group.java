package clubmanage.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import clubmanage.model.Activity;
import clubmanage.model.Club;
import clubmanage.model.User;
import clubmanage.ui.home.HomeAdapter;
import clubmanage.util.BaseException;
import clubmanage.util.ClubManageUtil;

public class Club_group extends AppCompatActivity implements View.OnClickListener {
    private List<User> topList = new ArrayList<>();
    private List<Activity> actList = new ArrayList<>();
    private Club clubMsg;
    private RecyclerView recyclerView2;
    private TopAdapter adapter2;
    private RecyclerView recyclerView3;
    private HomeAdapter adapter3= new HomeAdapter(actList);
    private String notice;

    private RelativeLayout join;
    private RelativeLayout gonggao;
    private RelativeLayout rel_join;
    private TextView gonggao_text;

    private Button btatt;
    private ImageButton member;
    private boolean atted=false;
    private boolean joined=false;
    private boolean iscap=false;

    private Handler handler1=new Handler(){
        public void handleMessage(Message msg){
            atted=(boolean)msg.obj;
            if (atted==false){
                btatt.setBackgroundResource(R.drawable.button_shape_clubbutton);
            }else {
                btatt.setBackgroundResource(R.drawable.button_shape_clubbutton2);
            }
        }
    };
    private Handler handler2=new Handler(){
        public void handleMessage(Message msg){
            List<User> users=(List<User>) msg.obj;
            topList.clear();
            topList.addAll(users);
            adapter2.notifyDataSetChanged();
        }
    };
    private Handler handler3=new Handler(){
        public void handleMessage(Message msg){
            List<Activity> activitys=(List<Activity>) msg.obj;
            actList.clear();
            actList.addAll(activitys);
            if (joined!=true){
                if (actList.isEmpty()==false){
                    for (int i=0;i<actList.size();i++){
                        if (actList.get(i).getIf_public_activity()==0){
                            actList.remove(i);
                            i--;
                        }
                    }
                }
            }
            adapter3.notifyDataSetChanged();
        }
    };
    private Handler handler4=new Handler(){
        public void handleMessage(Message msg){
            notice=(String) msg.obj;
            if (notice==null) gonggao_text.setText("暂无公告");
            else gonggao_text.setText(notice);
        }
    };
    private Handler handler5=new Handler(){
        public void handleMessage(Message msg){
            joined=(boolean)msg.obj;
            initActs();
            if (joined==true) {
                rel_join.setVisibility(View.GONE);
                gonggao.setVisibility(View.VISIBLE);
            }else {
                rel_join.setVisibility(View.VISIBLE);
                gonggao.setVisibility(View.GONE);
            }
        }
    };
    private Handler handler6=new Handler(){
        public void handleMessage(Message msg){
            iscap=(boolean)msg.obj;
            Intent intent=new Intent(Club_group.this,ClubMemberManage.class);
            intent.putExtra("club",clubMsg);
            intent.putExtra("iscap",iscap);
            startActivity(intent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_group);
        Intent intentget=getIntent();
        clubMsg=(Club)intentget.getSerializableExtra("clubMsg");
        initGonggao();
        new Thread(){
            @Override
            public void run() {
                boolean ifatt =ClubManageUtil.attentionManage.issubscribe(User.currentLoginUser.getUid(),clubMsg.getClub_id());
                Message message=new Message();
                message.obj=ifatt;
                handler1.sendMessage(message);
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                boolean ifinclub =ClubManageUtil.clubManage.if_userInClub(User.currentLoginUser.getUid(),clubMsg.getClub_id());
                Message message=new Message();
                message.obj=ifinclub;
                handler5.sendMessage(message);
            }
        }.start();


        TextView name=findViewById(R.id.club_item_text1);
        name.setText(clubMsg.getClub_name());
        TextView memberNum=findViewById(R.id.club_item_text3);
        memberNum.setText("成员"+clubMsg.getMember_number());
        TextView introduce=findViewById(R.id.club_item_text8);
        introduce.setText(clubMsg.getClub_introduce());
        TextView slogan=findViewById(R.id.club1_body1_text1);
        if (clubMsg.getSlogan()==null) slogan.setText("");
        else slogan.setText(clubMsg.getSlogan());

        btatt=findViewById(R.id.club1_head_btn2);
        btatt.setOnClickListener(this);
        ImageButton back=(ImageButton)findViewById(R.id.club1_head_btn1);
        back.setOnClickListener(this);
        member=(ImageButton)findViewById(R.id.club1_body2_btn1);
        member.setOnClickListener(this);
        join=(RelativeLayout)findViewById(R.id.club_join);
        join.setOnClickListener(this);
        gonggao=(RelativeLayout)findViewById(R.id.club1_body4);
        gonggao.setVisibility(View.GONE);
        rel_join=(RelativeLayout)findViewById(R.id.rel_join);
        rel_join.setVisibility(View.GONE);
        gonggao_text=(TextView)findViewById(R.id.gonggao_text);

        initTops();
        recyclerView2= (RecyclerView)findViewById(R.id.club1_recycler2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(layoutManager2);
        adapter2 = new TopAdapter(topList);
        recyclerView2.setAdapter(adapter2);

//        initActs();
        recyclerView3= (RecyclerView)findViewById(R.id.club1_recycler3);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        recyclerView3.setLayoutManager(layoutManager3);
        adapter3 = new HomeAdapter(actList);
        recyclerView3.setAdapter(adapter3);
    }

    private void initTops(){
        new Thread(){
            @Override
            public void run() {
                List<User> userList= null;
                try {
                    userList = ClubManageUtil.clubManage.searchMember(clubMsg.getClub_id());
                } catch (BaseException e) {
                    e.printStackTrace();
                }
                Message message=new Message();
                message.obj=userList;
                handler2.sendMessage(message);
            }
        }.start();
    }

    private void initGonggao(){
        new Thread(){
            @Override
            public void run() {
                String notice=ClubManageUtil.clubManage.searchNotice(clubMsg.getClub_id());
                Message message=new Message();
                message.obj=notice;
                handler4.sendMessage(message);
            }
        }.start();
    }

    private void initActs(){
        new Thread(){
            @Override
            public void run() {
                List<Activity> activityList=ClubManageUtil.activityManage.searchActivityByClubId(clubMsg.getClub_id());
                Message message=new Message();
                message.obj=activityList;
                handler3.sendMessage(message);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.club1_head_btn2:
                if (atted==false){
                    new Thread(){
                        @Override
                        public void run() {
                            ClubManageUtil.attentionManage.addAttention(User.currentLoginUser.getUid(),clubMsg.getClub_id());
                        }
                    }.start();
                    Toast.makeText(this, "关注成功", Toast.LENGTH_SHORT).show();
                    btatt.setBackgroundResource(R.drawable.button_shape_clubbutton2);
                    atted=true;
                }else {
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                ClubManageUtil.attentionManage.deleteAttention(User.currentLoginUser.getUid(),clubMsg.getClub_id());
                            } catch (BaseException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    Toast.makeText(this, "取消成功", Toast.LENGTH_SHORT).show();
                    btatt.setBackgroundResource(R.drawable.button_shape_clubbutton);
                    atted=false;
                }
                break;
            case R.id.club1_head_btn1:
                finish();
                break;
            case R.id.club1_body2_btn1:
                if (joined==false){
                    Toast.makeText(Club_group.this, "你不是本社成员，无法查看" , Toast.LENGTH_SHORT).show();
                    break;
                }
                new Thread(){
                    @Override
                    public void run() {
                        boolean ifcap =ClubManageUtil.clubManage.if_userIsCaptain(User.currentLoginUser.getUid(),clubMsg.getClub_id());
                        Message message=new Message();
                        message.obj=ifcap;
                        handler6.sendMessage(message);
                    }
                }.start();
                break;
            case R.id.club_join:
                new AlertDialog.Builder(this)
                        .setTitle("确定加入本社吗？")
                        .setIcon(android.R.drawable.ic_menu_edit)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                new Thread(){
                                    @Override
                                    public void run() {
                                        ClubManageUtil.clubManage.joinClub(User.currentLoginUser.getUid(),clubMsg.getClub_id());
                                    }
                                }.start();
                                joined=true;
                                rel_join.setVisibility(View.GONE);
                                gonggao.setVisibility(View.VISIBLE);
                                Toast.makeText(Club_group.this, "加入成功！" , Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }
    }
}

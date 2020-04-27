package clubmanage.ui;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import clubmanage.model.Club;
import clubmanage.model.Create_club;
import clubmanage.model.User;
import clubmanage.util.BaseException;
import clubmanage.util.ClubManageUtil;

public class CreateClub extends AppCompatActivity implements View.OnClickListener {
    private List<CreateClubMsg> createClubMsgList=new ArrayList<>();
    private String exception=null;
    private Button button;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            exception=(String) msg.obj;
            if(exception!=null){
                button.setEnabled(true);
                Toast.makeText(CreateClub.this, exception, Toast.LENGTH_SHORT).show();
                return;
            }else{
                Create_club.createClub=null;
                Toast.makeText(CreateClub.this, "创建成功，请等待审批", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.putExtra("data","OK");
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);
        Create_club.createClub=new Create_club();
        Create_club.createClub.setApplican_name(User.currentLoginUser.getName());
        Create_club.createClub.setUid(User.currentLoginUser.getUid());
        Toolbar toolbar = findViewById(R.id.create_club_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCreateClub();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_createclub);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CreateClubAdapter adapter = new CreateClubAdapter(createClubMsgList,CreateClub.this,true);
        recyclerView.setAdapter(adapter);
        button=findViewById(R.id.button_create_club);
        button.setOnClickListener(this);
    }
    private void initCreateClub(){
        CreateClubMsg name=new CreateClubMsg("社团名","ps.飞行社");
        createClubMsgList.add(name);
        CreateClubMsg sort=new CreateClubMsg("社团分类","ps.兴趣爱好");
        createClubMsgList.add(sort);
        CreateClubMsg place=new CreateClubMsg("社团场地","选择社团场地");
        createClubMsgList.add(place);
        CreateClubMsg slogan=new CreateClubMsg("社团简介","介绍你的社团");
        createClubMsgList.add(slogan);
        CreateClubMsg note=new CreateClubMsg("创建理由","理由");
        createClubMsgList.add(note);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_create_club:
                if (Create_club.createClub!=null){
                    Create_club c=Create_club.createClub;
                    if (c.getArea_name()==null||c.getClub_category()==null||c.getClub_name()==null||
                            c.getClub_name().equals("")==true||c.getIntroduce()==null||c.getIntroduce().equals("")==true
                            ||c.getReason()==null||c.getReason().equals("")==true){
                        Toast.makeText(this, "请完善所有信息", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    button.setEnabled(false);
                    createClub(Create_club.createClub);
                }
                break;
        }
    }

    private void createClub(final Create_club create){
        new Thread(){
            @Override
            public void run() {
                if(create!=null){
                    try {
                        ClubManageUtil.applicationManage.addClubAppli(create.getArea_name(),create.getUid(),
                                create.getApplican_name(),create.getClub_name(),create.getClub_category(),
                                create.getIntroduce(),create.getReason());
                        Message message=new Message();
                        message.obj=null;
                        handler.sendMessage(message);
                    } catch (BaseException e) {
                        Message message=new Message();
                        message.obj=e.getMessage();
                        handler.sendMessage(message);
                    }
                }
            }
        }.start();
    }
}
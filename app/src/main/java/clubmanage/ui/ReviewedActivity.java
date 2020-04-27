package clubmanage.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import clubmanage.model.Create_activity;
import clubmanage.model.User;
import clubmanage.util.ClubManageUtil;

public class ReviewedActivity extends AppCompatActivity implements View.OnClickListener {
    private List<CreateActivityMsg> createActivityMsgList=new ArrayList<>();
    private Create_activity activity;
    private EditText suggest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewed);
        Intent intentget=getIntent();
        activity=(Create_activity)intentget.getSerializableExtra("create_activity");
        CircleImageView poster=(CircleImageView)findViewById(R.id.activity_creat_poster);
        byte[] bt=activity.getPoster();
        if(bt!=null){
            poster.setImageBitmap(BitmapFactory.decodeByteArray(bt, 0, bt.length));
        }else {
            poster.setImageResource(R.drawable.enrollment);
        }
        Toolbar toolbar = findViewById(R.id.audit_activity_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initAuditActivity();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_auditActivity);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CreateActivityAdapter adapter = new CreateActivityAdapter(createActivityMsgList,ReviewedActivity.this,false);
        recyclerView.setAdapter(adapter);
        suggest=(EditText)findViewById(R.id.edit_text_audit);
        Button yes=(Button)findViewById(R.id.button_pass_audit);
        yes.setOnClickListener(this);
        Button no=(Button)findViewById(R.id.button_return_audit);
        no.setOnClickListener(this);
    }

    private void initAuditActivity(){
        CreateActivityMsg name=new CreateActivityMsg("活动名",activity.getActivity_name());
        createActivityMsgList.add(name);
        CreateActivityMsg sort=new CreateActivityMsg("分类",activity.getActivity_category());
        createActivityMsgList.add(sort);
        CreateActivityMsg arrangement=new CreateActivityMsg("活动介绍",activity.getActivity_details());
        createActivityMsgList.add(arrangement);
        CreateActivityMsg attention=new CreateActivityMsg("注意事项",activity.getActivity_attention());
        createActivityMsgList.add(attention);
        CreateActivityMsg place=new CreateActivityMsg("场地选择",activity.getArea_name());
        createActivityMsgList.add(place);
        CreateActivityMsg stime=new CreateActivityMsg("活动开始时间",activity.getActivity_start_time().toString());
        createActivityMsgList.add(stime);
        CreateActivityMsg etime=new CreateActivityMsg("活动开始时间",activity.getActivity_end_time().toString());
        createActivityMsgList.add(etime);
        CreateActivityMsg note=new CreateActivityMsg("申请理由",activity.getReason());
        createActivityMsgList.add(note);
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
            case R.id.button_return_audit:
                new Thread(){
                    @Override
                    public void run() {
                        ClubManageUtil.applicationManage.feedbackActivityAppli(activity.getActivity_approval_id(),0,suggest.getText().toString(), User.currentLoginUser.getUid());
                    }
                }.start();
                Toast.makeText(this,"审核完成",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.button_pass_audit:
                new Thread(){
                    @Override
                    public void run() {
                        ClubManageUtil.applicationManage.feedbackActivityAppli(activity.getActivity_approval_id(),1,suggest.getText().toString(), User.currentLoginUser.getUid());
                    }
                }.start();
                Toast.makeText(this,"审核完成",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}

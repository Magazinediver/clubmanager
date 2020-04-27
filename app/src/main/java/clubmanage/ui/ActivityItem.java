package clubmanage.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.sql.Timestamp;

import clubmanage.model.Activity;
import clubmanage.model.User;
import clubmanage.util.ClubManageUtil;

public class ActivityItem extends AppCompatActivity implements View.OnClickListener {
    private Activity activity;
    private boolean isSignUp=false;
    private Button join;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            isSignUp=(boolean)msg.obj;
            if (isSignUp==true){
                join.setEnabled(false);
                join.setText("已报名");
                join.setBackgroundResource(R.drawable.button_shape_gray);
            }else {
                join.setEnabled(true);
                join.setText("我要报名");
                join.setBackgroundResource(R.drawable.button_shape);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Intent intentget=getIntent();
        activity=(Activity) intentget.getSerializableExtra("activity");
        ImageView poster=(ImageView)findViewById(R.id.activity_poster);
        join=findViewById(R.id.activity_sign_up);
        join.setOnClickListener(this);
        if (activity.getPoster()!=null){
            byte[] bt= activity.getPoster();
            poster.setImageBitmap(BitmapFactory.decodeByteArray(bt, 0, bt.length));
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(activity.getActivity_end_time().before(now)){
            join.setEnabled(false);
            join.setText("活动已结束");
            join.setBackgroundResource(R.drawable.button_shape_gray);
        }
        else {
            join.setEnabled(true);
            join.setText("我要报名");
            join.setBackgroundResource(R.drawable.button_shape);
            new Thread(){
                @Override
                public void run() {
                    boolean isSignUp=ClubManageUtil.activityManage.if_participate(User.currentLoginUser.getUid(),activity.getActivity_id());
                    Message message=new Message();
                    message.obj=isSignUp;
                    handler.sendMessage(message);
                }
            }.start();
        }
        TextView name=findViewById(R.id.manage_item_text1);
        name.setText(activity.getActivity_name());
        TextView address=findViewById(R.id.act_address);
        address.setText(activity.getActivity_place());
        TextView time=findViewById(R.id.act_time);
        time.setText(activity.getActivity_start_time().toString());
        TextView time2=findViewById(R.id.act_time2);
        time2.setText(activity.getActivity_end_time().toString());
        TextView intruduction=findViewById(R.id.act_intruduction_context);
        intruduction.setText(activity.getActivity_introduce());
        TextView notice=findViewById(R.id.act_intruduction);
        notice.setText(activity.getActivity_attention());

        ImageButton back=findViewById(R.id.act_head_img1);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_head_img1:
                finish();
                break;
            case R.id.activity_sign_up:
                join.setEnabled(false);
                signUpAvtivity(User.currentLoginUser.getUid());
                Toast.makeText(ActivityItem.this,"报名成功",Toast.LENGTH_SHORT).show();
                join.setText("已报名");
                join.setBackgroundResource(R.drawable.button_shape_gray);
                break;
        }
    }
    private void signUpAvtivity(String uid){
        new Thread(){
            @Override
            public void run() {
               ClubManageUtil.applicationManage.signupActivity(User.currentLoginUser.getUid(),activity.getActivity_id());
            }
        }.start();
    }
}

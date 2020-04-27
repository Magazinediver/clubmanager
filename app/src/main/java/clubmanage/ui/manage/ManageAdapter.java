package clubmanage.ui.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import clubmanage.model.Club;
import clubmanage.model.User;
import clubmanage.ui.CheckActivity;
import clubmanage.ui.CheckClub;
import clubmanage.ui.ClubMemberManage;
import clubmanage.ui.CreateActivity;
import clubmanage.ui.ManageClub;
import clubmanage.ui.R;
import clubmanage.util.BaseException;
import clubmanage.util.ClubManageUtil;

public class ManageAdapter extends RecyclerView.Adapter<ManageAdapter.ViewHolder> {
    private Context mContext;
    private List<Manage> mManageList;
    private Club club;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            club=(Club) msg.obj;
        }
    };
    static class ViewHolder extends RecyclerView.ViewHolder{
        View manageView;
        ImageView manageImg1;
        ImageView manageImg2;
        ImageView manageImg3;
        ImageView manageImg4;
        TextView manageText1;
        TextView manageText2;
        TextView manageText3;
        TextView manageText4;
        TextView manageText5;


        public ViewHolder(View view){
            super(view);
            manageView=view;
            manageImg1 = (ImageView) view.findViewById((R.id.manage_item_img1));
            manageImg2 = (ImageView) view.findViewById((R.id.manage_item_img2));
            manageImg3 = (ImageView) view.findViewById((R.id.manage_item_img3));
            manageImg4 = (ImageView) view.findViewById((R.id.manage_item_img4));
            manageText1 = (TextView) view.findViewById((R.id.manage_item_text1));
            manageText2 = (TextView) view.findViewById((R.id.manage_item_text2));
            manageText3 = (TextView) view.findViewById((R.id.manage_item_text3));
            manageText4 = (TextView) view.findViewById((R.id.manage_item_text4));
            manageText5 = (TextView) view.findViewById((R.id.manage_item_text5));
        }
    }

    public ManageAdapter(List<Manage> ManageList){
        mManageList = ManageList;
    }

    @Override
    public ManageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        final View view = LayoutInflater.from(mContext).inflate(R.layout.manage_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        getClub();

        holder.manageImg1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                if(position<0) return;
                Manage manage=mManageList.get(position);
                TextView textView=view.findViewById(R.id.manage_item_text2);
                switch (textView.getText().toString()){
                    case "申请社团":
                        Intent intent1=new Intent(mContext, CheckClub.class);
                        mContext.startActivity(intent1);
                        break;
                    case "申请活动":
                        Intent intent2=new Intent(mContext, CheckActivity.class);
                        mContext.startActivity(intent2);
                        break;
                    case "社员管理":
                        Intent intent3=new Intent(mContext, ClubMemberManage.class);
                        intent3.putExtra("club",club);
                        intent3.putExtra("iscap",true);
                        mContext.startActivity(intent3);
                        break;
                }
            }
        });
        holder.manageImg2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                if(position<0) return;
                Manage manage=mManageList.get(position);
                TextView textView=view.findViewById(R.id.manage_item_text3);
                switch (textView.getText().toString()){
                    case "解散社团":break;
                    case "取消活动":break;
                    case "申请活动":
                        Intent intent=new Intent(mContext, CreateActivity.class);
                        mContext.startActivity(intent);
                        break;
                }
            }
        });
        holder.manageImg3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                if(position<0) return;
                Manage manage=mManageList.get(position);
                TextView textView=view.findViewById(R.id.manage_item_text4);
                switch (textView.getText().toString()){
                    case "活动编辑":break;
                }
            }
        });
        holder.manageImg4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                if(position<0) return;
                Manage manage=mManageList.get(position);
                TextView textView=view.findViewById(R.id.manage_item_text5);
                switch (textView.getText().toString()){
                    case "社团管理":
                        Intent intent=new Intent(mContext, ManageClub.class);
                        intent.putExtra("club",club);
                        mContext.startActivity(intent);
                        break;
                }
            }
        });
        return holder;
    }

    private void getClub(){
        new Thread(){
            @Override
            public void run() {
                Club club= ClubManageUtil.clubManage.searchClubByProprieter(User.currentLoginUser.getUid());
                Message message=new Message();
                message.obj=club;
                handler.handleMessage(message);
            }
        }.start();
    }
    @Override
    public void onBindViewHolder(ManageAdapter.ViewHolder holder, int position){
        Manage manage = mManageList.get(position);
        holder.manageImg1.setImageResource(manage.getImg1());
        holder.manageImg2.setImageResource(manage.getImg2());
        holder.manageImg3.setImageResource(manage.getImg3());
        holder.manageImg4.setImageResource(manage.getImg4());
        holder.manageText1.setText(manage.getText1());
        holder.manageText2.setText(manage.getText2());
        holder.manageText3.setText(manage.getText3());
        holder.manageText4.setText(manage.getText4());
        holder.manageText5.setText(manage.getText5());
    }

    @Override
    public int getItemCount(){
        return mManageList.size();
    }
}

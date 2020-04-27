package clubmanage.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import clubmanage.model.Activity;
import clubmanage.model.Club;
import clubmanage.model.User;
import clubmanage.ui.My_attention;
import clubmanage.ui.PersonalCenterActivity;
import clubmanage.ui.R;
import clubmanage.util.ClubManageUtil;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context mContext;
    private List<My> mMyList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View myView;
        ImageView myImg1;
        ImageView myImg2;
        TextView myText1;
        TextView myText2;

        public ViewHolder(View view){
            super(view);
            myView=view;
            myImg1 = (ImageView) view.findViewById((R.id.my_item_img1));
            myImg2 = (ImageView) view.findViewById((R.id.my_item_img2));
            myText1 = (TextView) view.findViewById((R.id.my_item_text1));
            myText2 = (TextView) view.findViewById((R.id.my_item_text2));
        }
    }

    public MyAdapter(List<My> MyList){
        mMyList = MyList;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_item,parent,false);
        final MyAdapter.ViewHolder holder = new MyAdapter.ViewHolder(view);
        holder.myView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                My my=mMyList.get(position);
                switch(my.getText1()){
                    case "我的社团": break;
                    case "我参与的活动": break;
                    case "我的关注":
                        Intent intent1 = new Intent(mContext, My_attention.class);
                        mContext.startActivity(intent1);
                        break;
                    case "我的品论": break;
                    case "个人中心":
                        Intent intent2 = new Intent(mContext, PersonalCenterActivity.class);
                        mContext.startActivity(intent2);
                        break;
                    default:break;
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position){
        My my = mMyList.get(position);
        holder.myImg1.setImageResource(my.getImg1());
        holder.myImg2.setImageResource(my.getImg2());
        holder.myText1.setText(my.getText1());
        holder.myText2.setText(my.getText2());
    }

    @Override
    public int getItemCount(){
        return mMyList.size();
    }
}

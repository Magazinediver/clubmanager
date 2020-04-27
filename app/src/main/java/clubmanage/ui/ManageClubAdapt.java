package clubmanage.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import clubmanage.model.Club;
import clubmanage.util.ClubManageUtil;


public class ManageClubAdapt extends RecyclerView.Adapter<ManageClubAdapt.ViewHolder>{
    private List<CreateClubMsg> createClubMsgList;
    private Context mcontext;
    private Club mclub;


    public ManageClubAdapt(List<CreateClubMsg> clublist,Club club){
        createClubMsgList=clublist;
        mclub=club;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        if (mcontext==null) mcontext=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_create_club_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.ClubView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                CreateClubMsg message = createClubMsgList.get(position);
                switch (message.getKey()){
                    case "社团分类":
                        final String[] items = {"兴趣爱好","学术竞赛", "体育运动"};
                        new AlertDialog.Builder(mcontext)
                                .setTitle("请选择类别")
                                .setItems(items,new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, final int which) {
                                        holder.Value.setText(items[which]);
                                        new Thread(){
                                            @Override
                                            public void run() {
                                                ClubManageUtil.clubManage.editCategory(mclub.getClub_id(),items[which]);
                                            }
                                        }.start();
                                        Toast.makeText(mcontext,"修改成功",Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                        break;
                    case "宣传语":
                        final EditText edt1 = new EditText(parent.getContext());
                        edt1.setMinLines(1);
                        edt1.setMaxLines(3);
                        new AlertDialog.Builder(mcontext)
                                .setTitle("新的宣传语")
                                .setIcon(android.R.drawable.ic_menu_edit)
                                .setView(edt1)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        holder.Value.setText(edt1.getText().toString());
                                        new Thread(){
                                            @Override
                                            public void run() {
                                                ClubManageUtil.clubManage.editSlogan(mclub.getClub_id(),edt1.getText().toString());
                                            }
                                        }.start();
                                        Toast.makeText(mcontext,"修改成功",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                        break;
                    case "社团简介":
                        final EditText edt2 = new EditText(parent.getContext());
                        edt2.setMinLines(1);
                        edt2.setMaxLines(15);
                        new AlertDialog.Builder(mcontext)
                                .setTitle("新的简介")
                                .setIcon(android.R.drawable.ic_menu_edit)
                                .setView(edt2)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        holder.Value.setText(edt2.getText().toString());
                                        new Thread(){
                                            @Override
                                            public void run() {
                                                ClubManageUtil.clubManage.editIntroduction(mclub.getClub_id(),edt2.getText().toString());
                                            }
                                        }.start();
                                        Toast.makeText(mcontext,"修改成功",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                        break;
                    case "发布公告":
                        final EditText edt3 = new EditText(parent.getContext());
                        edt3.setMinLines(1);
                        edt3.setMaxLines(10);
                        new AlertDialog.Builder(mcontext)
                                .setTitle("新的公告")
                                .setIcon(android.R.drawable.ic_menu_edit)
                                .setView(edt3)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        holder.Value.setText(edt3.getText().toString());
                                        new Thread(){
                                            @Override
                                            public void run() {
                                                ClubManageUtil.clubManage.editNotice(mclub.getClub_id(),edt3.getText().toString());
                                            }
                                        }.start();
                                        Toast.makeText(mcontext,"修改成功",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                        break;
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CreateClubMsg CreateClub = createClubMsgList.get(position);
        holder.Key.setText(CreateClub.getKey());
        holder.Value.setText(CreateClub.getValue());
        holder.Image.setImageResource(R.drawable.arrow_right_grey);
    }

    @Override
    public int getItemCount() {
        return createClubMsgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View ClubView;
        TextView Key;
        TextView Value;
        ImageView Image;

        public ViewHolder(View view) {
            super(view);
            ClubView = view;
            Key = (TextView) view.findViewById(R.id.club_key);
            Value = (TextView) view.findViewById(R.id.club_value);
            Image = (ImageView) view.findViewById(R.id.club_img);
        }
    }
}

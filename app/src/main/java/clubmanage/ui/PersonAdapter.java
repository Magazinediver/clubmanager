package clubmanage.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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

import clubmanage.model.User;
import clubmanage.util.BaseException;
import clubmanage.util.ClubManageUtil;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder>{
    private List<PersonMsg> personMsgList;
    private PersonalCenterActivity mContext;
//    private Handler handler=new Handler(){
//        public void handleMessage(Message msg){
//            String exception=(String)msg.obj;
//            if(exception!=null){
//                Toast.makeText(mContext, exception , Toast.LENGTH_SHORT).show();
//                return;
//            }else {
//                Toast.makeText(mContext, "修改成功" , Toast.LENGTH_SHORT).show();
//                holder.personValue.setText(message);
//            }
//        }
//    };
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.personView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                PersonMsg person = personMsgList.get(position);
                switch (person.getKey()){
                    case "姓名":
                        final EditText edt1 = new EditText(mContext);
                        edt1.setMinLines(1);
                        edt1.setMaxLines(1);
                        new AlertDialog.Builder(mContext)
                                .setTitle("修改姓名")
                                .setIcon(android.R.drawable.ic_menu_edit)
                                .setView(edt1)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        final String meg=edt1.getText().toString();
                                        if(meg==null||meg.equals("")) {
                                            Toast.makeText(mContext, "内容不能为空" , Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        new Thread(){
                                            @Override
                                            public void run() {
                                                try {
                                                    ClubManageUtil.personalManage.changeName(User.currentLoginUser.getUid(), meg);
                                                } catch (BaseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.start();
                                        holder.personValue.setText(meg);
                                        User.currentLoginUser.setName(meg);
                                        Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                        ;break;
                    case "性别":
                        final String[] items = {"男", "女"};
                        new AlertDialog.Builder(mContext)
                                .setTitle("请选择性别")
                                .setItems(items,new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final String msg=items[which];
                                        new Thread(){
                                            @Override
                                            public void run() {
                                                try {
                                                    ClubManageUtil.personalManage.changeGender(User.currentLoginUser.getUid(), msg);
                                                } catch (BaseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.start();
                                        holder.personValue.setText(msg);
                                        User.currentLoginUser.setGender(msg);
                                        Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                        break;
                    case "手机":
                        final EditText edt2 = new EditText(mContext);
                        edt2.setMinLines(1);
                        edt2.setMaxLines(1);
                        new AlertDialog.Builder(mContext)
                                .setTitle("修改手机")
                                .setIcon(android.R.drawable.ic_menu_edit)
                                .setView(edt2)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        final String meg=edt2.getText().toString();
                                        if(meg==null||meg.equals("")) {
                                            Toast.makeText(mContext, "内容不能为空" , Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        new Thread(){
                                            @Override
                                            public void run() {
                                                try {
                                                    ClubManageUtil.personalManage.changePhone_number(User.currentLoginUser.getUid(), meg);
                                                } catch (BaseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.start();
                                        holder.personValue.setText(meg);
                                        User.currentLoginUser.setPhone_number(meg);
                                        Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                        ;break;
                    case "邮箱":
                        final EditText edt3 = new EditText(mContext);
                        edt3.setMinLines(1);
                        edt3.setMaxLines(2);
                        new AlertDialog.Builder(mContext)
                                .setTitle("修改邮箱")
                                .setIcon(android.R.drawable.ic_menu_edit)
                                .setView(edt3)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        final String meg=edt3.getText().toString();
                                        if(meg==null||meg.equals("")) {
                                            Toast.makeText(mContext, "内容不能为空" , Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        new Thread(){
                                            @Override
                                            public void run() {
                                                try {
                                                    ClubManageUtil.personalManage.changeMail(User.currentLoginUser.getUid(), meg);
                                                } catch (BaseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.start();
                                        holder.personValue.setText(meg);
                                        User.currentLoginUser.setMail(meg);
                                        Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                        ;break;
                    case "专业":
                        final EditText edt4 = new EditText(mContext);
                        edt4.setMinLines(1);
                        edt4.setMaxLines(1);
                        new AlertDialog.Builder(mContext)
                                .setTitle("修改专业")
                                .setIcon(android.R.drawable.ic_menu_edit)
                                .setView(edt4)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        final String meg=edt4.getText().toString();
                                        if(meg==null||meg.equals("")) {
                                            Toast.makeText(mContext, "内容不能为空" , Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        new Thread(){
                                            @Override
                                            public void run() {
                                                try {
                                                    ClubManageUtil.personalManage.changeMajor(User.currentLoginUser.getUid(), meg);
                                                } catch (BaseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.start();
                                        holder.personValue.setText(meg);
                                        User.currentLoginUser.setMajor(meg);
                                        Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                        ;break;
                    default: break;
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PersonMsg person = personMsgList.get(position);
        holder.personKey.setText(person.getKey());
        holder.personValue.setText(person.getValue());
        holder.personImage.setImageResource(R.drawable.arrow_right_grey);
    }

    @Override
    public int getItemCount() {
        return personMsgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View personView;
        TextView personKey;
        TextView personValue;
        ImageView personImage;

        public ViewHolder(View view) {
            super(view);
            personView = view;
            personKey = (TextView) view.findViewById(R.id.person_key);
            personValue = (TextView) view.findViewById(R.id.person_value);
            personImage = (ImageView) view.findViewById(R.id.person_img);
        }
    }

    public PersonAdapter(PersonalCenterActivity context,List<PersonMsg> personList) {
        mContext = context;
        personMsgList = personList;
    }
}

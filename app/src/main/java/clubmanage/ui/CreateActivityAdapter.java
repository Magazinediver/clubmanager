package clubmanage.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import clubmanage.model.Area;
import clubmanage.model.Create_activity;
import clubmanage.ui.manage.Manage_Fragement;
import clubmanage.util.BaseException;
import clubmanage.util.ClubManageUtil;

public class CreateActivityAdapter extends RecyclerView.Adapter<CreateActivityAdapter.ViewHolder>{
    private List<CreateActivityMsg> createActivityMsgList;
    private AppCompatActivity mContext;
    private Calendar calendar= Calendar.getInstance(Locale.CHINA);
    private String[] place;
    private boolean canEdit;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            place=(String[])msg.obj;
        }
    };
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_create_item, parent, false);
        new Thread(){
            @Override
            public void run() {
                List<Area> area=null;
                try {
                    area=ClubManageUtil.areaManage.listUsibleSpe();
                } catch (BaseException e) {
                    e.printStackTrace();
                }
                String[] places=new String[area.size()];
                for(int i=0;i<area.size();i++){
                    places[i]=area.get(i).getArea_name();
                }
                Message message=new Message();
                message.obj=places;
                handler.sendMessage(message);
            }
        }.start();
        final ViewHolder holder = new ViewHolder(view);
        if (canEdit==true){
            holder.createActivityView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    CreateActivityMsg create_activity = createActivityMsgList.get(position);
                    switch (create_activity.getKey()){
                        case "活动名":
                            final EditText edt1 = new EditText(mContext);
                            edt1.setMinLines(1);
                            edt1.setMaxLines(1);
                            new AlertDialog.Builder(mContext)
                                    .setTitle("输入"+create_activity.getKey())
                                    .setIcon(android.R.drawable.ic_menu_edit)
                                    .setView(edt1)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Create_activity.createActivity.setActivity_name(edt1.getText().toString());
                                            holder.createActivityValue.setText(edt1.getText().toString());
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                            break;
                        case "分类":
                            final String[] items = {"学术创新", "公益服务", "体育运动"};
                            new AlertDialog.Builder(mContext)
                                    .setTitle("请选择类别")
                                    .setItems(items,new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            holder.createActivityValue.setText(items[which]);
                                            Create_activity.createActivity.setActivity_category(items[which]);
                                        }
                                    }).show();
                            break;
                        case "活动介绍":
                            final EditText edt2 = new EditText(mContext);
                            edt2.setMinLines(1);
                            edt2.setMaxLines(5);
                            new AlertDialog.Builder(mContext)
                                    .setTitle("输入活动介绍")
                                    .setIcon(android.R.drawable.ic_menu_edit)
                                    .setView(edt2)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Create_activity.createActivity.setActivity_details(edt2.getText().toString());
                                            holder.createActivityValue.setText(edt2.getText().toString());
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                            break;
                        case "活动注意事项":
                            final EditText edt4 = new EditText(mContext);
                            edt4.setMinLines(1);
                            edt4.setMaxLines(5);
                            new AlertDialog.Builder(mContext)
                                    .setTitle("输入活动注意事项")
                                    .setIcon(android.R.drawable.ic_menu_edit)
                                    .setView(edt4)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Create_activity.createActivity.setActivity_attention(edt4.getText().toString());
                                            holder.createActivityValue.setText(edt4.getText().toString());
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                            break;
                        case "是否公开":
                            final String[] items2 = {"是", "否"};
                            new AlertDialog.Builder(mContext)
                                    .setTitle("请选择是否公开")
                                    .setItems(items2,new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            holder.createActivityValue.setText(items2[which]);
                                            if (items2[which].equals("是"))
                                                Create_activity.createActivity.setIf_public_activity((byte)1);
                                            else Create_activity.createActivity.setIf_public_activity((byte)0);
                                        }
                                    }).show();
                            break;
                        case "场地选择":
                            new AlertDialog.Builder(mContext)
                                    .setTitle("请选择场地")
                                    .setItems(place,new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            holder.createActivityValue.setText(place[which]);
                                            Create_activity.createActivity.setArea_name(place[which]);
                                        }
                                    }).show();
                            break;
                        case "活动开始时间":
                            new DatePickerDialog(mContext, 4, new DatePickerDialog.OnDateSetListener() {
                                // 绑定监听器(How the parent is notified that the date is set.)
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    // 此处得到选择的时间，可以进行你想要的操作
                                    holder.createActivityValue.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                                    Create_activity.createActivity.setActivity_start_time(Timestamp.valueOf(year+"-"+(monthOfYear + 1)+"-"+dayOfMonth+" 00:00:00"));
                                }
                            }
                                    // 设置初始日期
                                    , calendar.get(Calendar.YEAR)
                                    , calendar.get(Calendar.MONTH)
                                    , calendar.get(Calendar.DAY_OF_MONTH)).show();
                            break;
                        case "活动结束时间":
                            new DatePickerDialog(mContext, 4, new DatePickerDialog.OnDateSetListener() {
                                // 绑定监听器(How the parent is notified that the date is set.)
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    // 此处得到选择的时间，可以进行你想要的操作
                                    holder.createActivityValue.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                                    Create_activity.createActivity.setActivity_end_time(Timestamp.valueOf(year+"-"+(monthOfYear + 1)+"-"+dayOfMonth+" 00:00:00"));
                                }
                            }
                                    // 设置初始日期
                                    , calendar.get(Calendar.YEAR)
                                    , calendar.get(Calendar.MONTH)
                                    , calendar.get(Calendar.DAY_OF_MONTH)).show();break;
                        case "申请理由":
                            final EditText edt3 = new EditText(mContext);
                            edt3.setMinLines(1);
                            edt3.setMaxLines(5);
                            new AlertDialog.Builder(mContext)
                                    .setTitle("输入申请理由")
                                    .setIcon(android.R.drawable.ic_menu_edit)
                                    .setView(edt3)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Create_activity.createActivity.setReason(edt3.getText().toString());
                                            holder.createActivityValue.setText(edt3.getText().toString());
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                            break;
                    }
                }
            });
        }
        return holder;
    }

    public static void showDatePickerDialog(Activity activity, int themeResId, final ViewHolder holder, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                holder.createActivityValue.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                Create_activity.createActivity.setActivity_start_time(Timestamp.valueOf(year+"-"+(monthOfYear + 1)+"-"+dayOfMonth+" 00:00:00"));
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CreateActivityMsg createActivity = createActivityMsgList.get(position);
        holder.createActivityKey.setText(createActivity.getKey());
        holder.createActivityValue.setText(createActivity.getValue());
        if (canEdit==true)
        holder.createActivityImage.setImageResource(R.drawable.arrow_right_grey);
    }

    @Override
    public int getItemCount() {
        return createActivityMsgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View createActivityView;
        TextView createActivityKey;
        TextView createActivityValue;
        ImageView createActivityImage;

        public ViewHolder(View view) {
            super(view);
            createActivityView = view;
            createActivityKey = (TextView) view.findViewById(R.id.activity_key);
            createActivityValue = (TextView) view.findViewById(R.id.activity_value);
            createActivityImage = (ImageView) view.findViewById(R.id.activity_img);
        }
    }

    public CreateActivityAdapter(List<CreateActivityMsg> fruitList, AppCompatActivity context,boolean canEdit) {
        createActivityMsgList = fruitList;
        this.canEdit=canEdit;
        mContext= context;
    }
}

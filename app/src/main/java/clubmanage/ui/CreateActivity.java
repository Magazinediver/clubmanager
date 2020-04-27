package clubmanage.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import clubmanage.model.Club;
import clubmanage.model.Create_activity;
import clubmanage.model.User;
import clubmanage.util.BaseException;
import clubmanage.util.ClubManageUtil;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
    private List<CreateActivityMsg> createActivityMsgList=new ArrayList<>();
    private Club club;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            club=(Club) msg.obj;
        }
    };
    private Handler handler2=new Handler(){
        public void handleMessage(Message msg){
            exception=(String) msg.obj;
            if (exception==null){
                Create_activity.createActivity=null;
                Toast.makeText(CreateActivity.this, "创建成功，请等待审批", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                button.setEnabled(true);
                Toast.makeText(CreateActivity.this, exception, Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };
    private String exception=null;
    private CircleImageView img;
    private Button button;

    public static final int CHOOSE_PHOTO = 2;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        verifyStoragePermissions(this);
        getClub();
        Create_activity.createActivity=new Create_activity();
        Create_activity.createActivity.setActivity_owner_name(User.currentLoginUser.getName());
        Create_activity.createActivity.setActivity_owner_id(User.currentLoginUser.getUid());
        Toolbar toolbar = findViewById(R.id.create_activity_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCreateActivity();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_createActivity);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CreateActivityAdapter adapter = new CreateActivityAdapter(createActivityMsgList,this,true);
        recyclerView.setAdapter(adapter);
        button=(Button)findViewById(R.id.button_create_activity);
        button.setOnClickListener(this);
        img=findViewById(R.id.create_activity_poster);
        img.setOnClickListener(this);
    }
    private void initCreateActivity(){
        CreateActivityMsg name=new CreateActivityMsg("活动名","");
        createActivityMsgList.add(name);
        CreateActivityMsg sort=new CreateActivityMsg("分类","");
        createActivityMsgList.add(sort);
        CreateActivityMsg arrangement=new CreateActivityMsg("活动介绍","");
        createActivityMsgList.add(arrangement);
        CreateActivityMsg atention=new CreateActivityMsg("活动注意事项","");
        createActivityMsgList.add(atention);
        CreateActivityMsg if_public=new CreateActivityMsg("是否公开","");
        createActivityMsgList.add(if_public);
        CreateActivityMsg place=new CreateActivityMsg("场地选择","");
        createActivityMsgList.add(place);
        CreateActivityMsg timestart=new CreateActivityMsg("活动开始时间","");
        createActivityMsgList.add(timestart);
        CreateActivityMsg timefinish=new CreateActivityMsg("活动结束时间","");
        createActivityMsgList.add(timefinish);
        CreateActivityMsg note=new CreateActivityMsg("申请理由","");
        createActivityMsgList.add(note);
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
            case R.id.create_activity_poster:
                if (ContextCompat.checkSelfPermission(CreateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
                } else {
                    openAlbum();
                }
                break;
            case R.id.button_create_activity:
                if(Create_activity.createActivity!=null){
                    Create_activity c=Create_activity.createActivity;
                    if(c.getActivity_category()==null||c.getActivity_details()==null||c.getActivity_details().equals("")==true
                    ||c.getActivity_end_time()==null||c.getActivity_start_time()==null||c.getActivity_name()==null||
                    c.getActivity_name().equals("")==true||c.getArea_name()==null||c.getReason()==null
                    ||c.getReason().equals("")==true){
                        Toast.makeText(this, "请完善所有信息", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    button.setEnabled(false);
                    createActivity(Create_activity.createActivity);
                }
                break;
        }
    }

    private void createActivity(final Create_activity create){
        new Thread(){
            @Override
            public void run() {
                if(create!=null){
                    boolean p=true;
                    if (create.getIf_public_activity()==1)p=true;
                    else if (create.getIf_public_activity()==0) p=false;
                    try {
                        ClubManageUtil.applicationManage.addActivityAppli(club.getClub_id(),create.getPoster(),
                                create.getActivity_name(),create.getArea_name(),User.currentLoginUser.getUid(),
                                User.currentLoginUser.getName(),create.getActivity_start_time().toString(),
                                create.getActivity_end_time().toString(), create.getActivity_details(),
                                create.getActivity_attention(),create.getActivity_category(),p,create.getReason());
                        Message message=new Message();
                        message.obj=null;
                        handler2.sendMessage(message);
                    } catch (BaseException e) {
                        Message message=new Message();
                        message.obj=e.getMessage();
                        handler2.sendMessage(message);
                    }
                }
            }
        }.start();
    }

    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Create_activity.createActivity.setPoster(byteArray);
            img.setImageBitmap(bitmap);
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
}

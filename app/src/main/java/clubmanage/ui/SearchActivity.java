package clubmanage.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import clubmanage.model.Activity;
import clubmanage.ui.home.HomeAdapter;
import clubmanage.util.ClubManageUtil;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Activity> SearchActivitylist = new ArrayList<>();
    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private EditText searchCon;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            List<Activity> activityList=(List<Activity>) msg.obj;
            SearchActivitylist.clear();
            SearchActivitylist.addAll(activityList);
            adapter.notifyDataSetChanged();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ImageView back=findViewById(R.id.img_array);
        back.setOnClickListener(this);
        Button search=findViewById(R.id.search_head_btn1);
        search.setOnClickListener(this);
        searchCon=(EditText)findViewById(R.id.home_head_edit1);

//        initSearchActivity();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_searchClub);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HomeAdapter(SearchActivitylist);
        recyclerView.setAdapter(adapter);
//        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_searchClub);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        HomeAdapter adapter = new HomeAdapter(SearchActivitylist);
//        recyclerView.setAdapter(adapter);
    }

    private void initSearchActivity() {
//        Home ho = new Home(R.drawable.enrollment, "招新游园会", "2019年11月24日9:00-11:00", "南二食堂门口", "火热报名中");
//        SearchActivitylist.add(ho);
        new Thread(){
            @Override
            public void run() {
                List<Activity> activityList=new ArrayList<>();
                activityList.addAll(ClubManageUtil.activityManage.searchActivityByName(searchCon.getText().toString()));
                Message message=new Message();
                message.obj=activityList;
                handler.sendMessage(message);
            }
        }.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_array:
                finish();
                break;
            case R.id.search_head_btn1:
                initSearchActivity();
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
package clubmanage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import clubmanage.model.Club;

public class ManageClub extends AppCompatActivity {
    private List<CreateClubMsg> createClubMsgList=new ArrayList<>();
    private Club club;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_club);
        Intent intentget=getIntent();
        club=(Club)intentget.getSerializableExtra("club");
        Toolbar toolbar = findViewById(R.id.change_club_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCreateClub();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_changeclub);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ManageClubAdapt adapter = new ManageClubAdapt(createClubMsgList,club);
        recyclerView.setAdapter(adapter);
    }
    private void initCreateClub(){
        CreateClubMsg name=new CreateClubMsg("社团名",club.getClub_name());
        createClubMsgList.add(name);
        CreateClubMsg sort=new CreateClubMsg("社团分类",club.getCategory_name());
        createClubMsgList.add(sort);
        CreateClubMsg arrangement=new CreateClubMsg("宣传语",club.getSlogan());
        createClubMsgList.add(arrangement);
        CreateClubMsg slogan=new CreateClubMsg("社团简介",club.getClub_introduce());
        createClubMsgList.add(slogan);
        CreateClubMsg note=new CreateClubMsg("发布公告","");
        createClubMsgList.add(note);
        CreateClubMsg place=new CreateClubMsg("社团场地",club.getClub_place());
        createClubMsgList.add(place);
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
}
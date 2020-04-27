package clubmanage.task;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import clubmanage.model.Activity;
import clubmanage.util.ClubManageUtil;

public class ActivityTask extends AsyncTask<Object,Object, List<Activity>> {
    private List<Activity> contentDatas;//数据
    private DBCallBack callBack;

    public ActivityTask(DBCallBack dbCallBack) {
        contentDatas=new ArrayList<Activity>();
        callBack=dbCallBack;
    }

    @Override
    protected List<Activity> doInBackground(Object... objects) {
        contentDatas=ClubManageUtil.activityManage.searchAllActivity();
        return contentDatas;
    }
    @Override
    protected void onPostExecute(List<Activity> contentDatas) {
        super.onPostExecute(contentDatas);
        this.callBack.success(contentDatas);
    }
}

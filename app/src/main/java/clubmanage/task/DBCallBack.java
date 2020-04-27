package clubmanage.task;

import java.util.List;

import clubmanage.model.Activity;

public interface DBCallBack {
    void success(List<Activity> result);
    void error(Exception e);
}

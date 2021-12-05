package base;

import android.app.Activity;
import android.app.Application;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import db.DBManager;
import db.DBMangerBudget;

public class UniteApp extends Application {

    private List<Activity> activities = new ArrayList<Activity>();

    public List<Activity> getActivities() {
        return activities;
    }
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
        DBManager.initDB(this);
        DBMangerBudget.initDB(getApplicationContext());
    }
}

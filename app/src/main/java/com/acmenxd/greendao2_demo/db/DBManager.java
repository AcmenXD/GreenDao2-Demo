package com.acmenxd.greendao2_demo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.acmenxd.greendao2_demo.db.dao.DaoMaster;
import com.acmenxd.greendao2_demo.db.dao.DaoSession;


/**
 * @author 小东
 * @version v1.0
 * @date 2016/11/21 15:44
 * @detail 数据库管理
 */
public class DBManager {
    private static final String TAG = DBManager.class.getSimpleName();
    private static final String DB_NAME = "demo_db"; //数据库名字
    private static DBManager instance;

    private DBManager() {
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    private boolean mInited = false; //是否初始化过
    private Context mContext = null;//上下文对象
    /**
     * 数据库操作
     */
    private DaoMaster.OpenHelper mOpenHelper = null;
    private SQLiteDatabase mDatabase = null;
    private DaoMaster mDaoMaster = null;
    private DaoSession mDaoSession = null;

    /**
     * 初始化数据库
     *
     * @param context
     */
    public void init(Context context) {
        if (!mInited || mContext == null) {
            this.mContext = context;
            mOpenHelper = new DBOpenHelper(mContext, DB_NAME, null);
            mDatabase = mOpenHelper.getWritableDatabase();
            mDaoMaster = new DaoMaster(mDatabase);
            mDaoSession = mDaoMaster.newSession();
        }
    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}

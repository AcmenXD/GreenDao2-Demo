package com.acmenxd.greendao2_demo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.acmenxd.greendao2_demo.db.dao.DaoMaster;
import com.acmenxd.greendao2_demo.db.migrator.BaseMigratorHelper;

/**
 * @author 小东
 * @version v1.0
 * @date 2016/11/21 15:44
 * @detail 数据库升级
 */
public class DBOpenHelper extends DaoMaster.OpenHelper {

    private static final String TAG = DBOpenHelper.class.getSimpleName();

    public DBOpenHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory) {
        super(context, dbName, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //DBManager.getInstance().onUpgrade(db);
        /**
         * * 数据库版本号不能降低,会导致App无法安装
         * 循环数据库版本,更新各版本数据结构差异
         */
        if (newVersion > oldVersion) {
            for (int i = oldVersion; i < newVersion; i++) {
                try {
                    BaseMigratorHelper migratorHelper = (BaseMigratorHelper) Class
                            .forName("com.acmenxd.greendao2_demo.db.migrator.MigratorHelper" + (i + 1)).newInstance();
                    if (migratorHelper != null) {
                        migratorHelper.onUpgrade(db);
                    }
                } catch (ClassNotFoundException | ClassCastException | IllegalAccessException | InstantiationException e) {
                    Log.e("Dong", TAG + ":" + e.getMessage(), e);
                }
            }
        }
    }
}

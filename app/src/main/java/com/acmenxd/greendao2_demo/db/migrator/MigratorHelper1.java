package com.acmenxd.greendao2_demo.db.migrator;

import android.database.sqlite.SQLiteDatabase;

import com.acmenxd.greendao2_demo.db.dao.StudentDao;

/**
 * @author 小东
 * @version v1.0
 * @date 2016/11/21 15:04
 * @detail something
 */
public class MigratorHelper1 extends BaseMigratorHelper {

    private static final String TAG = MigratorHelper1.class.getSimpleName();

    @Override
    public void onUpgrade(SQLiteDatabase db) {
        //更新数据库表结构
        MigrationHelperUtil.getInstance().migrate(db, StudentDao.class);

    }
}

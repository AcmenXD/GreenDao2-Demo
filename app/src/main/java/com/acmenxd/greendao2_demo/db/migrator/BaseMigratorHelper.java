package com.acmenxd.greendao2_demo.db.migrator;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author 小东
 * @version v1.0
 * @date 2016/11/21 15:02
 * @detail 更新数据库版本基类
 */

public abstract class BaseMigratorHelper {
    public abstract void onUpgrade(SQLiteDatabase db);
}

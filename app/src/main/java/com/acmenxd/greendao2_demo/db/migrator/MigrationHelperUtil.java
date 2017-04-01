package com.acmenxd.greendao2_demo.db.migrator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * @author 小东
 * @version v1.0
 * @date 2016/11/21 15:44
 * @detail 数据库更新
 */
public class MigrationHelperUtil {
    private static final String TAG = MigrationHelperUtil.class.getSimpleName();
    /**
     * 数据库中对应Java类型 -> String,不支持char类型
     */
    public static final String TYPE_TEXT = "TEXT";
    /**
     * 数据库中对应Java类型 -> Short,Integer,Long
     */
    public static final String TYPE_INTEGER = "INTEGER";
    /**
     * 数据库中对应Java类型 -> Float,Double
     */
    public static final String TYPE_REAL = "REAL";
    /**
     * 数据库中对应Java类型 -> Boolean 0(false) 1(true)
     */
    public static final String TYPE_BOOLEAN = "BOOLEAN";

    private static MigrationHelperUtil instance;

    private MigrationHelperUtil() {
    }

    public static MigrationHelperUtil getInstance() {
        if (instance == null) {
            instance = new MigrationHelperUtil();
        }
        return instance;
    }

    /**
     * 数据库更新
     *
     * @param db
     * @param daoClasses
     */
    public void migrate(SQLiteDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        //新建临时表
        generateTempTables(db, daoClasses);
        //删除表
        invokeMethod("dropTable", true, db, daoClasses);
        //新建表
        invokeMethod("createTable", false, db, daoClasses);
        //临时表数据写入新表，删除临时表
        restoreData(db, daoClasses);
    }

    /**
     * 新建临时表
     *
     * @param db
     * @param daoClasses
     */
    private void generateTempTables(SQLiteDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String divider = "";
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList<>();
            StringBuilder createTableStringBuilder = new StringBuilder();
            createTableStringBuilder.append("CREATE TABLE ").append(tempTableName).append(" (");
            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;
                if (getColumns(db, tableName).contains(columnName)) {
                    properties.add(columnName);
                    String type = null;
                    try {
                        type = getTypeByClass(daoConfig.properties[j].type);
                    } catch (Exception pE) {
                        Log.e("Dong", TAG + ":" + pE.getMessage(), pE);
                    }
                    createTableStringBuilder.append(divider).append(columnName).append(" ").append(type);
                    if (daoConfig.properties[j].primaryKey) {
                        createTableStringBuilder.append(" PRIMARY KEY");
                    }
                    divider = ",";
                }
            }
            createTableStringBuilder.append(");");
            db.execSQL(createTableStringBuilder.toString());
            StringBuilder insertTableStringBuilder = new StringBuilder();
            insertTableStringBuilder.append("INSERT INTO ").append(tempTableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(" FROM ").append(tableName).append(";");
            db.execSQL(insertTableStringBuilder.toString());
        }
    }

    /**
     * 删除表 & 新建表
     */
    private void invokeMethod(String methodName, boolean param, SQLiteDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        try {
            for (int i = 0; i < daoClasses.length; i++) {
                Method method = daoClasses[i].getDeclaredMethod(methodName, SQLiteDatabase.class, boolean.class);
                method.invoke(daoClasses[i], db, param);
            }
        } catch (NoSuchMethodException pE) {
            Log.e("Dong", TAG + ":" + pE);
        } catch (InvocationTargetException pE) {
            Log.e("Dong", TAG + ":" + pE);
        } catch (IllegalAccessException pE) {
            Log.e("Dong", TAG + ":" + pE);
        }
    }

    /**
     * 临时表数据写入新表，删除临时表
     *
     * @param db
     * @param daoClasses
     */
    private void restoreData(SQLiteDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList();
            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;
                if (getColumns(db, tempTableName).contains(columnName)) {
                    properties.add(columnName);
                }
            }
            StringBuilder insertTableStringBuilder = new StringBuilder();
            insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");
            StringBuilder dropTableStringBuilder = new StringBuilder();
            dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);
            db.execSQL(insertTableStringBuilder.toString());
            db.execSQL(dropTableStringBuilder.toString());
        }
    }

    private String getTypeByClass(Class<?> type) throws Exception {
        if (type.equals(String.class)) {
            return TYPE_TEXT;
        }
        if (type.equals(Long.class) || type.equals(Integer.class) || type.equals(Short.class) || type.equals(long.class) || type.equals(int.class) || type.equals(short.class)) {
            return TYPE_INTEGER;
        }
        if (type.equals(Float.class) || type.equals(Double.class) || type.equals(float.class) || type.equals(double.class)) {
            return TYPE_REAL;
        }
        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return TYPE_BOOLEAN;
        }
        Exception exception = new Exception("MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT PARAMETERS".concat(" - Class: ").concat(type.toString()));
        Log.e("Dong", TAG + ":" + exception.getMessage(), exception);
        throw exception;
    }

    private static List<String> getColumns(SQLiteDatabase db, String tableName) {
        List<String> columns = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 1", null);
            if (cursor != null) {
                columns = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
            }
        } catch (Exception pE) {
            Log.e("Dong", TAG + ":" + pE.getMessage(), pE);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return columns;
    }
}

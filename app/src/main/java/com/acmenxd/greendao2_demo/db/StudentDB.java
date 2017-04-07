package com.acmenxd.greendao2_demo.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acmenxd.greendao2_demo.db.bean.Student;
import com.acmenxd.greendao2_demo.db.dao.DaoSession;
import com.acmenxd.greendao2_demo.db.dao.StudentDao;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * @author 小东
 * @version v1.0
 * @date 2016/11/21 16:22
 * @detail something
 */
public class StudentDB {
    private static StudentDB instance;
    private SQLiteDatabase db;
    private DaoSession ds;
    private StudentDao mStudentDao;

    private StudentDB() {
        db = DBManager.getInstance().getDatabase();
        ds = DBManager.getInstance().getDaoSession();
        mStudentDao = ds.getStudentDao();
    }

    public static StudentDB getInstance() {
        if (instance == null) {
            instance = new StudentDB();
        }
        return instance;
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    public Cursor getStudentCursor() {
        //查询，得到cursor
        String orderBy = StudentDao.Properties.Id.columnName + " DESC";//根据Id降序排序
        Cursor cursor = db.query(mStudentDao.getTablename(), mStudentDao.getAllColumns(), null, null, null, null, orderBy);
        return cursor;
    }

    /**
     * 添加学生
     */
    public void addStudent(String name, int age, double score) {
        Student entity = new Student(null, name, age, score, new Date());
        //面向对象添加表数据
        mStudentDao.insert(entity);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    public void deleteStudent(long id) {
        mStudentDao.deleteByKey(id);
    }

    /**
     * 更新
     */
    public void updateStudent(Long id, String name, int age, double score) {
        Student entity = new Student(id, name, age, score, new Date());
        mStudentDao.update(entity);
    }

    /**
     * 根据name查询
     *
     * @param name
     */
    public List queryStudent(String name) {
        // Query 类代表了一个可以被重复执行的查询
        Query query = mStudentDao.queryBuilder()
                .where(StudentDao.Properties.Name.eq(name))
                .orderAsc(StudentDao.Properties.Id)
                .build();
        // 查询结果以 List 返回
        return query.list();
    }
}

package com.acmenxd.greendao2_demo;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.acmenxd.greendao2_demo.db.DBUtil;
import com.acmenxd.greendao2_demo.db.bean.Student;
import com.acmenxd.greendao2_demo.db.dao.StudentDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 小东
 * @version v1.0
 * @date 2016/11/21 16:56
 * @detail GreenDaoDemo
 */
public class MainActivity extends AppCompatActivity {
    private TextView tId;
    private EditText eName;
    private EditText eAge;
    private EditText eScore;
    private TextView tDate;
    private ListView lv;
    private MyAdapter mMyAdapter;
    private DBUtil mDBUtil;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        tId = (TextView) findViewById(R.id.id);
        eName = (EditText) findViewById(R.id.name);
        eAge = (EditText) findViewById(R.id.age);
        eScore = (EditText) findViewById(R.id.score);
        tDate = (TextView) findViewById(R.id.date);
        lv = (ListView) findViewById(R.id.lv);

        //读取数据
        mDBUtil = DBUtil.getInstance();
        mCursor = mDBUtil.getStudentCursor();
        //设置数据视图
        mMyAdapter = new MyAdapter(this, mCursor);
        lv.setAdapter(mMyAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
                Cursor pCursor = (Cursor) mMyAdapter.getItem(pI);
                tId.setText(pCursor.getLong(0) + "");
                eName.setText(pCursor.getString(1));
                eAge.setText(pCursor.getInt(2) + "");
                eScore.setText(pCursor.getDouble(3) + "");
                long d = pCursor.getLong(4);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                tDate.setText(sdf.format(new Date(d)));
            }
        });
    }

    /**
     * 添加一条数据
     *
     * @param view
     */
    public void AddClick(View view) {
        String name = eName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(MainActivity.this, "name不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        int age = 0;
        double score = 0;
        try {
            age = getAge();
            score = getScore();
        } catch (Exception pE) {
            return;
        }
        mDBUtil.addStudent(name, age, score);
        mCursor.requery();
    }

    /**
     * 删除一条数据 - 根据id删除
     *
     * @param view
     */
    public void deleteClick(View view) {
        long id = 0;
        try {
            id = getId();
        } catch (Exception pE) {
            return;
        }
        mDBUtil.deleteStudent(id);
        mCursor.requery();
    }

    /**
     * 更新一条数据
     *
     * @param view
     */
    public void updateClick(View view) {
        String name = getName();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(MainActivity.this, "name不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        long id = 0;
        int age = 0;
        double score = 0;
        try {
            id = getId();
            age = getAge();
            score = getScore();
        } catch (Exception pE) {
            return;
        }
        mDBUtil.updateStudent(id, name, age, score);
        mCursor.requery();
    }

    /**
     * 根据 name查询
     *
     * @param view
     */
    public void queryClick(View view) {
        String name = getName();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(MainActivity.this, "name不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Student> data = mDBUtil.queryStudent(name);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        StringBuilder sb = new StringBuilder("查到 " + data.size() + " 条数据\n");
        for (int i = 0, c = data.size(); i < c; i++) {
            Student s = data.get(i);
            sb.append(" id:");
            sb.append(s.getId());
            sb.append(" name:");
            sb.append(s.getName());
            sb.append(" age:");
            sb.append(s.getAge());
            sb.append(" score:");
            sb.append(s.getScore());
            sb.append(" date:");
            sb.append(sdf.format(s.getDate()));
            if (i < c - 1) {
                sb.append("\n");
            }
        }
        Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
    }

    public long getId() {
        String id = tId.getText().toString().trim();
        if (TextUtils.isEmpty(id)) {
            return 0;
        }
        long idLong = 0;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException pE) {
            Toast.makeText(this, "id必须是long类型", Toast.LENGTH_SHORT).show();
            throw pE;
        }

        return idLong;
    }

    public String getName() {
        String name = eName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        return name;
    }

    public int getAge() {
        String age = eAge.getText().toString().trim();
        if (TextUtils.isEmpty(age)) {
            return 0;
        }
        int ageInt = 0;
        try {
            ageInt = Integer.parseInt(age);
        } catch (NumberFormatException pE) {
            Toast.makeText(this, "年龄必须是int类型", Toast.LENGTH_SHORT).show();
            throw pE;
        }
        return ageInt;
    }

    public double getScore() {
        String score = eScore.getText().toString().trim();
        if (TextUtils.isEmpty(score)) {
            return 0;
        }
        double scoreDouble = 0;
        try {
            scoreDouble = Double.parseDouble(score);
        } catch (NumberFormatException pE) {
            Toast.makeText(this, "分数必须是Double类型", Toast.LENGTH_SHORT).show();
            throw pE;
        }
        return scoreDouble;
    }

    class MyAdapter extends CursorAdapter {
        public Context mContext;

        public MyAdapter(Context context, Cursor c) {
            super(context, c);
            mContext = context;
        }

        @Override
        public View newView(Context pContext, Cursor pCursor, ViewGroup pViewGroup) {
            ViewHolder holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item, null, false);
            holder.tv_id = (TextView) view.findViewById(R.id.id);
            holder.tv_name = (TextView) view.findViewById(R.id.name);
            holder.tv_age = (TextView) view.findViewById(R.id.age);
            holder.tv_score = (TextView) view.findViewById(R.id.score);
            holder.tv_date = (TextView) view.findViewById(R.id.date);
            view.setTag(holder);
            return view;
        }

        @Override
        public void bindView(View pView, Context pContext, Cursor pCursor) {
            ViewHolder holder = (ViewHolder) pView.getTag();
            long id = pCursor.getLong(pCursor.getColumnIndex(StudentDao.Properties.Id.columnName));
            String name = pCursor.getString(pCursor.getColumnIndex(StudentDao.Properties.Name.columnName));
            int age = pCursor.getInt(pCursor.getColumnIndex(StudentDao.Properties.Age.columnName));
            double score = pCursor.getDouble(pCursor.getColumnIndex(StudentDao.Properties.Score.columnName));
            long date = pCursor.getLong(pCursor.getColumnIndex(StudentDao.Properties.Date.columnName));
            holder.tv_id.setText(id + "");
            holder.tv_name.setText(name);
            holder.tv_age.setText(age + "");
            holder.tv_score.setText(score + "");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            holder.tv_date.setText(sdf.format(new Date(date)));
        }

        class ViewHolder {
            TextView tv_id;
            TextView tv_name;
            TextView tv_age;
            TextView tv_score;
            TextView tv_date;
        }
    }
}

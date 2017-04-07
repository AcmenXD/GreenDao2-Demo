package com.xd;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * @author 小东
 * @version v1.0
 * @date 2016/11/21 14:16
 * @detail GreenDaoMain
 */
public class GreenDaoMain {
    /**
     * 数据库版本号不能降低,会导致App无法安装
     * 数据库字段发生变更或新增表时,更新这个版本号,运行main
     * 版本号历史:
     * 1:2016年11月21日13:32:33 - > 创建初始库和表
     */
    private static final int VERSION = 1;
    //根据工程路径,指定一个相对或者绝对路径
    private static final String outDIR = "../GreenDao2-Demo/app/src/main/java/";
    //bean的存放路径 outDIR + defaultPackageBEAN
    private static final String defaultPackageBEAN = "com.acmenxd.greendao2_demo.db.bean";
    //dao的存放路径 outDIR + defaultPackageDAO
    private static final String defaultPackageDAO = "com.acmenxd.greendao2_demo.db.dao";

    public static void main(String[] args) throws Exception {
        //创建模式对象,指定版本号 及 生成的bean对象的包名
        Schema schema = new Schema(VERSION, defaultPackageBEAN);
        //指定生成的dao对象的包名,不指定则默认生成在 defaultPackageBEAN 包中
        schema.setDefaultJavaPackageDao(defaultPackageDAO);

        // 模式（Schema）同时也拥有两个默认的 flags，分别用来标示 entity 是否是 activie 以及是否使用 keep sections。
        //schema.enableActiveEntitiesByDefault();
        //schema.enableKeepSectionsByDefault();

        //添加实体 -> 对应数据库中的表
        addEntitys(schema);

        //自动生成代码到outDIR目录下
        new DaoGenerator().generateAll(schema, outDIR);
    }

    /**
     * 创建实体
     *
     * @param pSchema
     */
    private static void addEntitys(Schema pSchema) {
        addStudent(pSchema);
    }

    /**
     * Test -> student实体
     *
     * @param pSchema
     */
    public static void addStudent(Schema pSchema) {
        //添加一个实体，自动生成实体类
        Entity studentEntity = pSchema.addEntity("Student");
        //指定表名，如不指定，表名则为 Student（即实体类名）
        studentEntity.setTableName("tb_student");
        //添加Id,自增长
        studentEntity.addIdProperty().autoincrement();
        //添加String类型的name,不能为空
        studentEntity.addStringProperty("name").notNull();
        //添加int类型的age
        studentEntity.addIntProperty("age");
        //添加double类型的score
        studentEntity.addDoubleProperty("score");
        //添加Date类型的date
        studentEntity.addDateProperty("date");

        /**
         * Test
         */
        //studentEntity.addIntProperty("_id").primaryKey().notNull();
        //studentEntity.implementsInterface("android.os.Parcelable");

        /*
        Entity order = pSchema.addEntity("Order");
        order.setTableName("ORDERS");
        order.addIdProperty();
        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(studentEntity, customerId);
        ToMany customerToOrders = studentEntity.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
        */
    }
}

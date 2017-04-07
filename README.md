# GreenDao2-Demo
基于<a href="https://github.com/greenrobot/greenDAO">greenDAO2.x</a>版本,写的使用Demo,并拓展了一部分数据库升级功能.


如要了解功能实现,请运行app程序查看控制台日志和源代码!
* 源代码 : <a href="https://github.com/AcmenXD/GreenDao2-Demo">AcmenXD/GreenDao2-Demo</a>
* apk下载路径 : <a href="https://github.com/AcmenXD/Resource/blob/master/apks/GreenDao2-Demo.apk">GreenDao2-Demo.apk</a>
### 依赖
---
- AndroidStudio
```
    // 以下配置在app下的build.gradle配置即可
    android {
        sourceSets {
            main {
                java.srcDirs = ['src/main/java', 'src/main/java-gen']
            }
        }
    }
```
```
	compile 'de.greenrobot:greendao:2.1.0'
```
### 功能
---
- 支持greenDAO2.x所有功能,未改写其代码
- 支持新增表
- 支持表字段增加和删除
### 使用 -> 以下代码 注释很详细、很重要很重要很重要!!!
---
**第一步**
```java
    // 创建Module -> Java Library,并在build.gradle中做如下配置
    dependencies {
        compile 'de.greenrobot:greendao-generator:2.1.0'
    }
```
---
**第二步**

    - 新建的Module中新建GreenDaoMain.java, 并参考Demo中的<a href="https://github.com/AcmenXD/GreenDao2-Demo/blob/master/greendao/src/main/java/com/xd/GeneratorBase.java">GreenDaoMain.java</a>做相关配置(参考类中注释写的很详细)
    - 配置完成后,运行GreenDaoMain.java中的main函数,会自动生成部分代码和java文件(如运行失败,修复问题后,需再次运行)
---
**第三步**
```java
    创建StudentDB,为Student表添加 增删改查 函数
```
---
**第四步**
```java
    运行程序,会自动创建数据库表和字段
```
### 新增表 或 增删表字段
---
**第一步**
```java
    修改Module的GreenDaoMain.java文件,创建新的实体 或 修改实体类(Student)
    完成后,运行GreenDaoMain.java中的main函数,会自动更改相关代码(如运行失败,修复问题后,需再次运行)
```
---
**第二步**
```java
    创建 MigratorHelper+数据库版本号 的类文件(此文件为数据库升级时,表结构的修改类),并在DBOpenHelper中确认MigratorHelper类的包名是否正确(因为这里用的反射)
```
---
**第三步**
```java
    /**
     * 在MigratorHelper.onUpgrade函数中,更新数据库表结构
     */
    public void onUpgrade(Database db) {
        /**
         * migrate()参数解释
         * 参数一:数据库db实例
         * 参数二:需要更新或新建表的Dao.class类(有增删字段或新增的表必须在这里配置)
         */
        MigrationHelperUtil.getInstance().migrate(db, Student2Dao.class, Student3Dao.class);
    }
```
---
**第五步**
```java
    运行程序,会自动更新数据库表和字段
```
有问题请于作者联系AcmenXD@163.com ^_^!
---
### 打个小广告^_^
**gitHub** : https://github.com/AcmenXD   如对您有帮助,欢迎点Star支持,谢谢~

**技术博客** : http://blog.csdn.net/wxd_beijing
# END
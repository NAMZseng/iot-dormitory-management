-   [安卓端与后端功能模块交互说明](#安卓端与后端功能模块交互说明)
    -   [基本信息模块](#基本信息模块)
        -   [APP使用对象](#app使用对象)
        -   [登陆](#登陆)
        -   [默认密码修改](#默认密码修改)
        -   [手机号修改](#手机号修改)
        -   [密码找回（附加功能）](#密码找回附加功能)
    -   [学生信息模块](#学生信息模块)
        -   [基本信息展示](#基本信息展示)
        -   [信息更新（附加功能）](#信息更新附加功能)
    -   [人流管理模块](#人流管理模块)
        -   [每日出入刷卡图表显示](#每日出入刷卡图表显示)
        -   [异常出入信息显示](#异常出入信息显示)
        -   [学生未归寝提醒](#学生未归寝提醒)
    -   [环境监控模块](#环境监控模块)
        -   [每日温湿度图表显示](#每日温湿度图表显示)
        -   [温湿度异常信息提醒](#温湿度异常信息提醒)

安卓端与后端功能模块交互说明
============================

**注：该文档为功能及逻辑实现的初步说明，对于具体的接口及相关参数，将在后续沟通中一一说明。**

基本信息模块
------------

### APP使用对象

我们的宿舍管理系统的安卓端主要面向的使用者为**宿舍管理员**，即宿管阿姨/叔叔，不涉及学生登陆。

宿舍管理员通过登陆App，可以动态的了解所在的宿舍楼人流出入情况，楼层环境情况以及所住的学生基本信息等。

### 登陆

由于该App主要面向的使用者为宿舍管理员，即职工，所以不涉及注册功能，所有的职工信息有系统管理员在后台输入数据库。职工通过输入**手机号**以及**初始密码**（123456）即可登陆。

对于离职的职工，系统管理员会在后台删除该职工的信息，离职职工无法再登陆。

在登陆模块中，安卓端需获取职工输入的手机号（**tel**）以及密码(**password**)，发送http
get请求给服务端，服务端通过查询staff\_info表，判断手机号与密码是否匹配。若匹配，则**以Json格式**返回封装了该职工信息的**StaffInfo类对象**；若不匹配否则返回**null**；

> http
> get请求示例：http://49.232.57.160:8080/DormitoryManage/staff/login?tel=18406580009&password=123456

> Json格式的返回结果示例：
>
> ```json
> {
>   "num": 1,
>   "name": "刘阿姨",
>   "tel": "18406580009",
>   "buildingNum": 9,
>   "title": "宿舍楼总负责人",
>   "password": "123456"
> }
> ```
>
> 

StaffInfo类的定义如下：

``` java
public class StaffInfo {

    private int num;
    private String name;
    private String tel;
    private int buildingNum;
    private String title ;
    private String password;
    
    // getters and setters
 }
```

### 默认密码修改

职工登陆App后，可自己修改登陆密码。修改登陆密码时，需先一次输入原密码，完成安全校验后，再输入更新的密码（两次）。

在密码修改模块中，安卓端需先判断用户两次输入的更新密码是否一致，若不一致，弹出提示信息，要求用户重新输入。

但两次输入的更新密码一致后，将职工的**手机号**（tel），**原密码**(oldPassword)与**新密码**(newPassword)通过http
post请求发送给服务端，服务端通过查询staff\_info表,
判断原密码的是否正确。**若正确，修改成功返回1，否则返回-1**；

密码修改成功后，需用户重新登陆。

> http post示例：http://49.232.57.160:8080/DormitoryManage/staff/updatePassword?tel=18406580009&oldPassword=123456&newPassword=000000

### 手机号修改

手机号的修改逻辑类似密码的修改，安卓端需要获取用户的**原手机号**（oldTel），**登陆密码**(password)，**新手机号**（newTel）,发送http
post请求给服务端，服务端先判断新手机号在数据库中**是否已存在**，若**存在**，**返回0**，安卓端告知用户重新输入其他手机号；若**不存在**，当**原手机号与登陆密码**是否匹配。若**匹配，修改成功返回1，否则返回-1**；

手机号修改成功后，需用户重新登陆。

> http post示例：http://49.232.57.160:8080/DormitoryManage/staff/updateTel?oldTel=18406580009&password=123456&newTel=18406587474

### 密码找回（附加功能）

此功能可以暂时先不写。对于不写，可以解释为，不将修改密码的权限开放给职工，
职工通过向管理员提交申请，管理员将密码重新置为默认值。

对于写，即将找回密码的权限赋给职工，需要添加一个发送短信的api，通过手机号短信验证找回。该功能我个人在上学期的课设中已经实现，若后续有时间，可以将这块功能完善。

学生信息模块
------------

### 基本信息展示

在该模块，宿舍管理员可查询所在宿舍楼的学生信息。

当宿舍管理员点击该部分的按钮时，安卓端将登陆职工的所在的**宿舍楼号**（buildingNum），通过http
get请求传给服务端，服务端会根据宿舍楼号查询student\_info表，将该楼的所有学生信息，以一个**List\<StudentInfo\>，通过Json格式**，返回给返回给安卓端。安卓端可以以**瀑布流**的布局展示学生信息。

StudentInfo类的定义如下：

``` java
public class StudentInfo {

    private int num;
    private String name;
    private String school;
    private String major;
    private String teacherName;
    private String teacherTel;
    private int buildingNum;
    private int roomNum;
    
    // getters and setters
}
```

> http get请求示例：http://49.232.57.160:8080/DormitoryManage/student/getStudentsInfo?buildingNum=9
>
> Json格式的返回结果示例：
>
> ```json
> [
>   {
>     "num": 1607094215,
>     "name": "刘乙同学",
>     "school": "大数据学院",
>     "major": "物联网工程",
>     "teacherName": "孟老师",
>     "teacherTel": "18406587401",
>     "buildingNum": 9,
>     "roomNum": 201
>   },
>   {
>     "num": 1607094220,
>     "name": "杨乙同学",
>     "school": "大数据学院",
>     "major": "物联网工程",
>     "teacherName": "孟老师",
>     "teacherTel": "18406587401",
>     "buildingNum": 9,
>     "roomNum": 203
>   }
> ]
> ```
>
> 

### 信息更新（附加功能）

该模块可暂时先不写，即先不将修改学生信息的权限开放给职工。

人流管理模块
------------

### 每日出入刷卡图表显示

在该模块，宿舍管理员可以看到每日出入该宿舍楼的人流情况。

当宿舍管理员点击该部分的按钮时，安卓端将登陆职工的所在的**宿舍楼号**（buildingNum），通过http
get请求传给服务端，服务端通过查询访问记录access\_info表，将**该楼的该日**的人流情况，以**Json格式**返回一个封装了出入总人数的**Map\<String, Long\>**, 若当日**无人流出入，则返回null**。安卓端可以**柱状图**的形式展现出入总人数。

该模块的信息是动态变化的，为了节省资源消耗，初步设置为宿舍管理员每点击一次，系统就查询一次，更新信息。

>   http get 请求示例：http://49.232.57.160:8080/DormitoryManage/access/getTodayInOutSum?buildingNum=10
>
>   Json格式的返回结果示例：
>
>   ```json
>   {
>     "in": 5,
>     "out": 4
>   }
>   ```
>
>   

### 异常出入信息显示

在该模块，宿舍管理员可以看到非该宿舍楼的同学尝试访问的记录，实现逻辑同上。

当宿舍管理员点击该部分的按钮时，安卓端将登陆职工的所在的**宿舍楼号**（buildingNum），通过http
get请求传给服务端，服务端通过查询被阻访问记录block\_info表，将**该楼的该日**的异常访问情况以一个**列表List\<BlockInfo\>，通过Json格式**，返回给返回给安卓端。安卓端可以直接以**表格**的形式展现。

BlockInfo类定义如下：

``` java
public class BlockInfo {
    private String studentName;
    private int num;
    private String teacherTel;
    private int buildingNum;
    private Date accessTime;
   // setters and getters
}
```

>   http get 请求示例：http://49.232.57.160:8080/DormitoryManage/access/getTodayBlockInfo?buildingNum=9
>
>   Json格式的返回结果示例：
>
>   ```json
>   [
>     {
>       "studentName": "赵甲同学",
>       "num": 1607094202,
>       "teacherTel": "18406587401",
>       "buildingNum": 9,
>       "accessTime": 1567063343000
>     },
>     {
>       "studentName": "刘甲同学",
>       "num": 1607094201,
>       "teacherTel": "18406587401",
>       "buildingNum": 9,
>       "accessTime": 1567062887000
>     }
>   ]
>   ```



### 学生未归寝提醒

在该模块，宿舍管理员通过设置门禁时间（如周一至周五晚11点，周末晚11：30），当到达门禁时间时，安卓端**自动**将登陆职工的所在的**宿舍楼号**（buildingNum），通过http
get请求，发送给服务端。服务端通过查询access\_info表，将未归寝的学生名单以一个列表**List\<StudentInfo\>，通过Json格式**，返回给返回给安卓端。安卓端可以直接以**表格**的形式展现，供宿舍管理员查询。

>   http get请求示例：http://49.232.57.160:8080/DormitoryManage/access/getOutStudentInfo?buildingNum=10
>
>   Json格式的返回结果示例：
>
>   ```json
>   [
>     {
>       "num": 1607094202,
>       "teacherTel": "18406587401",
>       "buildingNum": 10,
>       "name": "赵甲同学",
>       "school": "大数据学院",
>       "major": "物联网工程",
>       "teacherName": "孟老师",
>       "roomNum": 101
>     }
>   ]
>   ```
>
>   

环境监控模块
------------

### 每日温湿度图表显示

在该模块，宿舍管理员可查询该宿舍楼的温湿度情况。

当宿舍管理员点击该部分的按钮时，安卓端将登陆职工的所在的**宿舍楼号**（buildingNum），通过http
get请求传给服务端，服务端通过查询温湿度数据humiture\_info表，将**该楼的该日**的温湿度以一个**List\<HumitureInfo\>，通过Json格式**，返回给返回给安卓端。安卓端可以**折线图**等形式展现。为方便测量，温湿度默认是**每分钟**采集一次，折线图的单位可按**每小时**进行模拟。

HumitureInfo类的定义如下：

``` java
public class HumitureInfo {
    private String macAddress;
    private int buildingNum;
    private String location;
    private Date collectTime;
    private float temperature;
    private float humidity;
     
    // getters and setters
}
```

该模块的信息是动态变化的，为了节省资源消耗，初步设置为宿舍管理员每点击一次，系统就查询一次，更新信息。

>   http get请求示例：http://49.232.57.160:8080/DormitoryManage/humiture/getData?buildingNum=9

>   Json格式的返回结果示例：
>
>   ```json
>   [
>     {
>       "buildingNum": 9,
>       "macAddress": "E0DE5305004B1200",
>       "location": "一楼东",
>       "collectTime": 1566985657000,
>       "temperature": 90.8798,
>       "humidity": 80.9891
>     },
>     {
>       "buildingNum": 9,
>       "macAddress": "9C625305004B1200",
>       "location": "二楼西",
>       "collectTime": 1566985629000,
>       "temperature": 40.8798,
>       "humidity": 60.9891
>     }
>   ]
>   ```
>
>   注意，时间的格式为long型，安卓端需使用SimpleDateFormat类进行转换。
>
>   ```java
>   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
>   long collectTime = 1566985657000;
>   Date date = sdf.parse(collectTime);
>   ```
>
>   

### 温湿度异常信息提醒

该功能有硬件端实现。硬件端在接收协调器的数据时，先检查数据是否异常，若异常，发送异常信号，如让灯亮。

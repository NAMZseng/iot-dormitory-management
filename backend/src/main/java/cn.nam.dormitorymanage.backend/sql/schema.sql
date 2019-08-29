CREATE DATABASE `dormitory` CHARACTER SET utf8 COLLATE utf8_general_ci;
use dormitory;

create table building_info(
  `num` tinyint comment '宿舍楼号',
  `name` char(30) comment '宿舍名称，最多10个汉字',
  `chief_name` char(15) comment '宿舍楼负责人名字',
  `chief_tel` char(11) comment '宿舍楼负责人电话',
  primary key(num)
) engine=innodb default charset=utf8 comment '宿舍楼信息表';

create table staff_info(
  `num` int comment '员工工号',
  `name` char(30) comment '员工姓名',
  `tel` char(11) unique comment '员工电话',
  `building_num` tinyint comment '工作所在的宿舍楼号',
  `title` char(30) comment '职称',
  `password` char(30) default '123456' comment 'app登陆密码',
  primary key(num)
)engine=innodb default charset=utf8 comment '宿舍楼职工信息表';

create table student_info(
  `num` int comment '学生学号',
  `name` char(30) comment '学生姓名',
  `school` char(45) comment '所在学院，最多15个汉字',
  `major` char(45) comment '专业名称',
  `teacher_name` char(30) comment '班主任',
  `teacher_tel` char(11) comment '班主任电话',
  `building_num` tinyint comment '所在宿舍楼号',
  `room_num` smallint comment '房间号',
  primary key(num)
) engine=innodb default charset=utf8 comment '学生信息表';

create table access_info(
  `building_num` tinyint comment '出入的宿舍楼号',
  `student_num` int comment '学生学号',
  `access_time` timestamp default current_timestamp comment '出入时间',
  `access_status` tinyint comment'出入状态，入:1 出-1',
  primary key(building_num, student_num, access_time)
) engine=innodb default charset=utf8 comment '学生出入宿舍楼记录表';

create table block_info(
  `building_num` tinyint comment '出入的宿舍楼号',
  `student_num` int comment '学生学号',
  `access_time`  timestamp default current_timestamp comment '出入时间',
  primary key(building_num, student_num, access_time)
) engine=innodb default charset=utf8 comment '被阻访问记录表';

insert into card_info values (99998, 1607094202);

create table card_info(
  `card_num` int comment '卡号',
  `student_num` int comment '学生学号',
  primary key(card_num)
) engine=innodb default charset=utf8 comment '一卡通注册信息表';

create table sensors_info(
   `mac_address` char(16) comment '传感器的MAC地址',
   `building_num` tinyint comment '所在的宿舍楼号',
   `location` char(15) comment '楼内的位置，如二层东',
    primary key(mac_address)
) engine=innodb default charset=utf8 comment '传感器信息表';

create table humiture_info(
   `mac_address` char(16) comment '传感器的MAC地址',
   `collect_time`  timestamp default current_timestamp comment '采集的时间',
   `temperature` float comment '温度，单位摄氏度',
   `humidity` float comment '相对湿度，单位',
   primary key(mac_address, collect_time)
) engine=innodb default charset=utf8 comment '温湿度数据表';

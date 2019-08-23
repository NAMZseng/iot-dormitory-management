use backend_test;

create table building_info(
  `num` tinyint comment '宿舍楼号',
  `name` char(30) comment '宿舍名称，最多10个汉字',
  `chief_name` char(15) comment '宿舍楼负责人名字',
  `chief_tel` char(11) comment '宿舍楼负责人电话',
  primary key(num)
) engine=innodb default charset=utf8 comment '宿舍楼信息表';

create table student_info(
  `num` int comment '学生学号',
  `name` char(30) comment '学生姓名',
  `school` char(45) comment '所在学院，最多15个汉字',
  `major` char(45) comment '专业名称',
  `teacher_name` char(30) comment '班主任',
  `teacher_tel` char(11) comment '班主任电话',
  `building_num` tinyint comment '所在宿舍楼号',
  `room_num` tinyint comment '房间号',
  primary key(num)
) engine=innodb default charset=utf8 comment '学生信息表';

create table access_info(
  `building_num` tinyint comment '出入的宿舍楼号',
  `student_num` int comment '学生学号',
  `access_time` datetime comment '出入时间',
  `access_status` char(3)  comment'出入状态，入：in 出：out',
  primary key(building_num, student_num, access_time)
) engine=innodb default charset=utf8 comment '学生出入宿舍楼记录表';

create table block_info(
  `building_num` tinyint comment '出入的宿舍楼号',
  `student_num` int comment '学生学号',
  `access_time` datetime  comment '出入时间',
  `access_status` char(3)  comment '出入状态，入：in 出：out',
  primary key(building_num, student_num, access_time)
) engine=innodb default charset=utf8 comment '被阻访问记录表';

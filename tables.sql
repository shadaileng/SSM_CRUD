-- CREATE DATABASE IF NOT EXISTS ssm DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

CREATE TABLE empl (
  empl_id int(11) auto_increment not null comment '员工id',
  empl_name varchar(255) not null comment '员工姓名',
  empl_gender char(1) not null comment '员工性别',
  empl_email varchar(255) not null comment '员工邮箱',
  dept_id int(11) not null comment '员工部门id',
  PRIMARY KEY (empl_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment = '员工表';

CREATE TABLE dept (
  dept_id int(11) auto_increment not null comment '部门id',
  dept_name varchar(255) not null comment '部门名称',
  PRIMARY KEY (dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment = '部门表';

ALTER TABLE empl ADD FOREIGN KEY (dept_id) REFERENCES dept(dept_id);

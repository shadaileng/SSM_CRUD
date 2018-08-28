CREATE TABLE empl (
  empl_id int(11) auto_increment not null,
  empl_name varchar(255) not null,
  empl_gender char(1) not null,
  empl_email varchar(255) not null,
  dept_id int(11) not null,
  PRIMARY KEY (empl_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE dept (
  dept_id int(11) auto_increment not null,
  dept_name varchar(255) not null,
  PRIMARY KEY (dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE empl ADD FOREIGN KEY (dept_id) REFERENCES dept(dept_id);

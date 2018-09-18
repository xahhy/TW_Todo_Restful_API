CREATE TABLE todo (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(100),
  status varchar(50),
  due_date date,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
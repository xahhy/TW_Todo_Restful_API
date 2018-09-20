CREATE TABLE todo (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(100),
  status varchar(50),
  due_date date,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE tag(
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(30),
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE todo_tag(
  todo_id bigint(20) NOT NULL,
  tag_id  bigint(20) NOT NULL,
--   CONSTRAINT fk_todo FOREIGN KEY (todo_id) REFERENCES todo(id),
--   CONSTRAINT fk_tag FOREIGN KEY (tag_id) REFERENCES tag(id),
  PRIMARY KEY(todo_id,tag_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;



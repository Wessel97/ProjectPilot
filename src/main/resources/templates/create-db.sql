DROP DATABASE IF EXISTS projectpilotdb;
CREATE SCHEMA projectpilotdb ;
USE projectpilotdb;

DROP TABLE IF EXISTS user;
CREATE TABLE user (
                      id INT NOT NULL AUTO_INCREMENT,
                      first_name VARCHAR(45) NOT NULL ,
                      last_name VARCHAR(45) NOT NULL ,
                      email VARCHAR(320) NOT NULL,
                      password VARCHAR(45) NOT NULL,
                      PRIMARY KEY (id));
INSERT INTO projectpilotdb.user (first_name, last_name,email,password) VALUES ('Frederik', 'Wessel', 'test@user.com', 'password123');

DROP TABLE IF EXISTS task;
CREATE TABLE task (
                      task_id INT NOT NULL AUTO_INCREMENT,
                      user_id INT NULL,
                      title VARCHAR(45) NOT NULL,
                      description VARCHAR(320) NULL,
                      note VARCHAR(320) NULL,
                      hours INT NOT NULL,
                      pay_rate INT NOT NULL DEFAULT 500,
                      flag TINYINT(1) NULL DEFAULT 0,
                      start_date VARCHAR(45) NOT NULL,
                      end_date VARCHAR(45) NOT NULL,
                      status VARCHAR(45) NOT NULL,
                      department VARCHAR(45) NOT NULL,
                      PRIMARY KEY (task_id),
                      FOREIGN KEY (user_id) REFERENCES user(id));
INSERT INTO projectpilotdb.task (title, description,hours,start_date,end_date,status,department) VALUES ('Hjemmesiden', 'desc test', '10', '05/05/2023', '12/05/2023', 'In Progress', 'Testing dep.');
USE projectpilotdb;

-- Drop foreign key constraint before dropping the tables
-- Note: You might need to adjust these commands according to the actual foreign key names in your database.
/*ALTER TABLE task DROP FOREIGN KEY task_ibfk_1;
ALTER TABLE task DROP FOREIGN KEY task_ibfk_2;
ALTER TABLE department DROP FOREIGN KEY department_ibfk_1;*/

-- Drop tables if they exist
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS department;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS user;

-- Create user table
CREATE TABLE user (
                      id INT NOT NULL AUTO_INCREMENT,
                      admin BOOLEAN NOT NULL DEFAULT FALSE,
                      first_name VARCHAR(45) NOT NULL,
                      last_name VARCHAR(45) NOT NULL,
                      email VARCHAR(320) NOT NULL,
                      password VARCHAR(100) NOT NULL,
                      profile_pic BLOB,
                      PRIMARY KEY (id)
);

-- Create project table
CREATE TABLE project(
                        id INT NOT NULL AUTO_INCREMENT,
                        name VARCHAR(45) NOT NULL,
                        PRIMARY KEY (id)
);

-- Create department table
CREATE TABLE department (
                            id INT NOT NULL AUTO_INCREMENT,
                            project_id INT NULL,
                            name VARCHAR(45) NOT NULL,
                            PRIMARY KEY (id),
                            FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
);

-- Create task table
CREATE TABLE task (
                      id INT NOT NULL AUTO_INCREMENT,
                      user_id INT NULL,
                      department_id INT NULL,
                      title VARCHAR(45) NOT NULL,
                      description VARCHAR(320) NULL,
                      note VARCHAR(320) NULL,
                      hours INT NOT NULL,
                      pay_rate INT NOT NULL DEFAULT 500,
                      flag BOOLEAN NOT NULL DEFAULT FALSE,
                      start_date DATE NOT NULL,
                      end_date DATE NOT NULL,
                      status VARCHAR(45) NOT NULL,
                      department VARCHAR(45) NULL,
                      project VARCHAR(45) NULL,
                      PRIMARY KEY (id),
                      FOREIGN KEY (user_id) REFERENCES user(id),
                      FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE CASCADE
);

-- Insert a dummy user
INSERT INTO user (admin, first_name, last_name, email, password)
VALUES (TRUE, 'Dummy', 'Dummy', 'test@user.com', '$2a$10$il68RiDz8kf2O2Dr47csfe4o9IUMlcM19R5E09DZjM3U9bTCP3ymi');

INSERT INTO project (name)
VALUES ('Talking AI'), ('Webpage for Jens'), ('Drone software'), ('Christmas party');

-- Insert departments
INSERT INTO department (project_id, name)
VALUES (1, 'HTML/CSS dep.'), (2, 'User Interface dep.'), (3, 'UX dep.'), (4, 'Cleaning');

-- Insert some sample tasks
-- Insert some sample tasks
INSERT INTO task (user_id, department_id, title, description, note, hours, pay_rate, flag, start_date, end_date, status)
VALUES (1, 1, 'Title 1', 'Description 1', 'Note 1', 40, 500, 0, '2023-05-01', '2023-05-31', 'Active');

INSERT INTO task (user_id, department_id, title, description, note, hours, pay_rate, flag, start_date, end_date, status)
VALUES (1, 2, 'Title 2', 'Description 2', 'Note 2', 30, 500, 0, '2023-06-01', '2023-06-30', 'Active');

INSERT INTO task (user_id, department_id, title, description, note, hours, pay_rate, flag, start_date, end_date, status)
VALUES (1, 3, 'Title 3', 'Description 3', 'Note 3', 20, 500, 0, '2023-07-01', '2023-07-31', 'Active');

INSERT INTO task (user_id, department_id, title, description, note, hours, pay_rate, flag, start_date, end_date, status)
VALUES (1, 4, 'Title 4', 'Description 4', 'Note 4', 50, 500, 0, '2023-08-01', '2023-08-31', 'Active');


/* https://bcrypt-generator.com/ to make hash code (10) */
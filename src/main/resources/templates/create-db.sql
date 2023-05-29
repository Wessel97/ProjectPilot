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

-- Insert 10 new users
INSERT INTO user (admin, first_name, last_name, email, password)
VALUES
    (FALSE, 'John', 'Doe', 'john.doe@example.com', '$2a$10$il68RiDz8kf2O2Dr47csfe4o9IUMlcM19R5E09DZjM3U9bTCP3ymi'),
    (FALSE, 'Jane', 'Smith', 'jane.smith@example.com', '$2a$10$il68RiDz8kf2O2Dr47csfe4o9IUMlcM19R5E09DZjM3U9bTCP3ymi'),
    (FALSE, 'David', 'Johnson', 'david.johnson@example.com', '$2a$10$il68RiDz8kf2O2Dr47csfe4o9IUMlcM19R5E09DZjM3U9bTCP3ymi'),
    (FALSE, 'Sarah', 'Williams', 'sarah.williams@example.com', '$2a$10$il68RiDz8kf2O2Dr47csfe4o9IUMlcM19R5E09DZjM3U9bTCP3ymi'),
    (FALSE, 'Michael', 'Brown', 'michael.brown@example.com', '$2a$10$il68RiDz8kf2O2Dr47csfe4o9IUMlcM19R5E09DZjM3U9bTCP3ymi'),
    (FALSE, 'Emily', 'Miller', 'emily.miller@example.com', '$2a$10$il68RiDz8kf2O2Dr47csfe4o9IUMlcM19R5E09DZjM3U9bTCP3ymi'),
    (FALSE, 'Christopher', 'Jones', 'christopher.jones@example.com', '$2a$10$il68RiDz8kf2O2Dr47csfe4o9IUMlcM19R5E09DZjM3U9bTCP3ymi'),
    (FALSE, 'Olivia', 'Davis', 'olivia.davis@example.com', '$2a$10$il68RiDz8kf2O2Dr47csfe4o9IUMlcM19R5E09DZjM3U9bTCP3ymi'),
    (FALSE, 'Daniel', 'Wilson', 'daniel.wilson@example.com', '$2a$10$il68RiDz8kf2O2Dr47csfe4o9IUMlcM19R5E09DZjM3U9bTCP3ymi'),
    (FALSE, 'Ava', 'Taylor', 'ava.taylor@example.com', '$2a$10$il68RiDz8kf2O2Dr47csfe4o9IUMlcM19R5E09DZjM3U9bTCP3ymi');


INSERT INTO project (name)
VALUES ('Talking AI'), ('Webpage for Jens'), ('Drone software'), ('Christmas party');

-- Insert departments
INSERT INTO department (project_id, name)
VALUES
    -- Departments for Project 1
    (1, 'Design'),
    (1, 'Development'),
    (1, 'Testing'),
    (1, 'Documentation'),
    (1, 'Deployment'),
    -- Departments for Project 2
    (2, 'Frontend'),
    (2, 'Backend'),
    (2, 'Database'),
    (2, 'Security'),
    (2, 'Quality Assurance'),
    -- Departments for Project 3
    (3, 'Research'),
    (3, 'Prototyping'),
    (3, 'Integration'),
    (3, 'Maintenance'),
    (3, 'Support'),
    -- Departments for Project 4
    (4, 'Marketing'),
    (4, 'Sales'),
    (4, 'Finance'),
    (4, 'Human Resources'),
    (4, 'Event Planning'),
    -- Departments for Project 5
    (5, 'Analytics'),
    (5, 'Reporting'),
    (5, 'Data Science'),
    (5, 'Machine Learning'),
    (5, 'Visualization');

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

INSERT INTO task (user_id, department_id, title, description, note, hours, pay_rate, flag, start_date, end_date, status)
VALUES
    -- Tasks for Department 1
    (1, 1, 'Design wireframes', 'Create wireframe designs for the user interface', 'Start with homepage design', 20, 550, 0, '2023-05-01', '2023-05-15', 'Active'),
    (2, 1, 'Create style guide', 'Develop a comprehensive style guide for the project', 'Include color palette and typography', 10, 500, 0, '2023-05-15', '2023-05-20', 'Active'),
    (3, 1, 'Design logo', 'Design a unique and visually appealing logo for the project', 'Consider brand identity and target audience', 5, 500, 0, '2023-05-20', '2023-05-23', 'Active'),
    (4, 1, 'Create UI mockups', 'Create high-fidelity UI mockups based on wireframes', 'Ensure consistency with the style guide', 15, 550, 0, '2023-05-23', '2023-05-31', 'Active'),
    (5, 1, 'Revise design based on feedback', 'Incorporate feedback from stakeholders and make necessary design revisions', 'Focus on improving usability and visual appeal', 8, 550, 0, '2023-06-01', '2023-06-05', 'Active'),
    (6, 1, 'Optimize design for responsiveness', 'Ensure the design is responsive and compatible with different screen sizes', 'Test on various devices and browsers', 12, 550, 0, '2023-06-05', '2023-06-10', 'Active'),
    (7, 1, 'Create design assets', 'Prepare and organize design assets for development', 'Include icons, images, and illustrations', 6, 500, 0, '2023-06-10', '2023-06-13', 'Active'),
    (8, 1, 'Collaborate with developers', 'Work closely with the development team to implement the design', 'Ensure design specifications are followed', 10, 550, 0, '2023-06-14', '2023-06-20', 'Active');

-- Department 2
INSERT INTO task (user_id, department_id, title, description, note, hours, pay_rate, flag, start_date, end_date, status)
VALUES
    -- Tasks for Department 2
    (1, 2, 'Develop frontend components', 'Implement frontend components based on the UI mockups', 'Use HTML, CSS, and JavaScript', 25, 600, 0, '2023-05-01', '2023-05-15', 'Active'),
    (2, 2, 'Implement responsive design', 'Ensure the frontend is responsive and adapts to different devices', 'Use media queries and CSS frameworks', 15, 600, 0, '2023-05-16', '2023-05-25', 'Active'),
    (3, 2, 'Integrate with backend APIs', 'Connect frontend components with backend APIs', 'Implement data retrieval and submission', 20, 600, 0, '2023-05-25', '2023-06-05', 'Active'),
    (4, 2, 'Implement user authentication', 'Develop user authentication functionality', 'Include registration, login, and password reset', 10, 600, 0, '2023-06-05', '2023-06-10', 'Active'),
    (5, 2, 'Implement data validation', 'Validate user input on forms and ensure data integrity', 'Use client-side and server-side validation techniques', 8, 600, 0, '2023-06-10', '2023-06-15', 'Active'),
    (6, 2, 'Optimize frontend performance', 'Optimize frontend code and assets for better performance', 'Reduce page load time and improve caching', 12, 600, 0, '2023-06-15', '2023-06-20', 'Active'),
    (7, 2, 'Collaborate with designers', 'Work closely with the design team to implement the frontend', 'Ensure design specifications are followed', 8, 600, 0, '2023-06-21', '2023-06-26', 'Active'),
    (8, 2, 'Perform cross-browser testing', 'Test the frontend on different browsers and devices', 'Ensure compatibility and consistent user experience', 10, 600, 0, '2023-06-27', '2023-07-01', 'Active');

-- Department 3
INSERT INTO task (user_id, department_id, title, description, note, hours, pay_rate, flag, start_date, end_date, status)
VALUES
    -- Tasks for Department 3
    (1, 3, 'Conduct user research', 'Gather user insights and conduct interviews or surveys', 'Identify user needs and pain points', 15, 550, 0, '2023-05-01', '2023-05-10', 'Active'),
    (2, 3, 'Create user personas', 'Develop fictional representations of target users', 'Consider demographics, behaviors, and goals', 8, 500, 0, '2023-05-10', '2023-05-15', 'Active'),
    (3, 3, 'Design user flows', 'Map out user flows and interactions within the application', 'Consider different user scenarios', 10, 550, 0, '2023-05-15', '2023-05-20', 'Active'),
    (4, 3, 'Create wireframes', 'Create low-fidelity wireframes to visualize the application structure', 'Focus on layout and content hierarchy', 12, 550, 0, '2023-05-20', '2023-05-25', 'Active'),
    (5, 3, 'Design interactive prototypes', 'Develop interactive prototypes to demonstrate user interactions', 'Use prototyping tools or frameworks', 15, 550, 0, '2023-05-26', '2023-06-05', 'Active'),
    (6, 3, 'Conduct usability testing', 'Test the prototypes with users and gather feedback', 'Identify usability issues and areas for improvement', 10, 550, 0, '2023-06-05', '2023-06-10', 'Active'),
    (7, 3, 'Iterate based on feedback', 'Incorporate user feedback and iterate on the design', 'Ensure usability and user satisfaction', 8, 550, 0, '2023-06-10', '2023-06-15', 'Active'),
    (8, 3, 'Collaborate with developers', 'Work closely with the development team to implement the user experience', 'Ensure design specifications are followed', 10, 550, 0, '2023-06-16', '2023-06-22', 'Active');

-- Department 4
INSERT INTO task (user_id, department_id, title, description, note, hours, pay_rate, flag, start_date, end_date, status)
VALUES
    -- Tasks for Department 4
    (1, 4, 'Create cleaning checklist', 'Develop a checklist for cleaning tasks', 'Include areas to be cleaned and cleaning materials', 5, 500, 0, '2023-05-01', '2023-05-04', 'Active'),
    (2, 4, 'Perform daily cleaning', 'Clean designated areas on a daily basis', 'Follow the cleaning checklist and maintain cleanliness', 8, 500, 0, '2023-05-04', '2023-05-10', 'Active'),
    (3, 4, 'Deep clean office space', 'Perform thorough cleaning of the entire office space', 'Include dusting, vacuuming, and sanitizing', 20, 550, 0, '2023-05-10', '2023-05-15', 'Active'),
    (4, 4, 'Manage cleaning supplies', 'Monitor and restock cleaning supplies and equipment', 'Ensure availability and proper storage', 5, 500, 0, '2023-05-15', '2023-05-18', 'Active'),
    (5, 4, 'Handle waste disposal', 'Properly dispose of waste and recyclable materials', 'Follow recycling guidelines and maintain cleanliness', 8, 500, 0, '2023-05-19', '2023-05-25', 'Active'),
    (6, 4, 'Clean kitchen and break area', 'Clean and organize the kitchen and break area', 'Ensure cleanliness and stock necessary items', 6, 500, 0, '2023-05-25', '2023-05-29', 'Active'),
    (7, 4, 'Maintain restroom cleanliness', 'Clean and sanitize restrooms regularly', 'Ensure hygiene and replenish supplies', 8, 500, 0, '2023-05-30', '2023-06-05', 'Active'),
    (8, 4, 'Perform periodic maintenance', 'Perform maintenance tasks as needed', 'Repair equipment and fix any issues', 10, 550, 0, '2023-06-06', '2023-06-12', 'Active');


/* https://bcrypt-generator.com/ to make hash code (10) */
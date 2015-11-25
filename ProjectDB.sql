DROP DATABASE if exists ProjectDB; 
CREATE DATABASE ProjectDB; 
USE ProjectDB; 

CREATE TABLE attraction (
	name VARCHAR(200) not null,
    phone VARCHAR(50),
    longitude DOUBLE not null,
    lattitude DOUBLE not null, 
    id INT(11) primary key auto_increment not null, 
    address VARCHAR (200), 
    avgrating INT(1)
); 

CREATE TABLE keywords (
    keywords VARCHAR(50) not null, 
    id INT(11) not null,
	FOREIGN KEY fk1(id) REFERENCES attraction(id),
    keywordid INT primary key auto_increment not null
); 

CREATE TABLE ratings (
	rating INT(11) not null, 
    id INT(11),
    FOREIGN KEY fk1(id) REFERENCES attraction(id),
    ratingid INT primary key auto_increment not null
); 

INSERT INTO attraction (name, phone, longitude, lattitude, address, avgrating) VALUES ('USC', '1 (323) 442-2000', 34.022316, -118.285106, '3551 Trousdale Pkwy, Los Angeles, CA 90089', 5); 
INSERT INTO attraction (name, phone, longitude, lattitude, address, avgrating) VALUES ('In N Out Burger', '1 (800) 786-1000', 33.986831, -118.224897, '6000 Villa Pacific Dr, Huntington Beach, CA 90255', 5); 
INSERT INTO attraction (name, phone, longitude, lattitude, address, avgrating) VALUES ('UCLA', '(310) 825-4321', 34.068957, -118.445009, '301 Westwood Plaza, Los Angeles, CA 90024', 1); 
INSERT INTO attraction (name, phone, longitude, lattitude, address, avgrating) VALUES ('901 Bar and Grill', '(213) 745-7900', 34.025796, -118.277310, '2902 S Figueroa St, Los Angeles, CA 90007', 3); 
INSERT INTO attraction (name, phone, longitude, lattitude, address, avgrating) VALUES ('Readink', '(323) 734-4323', 34.038275, -118.313885, '2261 W 21st St, Los Angeles, CA 90018', 4); 
INSERT INTO attraction (name, phone, longitude, lattitude, address, avgrating) VALUES ('9 Dots Community Center', '(323) 524-8328', 34.088033, -118.338919, '931 Highland Ave, Los Angeles, CA 90038', 5); 

INSERT INTO keywords (keywords, id) VALUES ("school", 1); 
INSERT INTO keywords (keywords, id) VALUES ("school", 3); 
INSERT INTO keywords (keywords, id) VALUES ("university", 1); 
INSERT INTO keywords (keywords, id) VALUES ("food", 2); 
INSERT INTO keywords (keywords, id) VALUES ("restaurant", 2); 
INSERT INTO keywords (keywords, id) VALUES ("university", 3); 
INSERT INTO keywords (keywords, id) VALUES ("stadium", 1); 

CREATE TABLE userinfo (
	firstname VARCHAR (50), 
    lastname VARCHAR (50), 
    cityinfo VARCHAR (200), 
    username VARCHAR (50) not null, 
    longitude DOUBLE, 
    lattitude DOUBLE,
    KEY (username), 
    password VARCHAR(50) not null,
    userid INT (11) primary key auto_increment not null
); 

CREATE TABLE testimonials (
	testimonial VARCHAR(200) not null, 
    id INT(11) not null, 
    userid INT(11) not null, 
    FOREIGN KEY fk1(id) REFERENCES attraction(id),
    FOREIGN KEY fk2(userid) REFERENCES userinfo(userid), 
    testimonialid INT primary key auto_increment not null
); 

CREATE TABLE userinterest (
    preference_1 INT(1), 
    preference_2 INT(1), 
    preference_3 INT(1), 
    preference_4 INT(1), 
    preference_5 INT(1), 
	username VARCHAR (50) not null, 
	FOREIGN KEY fk1(username) REFERENCES userinfo(username), 
    id INT(11) primary key auto_increment not null
); 

INSERT INTO userinfo (firstname, lastname, cityinfo, username, longitude, lattitude,password) VALUES ('TestUser', 'One', 'Los Angeles', 'user1', 34.025796, -118.277310, 'password'); 
INSERT INTO userinfo (firstname, lastname, cityinfo, username,longitude, lattitude, password) VALUES ('TestUser', 'Two', 'Los Angeles', 'test', 34.0, -130, 'password'); 
INSERT INTO userinfo (firstname, lastname, cityinfo, username, longitude, lattitude,password) VALUES ('Tran', 'Situ', 'San Francisco', 'tsitu', null, null, 'password'); 
INSERT INTO userinfo (firstname, lastname, cityinfo, username,longitude, lattitude, password) VALUES ('Vishnu', 'LastNameTooLong', 'Cincinnati', 'vishnu', null, null, 'password'); 
INSERT INTO userinfo (firstname, lastname, cityinfo, username,longitude, lattitude, password) VALUES ('Dana', 'Thomas', 'San Francisco', 'danathom', null, null, 'password'); 
INSERT INTO userinfo (firstname, lastname, cityinfo, username,longitude, lattitude, password) VALUES ('Willa', 'Zhao', 'Seattle', 'willaz', null, null, 'password'); 
INSERT INTO userinfo (firstname, lastname, cityinfo, username,longitude, lattitude, password) VALUES ('Arad', 'Margalit', 'San Francisco', 'jambajew', null, null, 'password'); 
INSERT INTO userinfo (firstname, lastname, cityinfo, username,longitude, lattitude, password) VALUES ('Jason', 'Wang', 'Denver', 'wasonjang', null, null, 'password'); 
INSERT INTO userinfo (firstname, lastname, cityinfo, username,longitude, lattitude, password) VALUES ('Prof', 'Miller', 'Los Angeles', 'jmiller', null, null, 'password'); 


INSERT INTO testimonials (testimonial, id, userid) VALUES ("This is the best school ever. Did you hear Team Crush went here?!testimonials", 1, 1); 
INSERT INTO testimonials (testimonial, id, userid) VALUES ("I hear it takes at least five years to graduate. :(", 3, 1); 
INSERT INTO testimonials (testimonial, id, userid) VALUES ("Neapolitan shakes AND animal fries? Hell yeah.", 2, 2); 
INSERT INTO testimonials (testimonial, id, userid) VALUES ("Coolest used bookstore in LA!.", 5, 1); 

INSERT INTO userinterest (username, preference_1, preference_2, preference_3, preference_4, preference_5) VALUES ('user1', 1, 2, 3, 4,5); 
INSERT INTO userinterest (username, preference_1, preference_2, preference_3, preference_4, preference_5) VALUES ('test', 5, 2, 1, 2,3); 
INSERT INTO userinterest (username, preference_1, preference_2, preference_3, preference_4, preference_5) VALUES ('tsitu', 5, 2, 1, 2,3); 
INSERT INTO userinterest (username, preference_1, preference_2, preference_3, preference_4, preference_5) VALUES ('vishnu', 5, 2, 1, 2,3); 
INSERT INTO userinterest (username, preference_1, preference_2, preference_3, preference_4, preference_5) VALUES ('danathom', 5, 2, 1, 2,3); 
INSERT INTO userinterest (username, preference_1, preference_2, preference_3, preference_4, preference_5) VALUES ('willaz', 5, 2, 1, 2,3); 
INSERT INTO userinterest (username, preference_1, preference_2, preference_3, preference_4, preference_5) VALUES ('jambajew', 5, 2, 1, 2,3); 
INSERT INTO userinterest (username, preference_1, preference_2, preference_3, preference_4, preference_5) VALUES ('wasonjang', 5, 2, 1, 2,3); 


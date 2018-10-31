-- DROP DATABASE IF EXISTS CalendarUsers;
CREATE DATABASE CalendarUsers;
USE CalendarUsers;

CREATE TABLE Users (
    email VARCHAR(100) PRIMARY KEY,
    username VARCHAR(100) NULL, 
    imgURL VARCHAR(500) NOT NULL
);

CREATE TABLE UserFollowing (
	followingID INT(11) PRIMARY KEY AUTO_INCREMENT,
    follower VARCHAR(100) NOT NULL,
    followingUser VARCHAR(100) NOT NULL,
    FOREIGN KEY fk3 (follower) REFERENCES Users(email),
    FOREIGN KEY fk2 (followingUser) REFERENCES Users(email)
);

CREATE TABLE EventList (
	eventNum INT(30) PRIMARY KEY AUTO_INCREMENT,
	eventID VARCHAR(500), 
	userEmail VARCHAR(100),
	ename VARCHAR(50),
    etime VARCHAR(50),
    edate VARCHAR(50),
    sDateTime VARCHAR(100),
    eDateTime VARCHAR(100),
    FOREIGN KEY fk1 (userEmail) REFERENCES Users(email)
);
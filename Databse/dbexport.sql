-- phpMyAdmin SQL Dump
-- version 2.11.11.3
-- http://www.phpmyadmin.net
--
-- Host: omega.uta.edu
-- Generation Time: Nov 03, 2013 at 09:54 PM
-- Server version: 5.0.95
-- PHP Version: 5.1.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `jwe0053`
--

-- --------------------------------------------------------

--
-- Table structure for table `mainCategories`
--

CREATE TABLE `mainCategories` (
  `categoryId` int(11) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY  (`categoryId`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `mainCategories`
--

INSERT INTO `mainCategories` VALUES(1, 'Math');
INSERT INTO `mainCategories` VALUES(2, 'Science');
INSERT INTO `mainCategories` VALUES(3, 'Language');
INSERT INTO `mainCategories` VALUES(4, 'Test Prep');
INSERT INTO `mainCategories` VALUES(5, 'Computer');
INSERT INTO `mainCategories` VALUES(6, 'Other');

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `reviewId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `tutorId` int(11) NOT NULL,
  `date` date NOT NULL,
  `rating` int(1) NOT NULL,
  `categoryId` int(11) NOT NULL,
  `subcategoryId` int(11) NOT NULL,
  `comment` varchar(255) NOT NULL,
  `reply` varchar(255) NOT NULL,
  PRIMARY KEY  (`reviewId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `reviews`
--


-- --------------------------------------------------------

--
-- Table structure for table `subCategories`
--

CREATE TABLE `subCategories` (
  `categoryId` int(11) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  `mainId` int(11) NOT NULL,
  `mainName` varchar(50) NOT NULL,
  PRIMARY KEY  (`categoryId`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=31 ;

--
-- Dumping data for table `subCategories`
--

INSERT INTO `subCategories` VALUES(1, 'Algebra', 1, 'Math');
INSERT INTO `subCategories` VALUES(2, 'Geometry', 1, 'Math');
INSERT INTO `subCategories` VALUES(3, 'Calculus', 1, 'Math');
INSERT INTO `subCategories` VALUES(4, 'Statistics', 1, 'Math');
INSERT INTO `subCategories` VALUES(5, 'Chemistry', 2, 'Science');
INSERT INTO `subCategories` VALUES(6, 'Physics', 2, 'Science');
INSERT INTO `subCategories` VALUES(7, 'Biology', 2, 'Science');
INSERT INTO `subCategories` VALUES(8, 'Anatomy', 2, 'Science');
INSERT INTO `subCategories` VALUES(9, 'Psychology', 2, 'Science');
INSERT INTO `subCategories` VALUES(10, 'English', 3, 'Language');
INSERT INTO `subCategories` VALUES(11, 'Spanish', 3, 'Language');
INSERT INTO `subCategories` VALUES(12, 'French', 3, 'Language');
INSERT INTO `subCategories` VALUES(13, 'German', 3, 'Language');
INSERT INTO `subCategories` VALUES(14, 'Italian', 3, 'Language');
INSERT INTO `subCategories` VALUES(15, 'Japanese', 3, 'Language');
INSERT INTO `subCategories` VALUES(16, 'Latin', 3, 'Language');
INSERT INTO `subCategories` VALUES(17, 'SAT', 4, 'Test Prep');
INSERT INTO `subCategories` VALUES(18, 'ACT', 4, 'Test Prep');
INSERT INTO `subCategories` VALUES(19, 'GRE', 4, 'Test Prep');
INSERT INTO `subCategories` VALUES(20, 'GED', 4, 'Test Prep');
INSERT INTO `subCategories` VALUES(21, 'C++', 5, 'Computer');
INSERT INTO `subCategories` VALUES(22, 'Java', 5, 'Computer');
INSERT INTO `subCategories` VALUES(23, 'Programming', 5, 'Computer');
INSERT INTO `subCategories` VALUES(24, 'Microsoft Office', 5, 'Computer');
INSERT INTO `subCategories` VALUES(25, 'Web Design', 5, 'Computer');
INSERT INTO `subCategories` VALUES(26, 'AutoCAD', 5, 'Computer');
INSERT INTO `subCategories` VALUES(27, 'Accounting', 6, 'Other');
INSERT INTO `subCategories` VALUES(28, 'Finance', 6, 'Other');
INSERT INTO `subCategories` VALUES(29, 'Economics', 6, 'Other');
INSERT INTO `subCategories` VALUES(30, 'Music', 6, 'Other');

-- --------------------------------------------------------

--
-- Table structure for table `subCatToTutor`
--

CREATE TABLE `subCatToTutor` (
  `tableId` int(11) NOT NULL auto_increment,
  `tutorId` int(11) NOT NULL,
  `subCategory` int(11) NOT NULL,
  PRIMARY KEY  (`tableId`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=23 ;

--
-- Dumping data for table `subCatToTutor`
--

INSERT INTO `subCatToTutor` VALUES(1, 1, 1);
INSERT INTO `subCatToTutor` VALUES(2, 1, 2);
INSERT INTO `subCatToTutor` VALUES(3, 1, 3);
INSERT INTO `subCatToTutor` VALUES(4, 1, 23);
INSERT INTO `subCatToTutor` VALUES(5, 1, 24);
INSERT INTO `subCatToTutor` VALUES(6, 1, 21);
INSERT INTO `subCatToTutor` VALUES(7, 2, 21);
INSERT INTO `subCatToTutor` VALUES(8, 2, 23);
INSERT INTO `subCatToTutor` VALUES(9, 2, 24);
INSERT INTO `subCatToTutor` VALUES(10, 2, 1);
INSERT INTO `subCatToTutor` VALUES(11, 2, 2);
INSERT INTO `subCatToTutor` VALUES(12, 2, 3);
INSERT INTO `subCatToTutor` VALUES(13, 3, 1);
INSERT INTO `subCatToTutor` VALUES(14, 3, 2);
INSERT INTO `subCatToTutor` VALUES(15, 3, 3);
INSERT INTO `subCatToTutor` VALUES(16, 3, 21);
INSERT INTO `subCatToTutor` VALUES(17, 3, 23);
INSERT INTO `subCatToTutor` VALUES(18, 3, 24);
INSERT INTO `subCatToTutor` VALUES(19, 4, 22);
INSERT INTO `subCatToTutor` VALUES(20, 4, 23);
INSERT INTO `subCatToTutor` VALUES(21, 4, 24);
INSERT INTO `subCatToTutor` VALUES(22, 4, 25);

-- --------------------------------------------------------

--
-- Table structure for table `tutors`
--

CREATE TABLE `tutors` (
  `tutorId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `zipcode` int(5) NOT NULL,
  `lat` double NOT NULL,
  `lon` double NOT NULL,
  `rate` float default NULL,
  `rating` float default NULL,
  `picture` varchar(255) default NULL,
  `profile` varchar(255) default NULL,
  `schedule` varchar(255) default NULL,
  `premium` tinyint(1) default NULL,
  PRIMARY KEY  (`tutorId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tutors`
--

INSERT INTO `tutors` VALUES(1, 1, 76063, 32.563183, -97.1416926, 60, 4.5, NULL, 'I love to tutor!', 'MWF 3-6 PM', 0);
INSERT INTO `tutors` VALUES(2, 2, 76040, 32.821201, -97.1036396, 40, 4.3, NULL, 'I know everything...', 'Sat/Sun 1-5 PM', 1);
INSERT INTO `tutors` VALUES(3, 3, 75052, 32.67942, -97.0283383, 45, 4.2, NULL, 'I''m great at tutoring ppl and would like to help you', 'Mon/Tues 8AM-8PM', NULL);
INSERT INTO `tutors` VALUES(4, 4, 76244, 32.9458766, -97.276076, 75, 3.5, NULL, 'Go Stars!', 'Tues/Thurs/Sat 10AM-3PM', 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userId` int(11) NOT NULL auto_increment,
  `userName` varchar(32) NOT NULL,
  `userPassword` varchar(32) NOT NULL,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `middleName` varchar(50) default NULL,
  `emailAddress` varchar(100) NOT NULL,
  `zipcode` int(5) NOT NULL,
  `lat` double NOT NULL,
  `lon` double NOT NULL,
  PRIMARY KEY  (`userId`),
  UNIQUE KEY `userName` (`userName`),
  UNIQUE KEY `emailAddress` (`emailAddress`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` VALUES(1, 'jeason', 'jeason', 'Jonathan', 'Eason', 'Wayne', 'jonathan.eason@mavs.uta.edu', 76063, 32.563183, -97.1416926);
INSERT INTO `users` VALUES(2, 'dweber', 'dweber', 'David', 'Weber', NULL, 'david.weber@mavs.uta.edu', 76040, 32.821201, -97.1036396);
INSERT INTO `users` VALUES(3, 'rdutt', 'rdutt', 'Richard', 'Dutt', NULL, 'richard.dutt@mavs.uta.edu', 75052, 32.67942, -97.0283383);
INSERT INTO `users` VALUES(4, 'scrane', 'scrane', 'Sean', 'Crane', NULL, 'sean.crane@mavs.uta.edu', 76244, 32.9458766, -97.276076);
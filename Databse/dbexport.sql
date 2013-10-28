-- phpMyAdmin SQL Dump
-- version 2.11.11.3
-- http://www.phpmyadmin.net
--
-- Host: omega.uta.edu
-- Generation Time: Oct 27, 2013 at 07:18 PM
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
-- Table structure for table `tutors`
--

CREATE TABLE `tutors` (
  `tutorId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `category` varchar(255) NOT NULL,
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

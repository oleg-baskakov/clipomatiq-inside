CREATE DATABASE `clipomatiqdb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `clipomatiqdb`;

#
# Table Objects for table artists
#

CREATE TABLE `artists` (
  `Id` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  `genre_id` int(11) default NULL,
  `description` varchar(255) default NULL,
  `status` int(1) default '0' COMMENT '0-new, 1- clips loaded',
  PRIMARY KEY  (`Id`),
  KEY `artist_name_indx` (`name`),
  KEY `idx_aid` (`Id`),
  KEY `genre_id_indx` (`genre_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `genries` (
  `Id` int(11) NOT NULL auto_increment,
  `name` varchar(64) NOT NULL,
  `description` text,
  `parent_id` int(11) NOT NULL default '0',
  `req_count` int(11) unsigned zerofill NOT NULL default '00000000001',
  PRIMARY KEY  (`Id`),
  UNIQUE KEY `id` (`Id`),
  UNIQUE KEY `id_indx` (`Id`),
  KEY `parent_id_indx` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `clips` (
  `Id` int(11) NOT NULL auto_increment,
  `artist_id` int(11) NOT NULL default '0',
  `title` varchar(255) NOT NULL,
  `content` text,
  `relevance_count` int(3) default '0',
  `youtube_id` varchar(16) NOT NULL,
  `average_cnt` double(13,5) default NULL,
  `media_url` tinytext NOT NULL,
  `average_rating` float(4,2) unsigned default NULL,
  `comments_url` tinytext,
  `view_cnt` int(11) unsigned default '0',
  `favorite_cnt` int(11) unsigned default '0',
  `thumb_url` tinytext NOT NULL,
  `duration` int(5) unsigned default NULL,
  `keywords` text,
  `published` date default NULL,
  `status` int(1) unsigned NOT NULL default '0',
  PRIMARY KEY  (`Id`),
  KEY `uid` (`youtube_id`),
  KEY `relcnt` (`relevance_count`),
  KEY `artist_indx` (`artist_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


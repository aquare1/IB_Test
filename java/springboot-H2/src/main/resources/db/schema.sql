

-- ----------------------------
-- Table structure for t_address
-- ----------------------------
DROP TABLE IF EXISTS `t_address`;
CREATE TABLE `t_address` (
  `id` varchar(64) NOT NULL,
  `adress_data` varchar(64) DEFAULT NULL COMMENT 'adressData',
  PRIMARY KEY (`id`)
) ;

-- ----------------------------
-- Records of t_address
-- ----------------------------

-- ----------------------------
-- Table structure for t_club
-- ----------------------------
DROP TABLE IF EXISTS `t_club`;
CREATE TABLE `t_club` (
  `id` varchar(64) NOT NULL COMMENT 'clubId',
  `club_name` varchar(64) DEFAULT NULL COMMENT 'clubName',
  PRIMARY KEY (`id`)
);

-- ----------------------------
-- Records of t_club
-- ----------------------------

-- ----------------------------
-- Table structure for t_club_person
-- ----------------------------
DROP TABLE IF EXISTS `t_club_person`;
CREATE TABLE `t_club_person` (
  `id` varchar(64) NOT NULL COMMENT 'clubPersonId',
  `person_id` varchar(64) DEFAULT NULL COMMENT 'personId',
  `club_id` varchar(64) DEFAULT NULL COMMENT 'clubId',
  PRIMARY KEY (`id`)
) ;

-- ----------------------------
-- Records of t_club_person
-- ----------------------------

-- ----------------------------
-- Table structure for t_person
-- ----------------------------
DROP TABLE IF EXISTS `t_person`;
CREATE TABLE `t_person` (
  `id` varchar(64) NOT NULL COMMENT 'person_id',
  `name` varchar(64) DEFAULT NULL COMMENT 'userName',
  PRIMARY KEY (`id`)
) ;



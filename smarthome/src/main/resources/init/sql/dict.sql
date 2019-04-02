DROP TABLE IF EXISTS `dict_area_type`;
CREATE TABLE `dict_area_type` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_area_type
-- ----------------------------
INSERT INTO `dict_area_type` VALUES ('1', '国家', '1');
INSERT INTO `dict_area_type` VALUES ('2', '省份、直辖市', '2');
INSERT INTO `dict_area_type` VALUES ('3', '地市', '3');
INSERT INTO `dict_area_type` VALUES ('4', '县区', '4');

-- ----------------------------
-- Table structure for `dict_device_operstatus`
-- ----------------------------
DROP TABLE IF EXISTS `dict_device_operstatus`;
CREATE TABLE `dict_device_operstatus` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_device_operstatus
-- ----------------------------
INSERT INTO `dict_device_operstatus` VALUES ('0', '离线', '1');
INSERT INTO `dict_device_operstatus` VALUES ('1', '在线', '2');
INSERT INTO `dict_device_operstatus` VALUES ('2', '异常', '3');

-- ----------------------------
-- Table structure for `dict_device_status`
-- ----------------------------
DROP TABLE IF EXISTS `dict_device_status`;
CREATE TABLE `dict_device_status` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_device_status
-- ----------------------------
INSERT INTO `dict_device_status` VALUES ('1', '库存', '1');
INSERT INTO `dict_device_status` VALUES ('2', '正常运营', '2');
INSERT INTO `dict_device_status` VALUES ('3', '维修', '3');
INSERT INTO `dict_device_status` VALUES ('4', '报废', '4');

DROP TABLE IF EXISTS `dict_device_type`;
CREATE TABLE `dict_device_type` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_device_type
-- ----------------------------
INSERT INTO `dict_device_type` VALUES ('1', '网关', '1');
INSERT INTO `dict_device_type` VALUES ('2', '长虹空调', '2');
INSERT INTO `dict_device_type` VALUES ('3', '智能插座', '3'); 
INSERT INTO `dict_device_type` VALUES ('4', '中央面板', '4'); 




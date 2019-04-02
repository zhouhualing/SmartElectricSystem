DROP TABLE IF EXISTS `tb_user_user`;
CREATE TABLE `tb_user_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile_phone` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_Number` varchar(255) DEFAULT NULL,
  `realRoleCode` varchar(500) DEFAULT NULL,
  `signature` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `userCode` varchar(255) DEFAULT NULL,
  `user_img` varchar(255) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `userType` varchar(255) DEFAULT NULL,
  `createUser_id` bigint(20) DEFAULT NULL,
  `lastModifyUser_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_b7j59ytcgoaa0ja4u9pxox957` (`createUser_id`),
  KEY `FK_p5ou6f1opy8rivek3xsp3wo0f` (`lastModifyUser_id`),
  CONSTRAINT `FK_b7j59ytcgoaa0ja4u9pxox957` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_p5ou6f1opy8rivek3xsp3wo0f` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user_user
-- ----------------------------
INSERT INTO `tb_user_user` VALUES ('1', '0', null, null, null, null, 'admin', null, null, null, null, null, 'admin', null, null, null, null, null);



-- ----------------------------
-- zcp:2015-01-05 Table structure for `tb_user_permission`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_permission`;
CREATE TABLE `tb_user_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `permissionCode` varchar(50) NOT NULL,
  `permissionName` varchar(100) DEFAULT NULL,
  `permissionType` int(11) DEFAULT NULL,
  `url` varchar(150) DEFAULT NULL,
  `createUser_id` bigint(20) DEFAULT NULL,
  `lastModifyUser_id` bigint(20) DEFAULT NULL,
  `parentPermissionEntity_id` bigint(20) DEFAULT NULL,
  `parentPermissionPath` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_noem3oo1qbv6mp47afchxv7mq` (`createUser_id`),
  KEY `FK_lc3aka9fvwpkx31kchqda6fqh` (`lastModifyUser_id`),
  KEY `FK_hfdxxsh43mutq1wldfvi4qu7d` (`parentPermissionEntity_id`),
  CONSTRAINT `FK_hfdxxsh43mutq1wldfvi4qu7d` FOREIGN KEY (`parentPermissionEntity_id`) REFERENCES `tb_user_permission` (`id`),
  CONSTRAINT `FK_lc3aka9fvwpkx31kchqda6fqh` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_noem3oo1qbv6mp47afchxv7mq` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user_permission
-- ----------------------------
INSERT INTO `tb_user_permission` VALUES ('1', '0', null, null, 'potal', '首页1', '1', null, null, null, null, null);
INSERT INTO `tb_user_permission` VALUES ('2', '0', null, null, 'ele', '用电管理', '1', null, null, null, null, null);
INSERT INTO `tb_user_permission` VALUES ('3', '0', null, null, 'device', '设备管理', '1', '/view/device/spmsDeviceList.html', null, null, null, null);
INSERT INTO `tb_user_permission` VALUES ('4', '1', null, null, 'custom', '订户管理', '1', null, null, null, null, null);
INSERT INTO `tb_user_permission` VALUES ('5', '0', null, null, 'area', '区域管理', '1', null, null, null, null, null);
INSERT INTO `tb_user_permission` VALUES ('6', '0', null, null, 'service', '服务管理', '1', null, null, null, null, null);
INSERT INTO `tb_user_permission` VALUES ('7', '0', null, null, 'system', '系统设置', '2', '/view/common/menu_tree.html', null, null, null, null);
INSERT INTO `tb_user_permission` VALUES ('8', '0', null, null, 'menu_manager', '菜单管理', '3', null, null, null, '7', null);
INSERT INTO `tb_user_permission` VALUES ('9', '0', null, null, 'role_manager', '角色管理', '3', null, null, null, '7', null);
INSERT INTO `tb_user_permission` VALUES ('10', '0', null, null, 'user_manager', '账户管理', '3', null, null, null, '7', null);
INSERT INTO `tb_user_permission` VALUES ('11', '0', null, null, 'flow_manager', '流程管理', '3', null, null, null, '7', null);

-- ----------------------------
-- Table structure for `user_permission`
-- ----------------------------
DROP TABLE IF EXISTS `user_permission`;
CREATE TABLE `user_permission` (
  `userentities_id` bigint(20) NOT NULL,
  `permissionentities_id` bigint(20) NOT NULL,
  PRIMARY KEY (`permissionentities_id`,`userentities_id`),
  KEY `FK_sx0oteotpb2l9j1y4gu8gbal7` (`userentities_id`),
  CONSTRAINT `FK_dmr3j79w5nbnvtq1sv5xux4l8` FOREIGN KEY (`permissionentities_id`) REFERENCES `tb_user_permission` (`id`),
  CONSTRAINT `FK_sx0oteotpb2l9j1y4gu8gbal7` FOREIGN KEY (`userentities_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_permission
-- ----------------------------
INSERT INTO `user_permission` VALUES ('1', '1');
INSERT INTO `user_permission` VALUES ('1', '2');
INSERT INTO `user_permission` VALUES ('1', '3');
INSERT INTO `user_permission` VALUES ('1', '4');
INSERT INTO `user_permission` VALUES ('1', '5');
INSERT INTO `user_permission` VALUES ('1', '6');
INSERT INTO `user_permission` VALUES ('1', '7');

-- ----------------------------
-- Table structure for spms_electro_result_data
-- ----------------------------
DROP TABLE IF EXISTS `spms_electro_result_data`;
CREATE TABLE `spms_electro_result_data` (
  `id` varchar(64) NOT NULL,
  `area_id` varchar(64) NOT NULL COMMENT '区域id',
  `start_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '开始时间',
  `power` int(11) DEFAULT NULL COMMENT '有功功率/有功电能',
  `reactive_power` int(11) DEFAULT NULL COMMENT '无功功率',
  `power_factor` int(11) DEFAULT NULL COMMENT '功率因数',
  `apparent_power` int(11) DEFAULT NULL COMMENT '视在功率',
  `reactive_energy` int(11) DEFAULT NULL COMMENT '无功电能',
  `reactive_demand` int(11) DEFAULT NULL COMMENT '无功需求',
  `active_demand` int(11) DEFAULT NULL COMMENT '有功需求',
  `demand_time` timestamp NULL DEFAULT NULL,
  `device_num` int(11) DEFAULT NULL,
  `accumulatePower` bigint(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `spms_electro_result_data_hour`;
CREATE TABLE `spms_electro_result_data_hour` (
  `id` int(64) NOT NULL AUTO_INCREMENT,
  `area_id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '区域id',
  `power` int(11) DEFAULT NULL COMMENT '有功功率/有功电能',
  `reactive_power` int(11) DEFAULT NULL COMMENT '无功功率',
  `power_factor` int(11) DEFAULT NULL COMMENT '功率因数',
  `apparent_power` int(11) DEFAULT NULL COMMENT '视在功率',
  `reactive_energy` int(11) DEFAULT NULL COMMENT '无功电能',
  `reactive_demand` int(11) DEFAULT NULL COMMENT '无功需求',
  `active_demand` int(11) DEFAULT NULL COMMENT '有功需求',
  `device_num` int(11) DEFAULT NULL,
  `accumulatePower` bigint(60) DEFAULT NULL,
  `date` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1369 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `spms_electro_result_data_month`;
CREATE TABLE `spms_electro_result_data_month` (
  `id` int(64) NOT NULL AUTO_INCREMENT,
  `area_id` varchar(64) NOT NULL COMMENT '区域id',
  `power` int(11) DEFAULT NULL COMMENT '有功功率/有功电能',
  `reactive_power` int(11) DEFAULT NULL COMMENT '无功功率',
  `power_factor` int(11) DEFAULT NULL COMMENT '功率因数',
  `apparent_power` int(11) DEFAULT NULL COMMENT '视在功率',
  `reactive_energy` int(11) DEFAULT NULL COMMENT '无功电能',
  `reactive_demand` int(11) DEFAULT NULL COMMENT '无功需求',
  `active_demand` int(11) DEFAULT NULL COMMENT '有功需求',
  `device_num` int(11) DEFAULT NULL,
  `accumulatePower` bigint(60) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=237 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `spms_electro_result_data_year`;
CREATE TABLE `spms_electro_result_data_year` (
  `id` int(64) NOT NULL AUTO_INCREMENT,
  `area_id` varchar(64) NOT NULL COMMENT '区域id',
  `power` int(11) DEFAULT NULL COMMENT '有功功率/有功电能',
  `reactive_power` int(11) DEFAULT NULL COMMENT '无功功率',
  `power_factor` int(11) DEFAULT NULL COMMENT '功率因数',
  `apparent_power` int(11) DEFAULT NULL COMMENT '视在功率',
  `reactive_energy` int(11) DEFAULT NULL COMMENT '无功电能',
  `reactive_demand` int(11) DEFAULT NULL COMMENT '无功需求',
  `active_demand` int(11) DEFAULT NULL COMMENT '有功需求',
  `device_num` int(11) DEFAULT NULL,
  `accumulatePower` bigint(60) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;




DROP TABLE IF EXISTS `spms_device_error`;
CREATE TABLE `spms_device_error` (
  `deviceId` varchar(100) NOT NULL,
  `errorCode` varchar(20) DEFAULT NULL,
  `id` varchar(255) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `tenantId` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_n1cfw8pd88xt869rlsolp4vhh` (`createUser_id`) USING BTREE,
  KEY `FK_8vlmdeel3yhpxommv7bgaug28` (`lastModifyUser_id`) USING BTREE,
  KEY `FK_6deoveg318ndy07gqie1rl6tf` (`deviceId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `spms_ac_status`;
CREATE TABLE `spms_ac_status` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备ID',
  `ac_id` varchar(64) DEFAULT NULL COMMENT '空调ID',
  `on_off` int(11) DEFAULT NULL COMMENT '开关',
  `temp` int(11) DEFAULT NULL COMMENT '室内温度',
  `ac_temp` int(11) DEFAULT NULL COMMENT '空调温度',
  `power` bigint(11) DEFAULT NULL COMMENT '功率',
  `speed` int(11) DEFAULT NULL COMMENT '风速',
  `direction` int(11) DEFAULT NULL COMMENT '风向',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `run_duration` int(11) DEFAULT NULL COMMENT '持续时长',
  `user_id` varchar(64) DEFAULT NULL COMMENT '所属用户',
  `model` int(11) DEFAULT NULL COMMENT '模式',
  `batery` int(11) DEFAULT NULL,
  `accumulatePower` bigint(11) DEFAULT NULL,
  `reactivePower` int(11) DEFAULT NULL COMMENT '无功功率',
  `reactiveEnergy` int(11) DEFAULT NULL COMMENT '无功电能',
  `apparentPower` int(11) DEFAULT NULL COMMENT '视在工率',
  `voltage` int(11) DEFAULT NULL COMMENT '电压',
  `current` int(11) DEFAULT NULL COMMENT '电流',
  `frequency` int(11) DEFAULT NULL COMMENT '频率',
  `powerFactor` int(11) DEFAULT NULL COMMENT '功率因数',
  `demandTime` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '需求开始时间',
  `period` int(11) DEFAULT NULL COMMENT '持续时间',
  `activeDemand` int(11) DEFAULT NULL COMMENT '有功需求',
  `reactiveDemand` int(11) DEFAULT NULL COMMENT '无功需求',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='空调运行记录';

DROP TABLE IF EXISTS `spms_ac_status_month`;
CREATE TABLE `spms_ac_status_month` (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备ID',
  `ac_id` varchar(64) DEFAULT NULL COMMENT '空调ID',
  `on_off` int(11) DEFAULT NULL COMMENT '开关',
  `temp` int(11) DEFAULT NULL COMMENT '室内温度',
  `ac_temp` int(11) DEFAULT NULL COMMENT '空调温度',
  `power` bigint(11) DEFAULT NULL COMMENT '功率',
  `speed` int(11) DEFAULT NULL COMMENT '风速',
  `direction` int(11) DEFAULT NULL COMMENT '风向',
  `run_duration` int(11) DEFAULT NULL COMMENT '持续时长',
  `user_id` varchar(64) DEFAULT NULL COMMENT '所属用户',
  `model` int(11) DEFAULT NULL COMMENT '模式',
  `batery` int(11) DEFAULT NULL,
  `accumulatePower` bigint(11) DEFAULT NULL,
  `reactivePower` int(11) DEFAULT NULL COMMENT '无功功率',
  `reactiveEnergy` int(11) DEFAULT NULL COMMENT '无功电能',
  `apparentPower` int(11) DEFAULT NULL COMMENT '视在工率',
  `voltage` int(11) DEFAULT NULL COMMENT '电压',
  `current` int(11) DEFAULT NULL COMMENT '电流',
  `frequency` int(11) DEFAULT NULL COMMENT '频率',
  `powerFactor` int(11) DEFAULT NULL COMMENT '功率因数',
  `period` int(11) DEFAULT NULL COMMENT '持续时间',
  `activeDemand` int(11) DEFAULT NULL COMMENT '有功需求',
  `reactiveDemand` int(11) DEFAULT NULL COMMENT '无功需求',
  `date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=15535 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `spms_ac_status_year`;
CREATE TABLE `spms_ac_status_year` (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备ID',
  `ac_id` varchar(64) DEFAULT NULL COMMENT '空调ID',
  `on_off` int(11) DEFAULT NULL COMMENT '开关',
  `temp` int(11) DEFAULT NULL COMMENT '室内温度',
  `ac_temp` int(11) DEFAULT NULL COMMENT '空调温度',
  `power` bigint(11) DEFAULT NULL COMMENT '功率',
  `speed` int(11) DEFAULT NULL COMMENT '风速',
  `direction` int(11) DEFAULT NULL COMMENT '风向',
  `run_duration` int(11) DEFAULT NULL COMMENT '持续时长',
  `user_id` varchar(64) DEFAULT NULL COMMENT '所属用户',
  `model` int(11) DEFAULT NULL COMMENT '模式',
  `batery` int(11) DEFAULT NULL,
  `accumulatePower` bigint(11) DEFAULT NULL,
  `reactivePower` int(11) DEFAULT NULL COMMENT '无功功率',
  `reactiveEnergy` int(11) DEFAULT NULL COMMENT '无功电能',
  `apparentPower` int(11) DEFAULT NULL COMMENT '视在工率',
  `voltage` int(11) DEFAULT NULL COMMENT '电压',
  `current` int(11) DEFAULT NULL COMMENT '电流',
  `frequency` int(11) DEFAULT NULL COMMENT '频率',
  `powerFactor` int(11) DEFAULT NULL COMMENT '功率因数',
  `period` int(11) DEFAULT NULL COMMENT '持续时间',
  `activeDemand` int(11) DEFAULT NULL COMMENT '有功需求',
  `reactiveDemand` int(11) DEFAULT NULL COMMENT '无功需求',
  `date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7068 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `spms_gw_status`;
CREATE TABLE `spms_gw_status` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备ID',
  `gw_id` varchar(64) DEFAULT NULL COMMENT '网关ID',
  `status` int(11) DEFAULT NULL COMMENT '网关状态',
  `user_id` varchar(64) DEFAULT NULL COMMENT '所属用户',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后记录时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='网关当前状态';


-- ----------------------------
-- Table structure for spms_win_door_status
-- ----------------------------
DROP TABLE IF EXISTS `spms_win_door_status`;
CREATE TABLE `spms_win_door_status` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备ID',
  `operate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `type_id` varchar(64) DEFAULT NULL COMMENT '设备类型',
  `operate_type` int(11) DEFAULT NULL COMMENT '操作类型',
  `user_id` varchar(64) DEFAULT NULL COMMENT '操作人',
  `remain` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='门窗操作日志';

DROP TABLE IF EXISTS `spms_ht_sensor_status`;
CREATE TABLE `spms_ht_sensor_status` (
  `id` varchar(100) NOT NULL,
  `mac` varchar(20) DEFAULT NULL,
  `temp` int(11) DEFAULT NULL,
  `humidity` int(11) DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `spms_onoff_pm_plug_status`;
CREATE TABLE `spms_onoff_pm_plug_status` (
  `id` varchar(50) NOT NULL,
  `mac` varchar(50) DEFAULT NULL,
  `onoff` int(11) DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `spms_pir_status`;
CREATE TABLE `spms_pir_status` (
  `id` varchar(50) NOT NULL,
  `mac` varchar(20) DEFAULT NULL,
  `alarmed` int(11) DEFAULT NULL,
  `operate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dict_user_type`;
CREATE TABLE `dict_user_type` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_user_type
-- ----------------------------
INSERT INTO `dict_user_type` VALUES ('0', '管理员', '0');
INSERT INTO `dict_user_type` VALUES ('1', '普通用户', '1');

DROP TABLE IF EXISTS `dict_spmsuser_type`;
CREATE TABLE `dict_spmsuser_type` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_spmsuser_type
-- ----------------------------
INSERT INTO `dict_spmsuser_type` VALUES ('1', '试用', '1');
INSERT INTO `dict_spmsuser_type` VALUES ('2', '商用', '2');
INSERT INTO `dict_spmsuser_type` VALUES ('3', '个人', '3');

DROP TABLE IF EXISTS `spms_dsm`;
CREATE TABLE `spms_dsm` (
  `deviceId` varchar(255) NOT NULL,
  `lowTemp` int(3) DEFAULT NULL,
  `upperTemp` int(3) DEFAULT NULL,
  `updateTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`deviceId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dict_device_storage
-- ----------------------------
DROP TABLE IF EXISTS `dict_device_storage`;
CREATE TABLE `dict_device_storage` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_device_storage
-- ----------------------------
INSERT INTO `dict_device_storage` VALUES ('1', '望京1号仓库', '1');
INSERT INTO `dict_device_storage` VALUES ('2', '学院路1号仓库', '2');
INSERT INTO `dict_device_storage` VALUES ('3', '亦庄1号仓库', '3');
INSERT INTO `dict_device_storage` VALUES ('5', '已出库', '5');

DROP TABLE IF EXISTS `dict_user_status`;
CREATE TABLE `dict_user_status` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_user_status
-- ----------------------------
INSERT INTO `dict_user_status` VALUES ('0', '正常', '0');
INSERT INTO `dict_user_status` VALUES ('1', '删除', '1');
INSERT INTO `dict_user_status` VALUES ('2', '冻结', '2');

DROP TABLE IF EXISTS `dict_user_type`;
CREATE TABLE `dict_user_type` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_user_type
-- ----------------------------
INSERT INTO `dict_user_type` VALUES ('0', '管理员', '0');
INSERT INTO `dict_user_type` VALUES ('1', '普通用户', '1');

DROP TABLE IF EXISTS `dict_warnset_level`;
CREATE TABLE `dict_warnset_level` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_warnset_level
-- ----------------------------
INSERT INTO `dict_warnset_level` VALUES ('0', '一级', '0');
INSERT INTO `dict_warnset_level` VALUES ('1', '二级', '1');


-- ----------------------------
-- Table structure for dict_area_policy
-- ----------------------------
DROP TABLE IF EXISTS `dict_area_policy`;
CREATE TABLE `dict_area_policy` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_area_policy
-- ----------------------------
INSERT INTO `dict_area_policy` VALUES ('1', '控电策略1', '1');
INSERT INTO `dict_area_policy` VALUES ('2', '控电策略2', '2');
INSERT INTO `dict_area_policy` VALUES ('3', '控电策略3', '3');
INSERT INTO `dict_area_policy` VALUES ('4', '控电策略4', '4');
INSERT INTO `dict_area_policy` VALUES ('5', '控电策略5', '5');


-- ----------------------------
-- Table structure for dict_area_classification
-- ----------------------------
DROP TABLE IF EXISTS `dict_area_classification`;
CREATE TABLE `dict_area_classification` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_area_classification
-- ----------------------------
INSERT INTO `dict_area_classification` VALUES ('1', '服务区域', '1');
INSERT INTO `dict_area_classification` VALUES ('2', '用电区域', '2');





////TODO alter
DROP TABLE IF EXISTS `dict_area_classification`;
CREATE TABLE `dict_area_classification` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_area_classification
-- ----------------------------
INSERT INTO `dict_area_classification` VALUES ('1', '服务区域', '1');
INSERT INTO `dict_area_classification` VALUES ('2', '用电区域', '2');

-- ----------------------------
-- Table structure for dict_area_policy
-- ----------------------------
DROP TABLE IF EXISTS `dict_area_policy`;
CREATE TABLE `dict_area_policy` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_area_policy
-- ----------------------------
INSERT INTO `dict_area_policy` VALUES ('1', '控电策略1', '1');
INSERT INTO `dict_area_policy` VALUES ('2', '控电策略2', '2');
INSERT INTO `dict_area_policy` VALUES ('3', '控电策略3', '3');
INSERT INTO `dict_area_policy` VALUES ('4', '控电策略4', '4');
INSERT INTO `dict_area_policy` VALUES ('5', '控电策略5', '5');

-- ----------------------------
-- Table structure for dict_area_type
-- ----------------------------
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
-- Table structure for dict_device_operstatus
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
-- Table structure for dict_device_status
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

-- ----------------------------
-- Table structure for dict_device_storage
-- ----------------------------
DROP TABLE IF EXISTS `dict_device_storage`;
CREATE TABLE `dict_device_storage` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_device_storage
-- ----------------------------
INSERT INTO `dict_device_storage` VALUES ('1', '望京1号仓库', '1');
INSERT INTO `dict_device_storage` VALUES ('2', '学院路1号仓库', '2');
INSERT INTO `dict_device_storage` VALUES ('3', '亦庄1号仓库', '3');
INSERT INTO `dict_device_storage` VALUES ('5', '已出库', '5');

-- ----------------------------
-- Table structure for dict_device_type
-- ----------------------------
DROP TABLE IF EXISTS `dict_device_type`;
CREATE TABLE `dict_device_type` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_device_type
-- ----------------------------

INSERT INTO `dict_device_type` VALUES ('1', 'Gateway', '1');
INSERT INTO `dict_device_type` VALUES ('2', 'Changhong Ac', '2');
INSERT INTO `dict_device_type` VALUES ('3', 'Plug Ac', '3');
INSERT INTO `dict_device_type` VALUES ('4', 'Central Ac', '4');
INSERT INTO `dict_device_type` VALUES ('5', 'Onoff Plug', '5');
INSERT INTO `dict_device_type` VALUES ('6', 'Onoff Pm Plug', '6');
INSERT INTO `dict_device_type` VALUES ('7', 'WinDoor', '7');
INSERT INTO `dict_device_type` VALUES ('8', 'Pir', '8');
INSERT INTO `dict_device_type` VALUES ('9', 'HtSensor', '9');
INSERT INTO `dict_device_type` VALUES ('10', 'Onoff Light', '10');
INSERT INTO `dict_device_type` VALUES ('11', 'Lamp', '11');

-- ----------------------------
-- Table structure for dict_user_status
-- ----------------------------
DROP TABLE IF EXISTS `dict_user_status`;
CREATE TABLE `dict_user_status` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_user_status
-- ----------------------------
INSERT INTO `dict_user_status` VALUES ('0', '正常', '0');
INSERT INTO `dict_user_status` VALUES ('1', '删除', '1');
INSERT INTO `dict_user_status` VALUES ('2', '冻结', '2');

-- ----------------------------
-- Table structure for dict_user_type
-- ----------------------------
DROP TABLE IF EXISTS `dict_user_type`;
CREATE TABLE `dict_user_type` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_user_type
-- ----------------------------
INSERT INTO `dict_user_type` VALUES ('0', '管理员', '0');
INSERT INTO `dict_user_type` VALUES ('1', '普通用户', '1');

-- ----------------------------
-- Table structure for dict_warnset_level
-- ----------------------------
DROP TABLE IF EXISTS `dict_warnset_level`;
CREATE TABLE `dict_warnset_level` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict_warnset_level
-- ----------------------------
INSERT INTO `dict_warnset_level` VALUES ('0', '一级', '0');
INSERT INTO `dict_warnset_level` VALUES ('1', '二级', '1');

-- ----------------------------
-- Table structure for spms_ac_status
-- ----------------------------
DROP TABLE IF EXISTS `spms_ac_status`;
CREATE TABLE `spms_ac_status` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备ID',
  `ac_id` varchar(64) DEFAULT NULL COMMENT '空调ID',
  `on_off` int(11) DEFAULT NULL COMMENT '开关',
  `temp` int(11) DEFAULT NULL COMMENT '室内温度',
  `ac_temp` int(11) DEFAULT NULL COMMENT '空调温度',
  `power` bigint(11) DEFAULT NULL COMMENT '功率',
  `speed` int(11) DEFAULT NULL COMMENT '风速',
  `direction` int(11) DEFAULT NULL COMMENT '风向',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `run_duration` int(11) DEFAULT NULL COMMENT '持续时长',
  `user_id` varchar(64) DEFAULT NULL COMMENT '所属用户',
  `model` int(11) DEFAULT NULL COMMENT '模式',
  `batery` int(11) DEFAULT NULL,
  `accumulatePower` bigint(11) DEFAULT NULL,
  `reactivePower` int(11) DEFAULT NULL COMMENT '无功功率',
  `reactiveEnergy` int(11) DEFAULT NULL COMMENT '无功电能',
  `apparentPower` int(11) DEFAULT NULL COMMENT '视在工率',
  `voltage` int(11) DEFAULT NULL COMMENT '电压',
  `current` int(11) DEFAULT NULL COMMENT '电流',
  `frequency` int(11) DEFAULT NULL COMMENT '频率',
  `powerFactor` int(11) DEFAULT NULL COMMENT '功率因数',
  `demandTime` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '需求开始时间',
  `period` int(11) DEFAULT NULL COMMENT '持续时间',
  `activeDemand` int(11) DEFAULT NULL COMMENT '有功需求',
  `reactiveDemand` int(11) DEFAULT NULL COMMENT '无功需求',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='空调运行记录';

-- ----------------------------
-- Table structure for spms_ac_status_month
-- ----------------------------
DROP TABLE IF EXISTS `spms_ac_status_month`;
CREATE TABLE `spms_ac_status_month` (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备ID',
  `ac_id` varchar(64) DEFAULT NULL COMMENT '空调ID',
  `on_off` int(11) DEFAULT NULL COMMENT '开关',
  `temp` int(11) DEFAULT NULL COMMENT '室内温度',
  `ac_temp` int(11) DEFAULT NULL COMMENT '空调温度',
  `power` bigint(11) DEFAULT NULL COMMENT '功率',
  `speed` int(11) DEFAULT NULL COMMENT '风速',
  `direction` int(11) DEFAULT NULL COMMENT '风向',
  `run_duration` int(11) DEFAULT NULL COMMENT '持续时长',
  `user_id` varchar(64) DEFAULT NULL COMMENT '所属用户',
  `model` int(11) DEFAULT NULL COMMENT '模式',
  `batery` int(11) DEFAULT NULL,
  `accumulatePower` bigint(11) DEFAULT NULL,
  `reactivePower` int(11) DEFAULT NULL COMMENT '无功功率',
  `reactiveEnergy` int(11) DEFAULT NULL COMMENT '无功电能',
  `apparentPower` int(11) DEFAULT NULL COMMENT '视在工率',
  `voltage` int(11) DEFAULT NULL COMMENT '电压',
  `current` int(11) DEFAULT NULL COMMENT '电流',
  `frequency` int(11) DEFAULT NULL COMMENT '频率',
  `powerFactor` int(11) DEFAULT NULL COMMENT '功率因数',
  `period` int(11) DEFAULT NULL COMMENT '持续时间',
  `activeDemand` int(11) DEFAULT NULL COMMENT '有功需求',
  `reactiveDemand` int(11) DEFAULT NULL COMMENT '无功需求',
  `date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=15593 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for spms_ac_status_year
-- ----------------------------
DROP TABLE IF EXISTS `spms_ac_status_year`;
CREATE TABLE `spms_ac_status_year` (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备ID',
  `ac_id` varchar(64) DEFAULT NULL COMMENT '空调ID',
  `on_off` int(11) DEFAULT NULL COMMENT '开关',
  `temp` int(11) DEFAULT NULL COMMENT '室内温度',
  `ac_temp` int(11) DEFAULT NULL COMMENT '空调温度',
  `power` bigint(11) DEFAULT NULL COMMENT '功率',
  `speed` int(11) DEFAULT NULL COMMENT '风速',
  `direction` int(11) DEFAULT NULL COMMENT '风向',
  `run_duration` int(11) DEFAULT NULL COMMENT '持续时长',
  `user_id` varchar(64) DEFAULT NULL COMMENT '所属用户',
  `model` int(11) DEFAULT NULL COMMENT '模式',
  `batery` int(11) DEFAULT NULL,
  `accumulatePower` bigint(11) DEFAULT NULL,
  `reactivePower` int(11) DEFAULT NULL COMMENT '无功功率',
  `reactiveEnergy` int(11) DEFAULT NULL COMMENT '无功电能',
  `apparentPower` int(11) DEFAULT NULL COMMENT '视在工率',
  `voltage` int(11) DEFAULT NULL COMMENT '电压',
  `current` int(11) DEFAULT NULL COMMENT '电流',
  `frequency` int(11) DEFAULT NULL COMMENT '频率',
  `powerFactor` int(11) DEFAULT NULL COMMENT '功率因数',
  `period` int(11) DEFAULT NULL COMMENT '持续时间',
  `activeDemand` int(11) DEFAULT NULL COMMENT '有功需求',
  `reactiveDemand` int(11) DEFAULT NULL COMMENT '无功需求',
  `date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7073 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `spms_dsm`;
CREATE TABLE `spms_dsm` (
  `deviceId` varchar(255) NOT NULL,
  `lowTemp` int(3) DEFAULT NULL,
  `upperTemp` int(3) DEFAULT NULL,
  `updateTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`deviceId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `spms_electro_result_data`;
CREATE TABLE `spms_electro_result_data` (
  `id` varchar(64) NOT NULL,
  `area_id` varchar(64) NOT NULL COMMENT '区域id',
  `start_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '开始时间',
  `power` int(11) DEFAULT NULL COMMENT '有功功率/有功电能',
  `reactive_power` int(11) DEFAULT NULL COMMENT '无功功率',
  `power_factor` int(11) DEFAULT NULL COMMENT '功率因数',
  `apparent_power` int(11) DEFAULT NULL COMMENT '视在功率',
  `reactive_energy` int(11) DEFAULT NULL COMMENT '无功电能',
  `reactive_demand` int(11) DEFAULT NULL COMMENT '无功需求',
  `active_demand` int(11) DEFAULT NULL COMMENT '有功需求',
  `demand_time` timestamp NULL DEFAULT NULL,
  `device_num` int(11) DEFAULT NULL,
  `accumulatePower` bigint(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `spms_electro_result_data_hour`;
CREATE TABLE `spms_electro_result_data_hour` (
  `id` int(64) NOT NULL AUTO_INCREMENT,
  `area_id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '区域id',
  `power` int(11) DEFAULT NULL COMMENT '有功功率/有功电能',
  `reactive_power` int(11) DEFAULT NULL COMMENT '无功功率',
  `power_factor` int(11) DEFAULT NULL COMMENT '功率因数',
  `apparent_power` int(11) DEFAULT NULL COMMENT '视在功率',
  `reactive_energy` int(11) DEFAULT NULL COMMENT '无功电能',
  `reactive_demand` int(11) DEFAULT NULL COMMENT '无功需求',
  `active_demand` int(11) DEFAULT NULL COMMENT '有功需求',
  `device_num` int(11) DEFAULT NULL,
  `accumulatePower` bigint(60) DEFAULT NULL,
  `date` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of spms_electro_result_data_hour
-- ----------------------------

-- ----------------------------
-- Table structure for spms_electro_result_data_month
-- ----------------------------
DROP TABLE IF EXISTS `spms_electro_result_data_month`;
CREATE TABLE `spms_electro_result_data_month` (
  `id` int(64) NOT NULL AUTO_INCREMENT,
  `area_id` varchar(64) NOT NULL COMMENT '区域id',
  `power` int(11) DEFAULT NULL COMMENT '有功功率/有功电能',
  `reactive_power` int(11) DEFAULT NULL COMMENT '无功功率',
  `power_factor` int(11) DEFAULT NULL COMMENT '功率因数',
  `apparent_power` int(11) DEFAULT NULL COMMENT '视在功率',
  `reactive_energy` int(11) DEFAULT NULL COMMENT '无功电能',
  `reactive_demand` int(11) DEFAULT NULL COMMENT '无功需求',
  `active_demand` int(11) DEFAULT NULL COMMENT '有功需求',
  `device_num` int(11) DEFAULT NULL,
  `accumulatePower` bigint(60) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=237 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `spms_electro_result_data_year`;
CREATE TABLE `spms_electro_result_data_year` (
  `id` int(64) NOT NULL AUTO_INCREMENT,
  `area_id` varchar(64) NOT NULL COMMENT '区域id',
  `power` int(11) DEFAULT NULL COMMENT '有功功率/有功电能',
  `reactive_power` int(11) DEFAULT NULL COMMENT '无功功率',
  `power_factor` int(11) DEFAULT NULL COMMENT '功率因数',
  `apparent_power` int(11) DEFAULT NULL COMMENT '视在功率',
  `reactive_energy` int(11) DEFAULT NULL COMMENT '无功电能',
  `reactive_demand` int(11) DEFAULT NULL COMMENT '无功需求',
  `active_demand` int(11) DEFAULT NULL COMMENT '有功需求',
  `device_num` int(11) DEFAULT NULL,
  `accumulatePower` bigint(60) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `spms_gw_status`;
CREATE TABLE `spms_gw_status` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备ID',
  `gw_id` varchar(64) DEFAULT NULL COMMENT '网关ID',
  `status` int(11) DEFAULT NULL COMMENT '网关状态',
  `user_id` varchar(64) DEFAULT NULL COMMENT '所属用户',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后记录时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='网关当前状态';


DROP TABLE IF EXISTS `spms_win_door_status`;
CREATE TABLE `spms_win_door_status` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `device_id` varchar(64) DEFAULT NULL COMMENT '设备ID',
  `operate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `type_id` varchar(64) DEFAULT NULL COMMENT '设备类型',
  `operate_type` int(11) DEFAULT NULL COMMENT '操作类型',
  `user_id` varchar(64) DEFAULT NULL COMMENT '操作人',
  `remain` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='门窗操作日志';


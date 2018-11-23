/*
Navicat MySQL Data Transfer

Source Server         : local_mysql
Source Server Version : 50540
Source Host           : localhost:3306
Source Database       : toms

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2016-04-12 17:16:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cfg_parameter
-- ----------------------------
DROP TABLE IF EXISTS `cfg_parameter`;
CREATE TABLE `cfg_parameter` (
  `UIID` double DEFAULT NULL COMMENT '唯一ID',
  `PDOMAIN` varchar(40) DEFAULT NULL COMMENT '模块标示',
  `PTYPE` varchar(40) DEFAULT NULL COMMENT '属性ID',
  `PKEY` varchar(254) DEFAULT NULL COMMENT '选项KEY',
  `PVALUE` longtext COMMENT '选项值',
  `PDESC` varchar(254) DEFAULT NULL COMMENT '扩展属性',
  `SYS_CREATE_BY` varchar(50) DEFAULT NULL COMMENT '创建人',
  `SYS_CREATE_DATE` date DEFAULT NULL COMMENT '创建日期',
  `SYS_UPDATE_BY` varchar(50) DEFAULT NULL COMMENT '修改人',
  `SYS_UPDATE_DATE` date DEFAULT NULL COMMENT '修改日期',
  `PSEQ` double DEFAULT NULL COMMENT '序号'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cfg_parameter
-- ----------------------------
INSERT INTO `cfg_parameter` VALUES ('1', 'TCL_TOMS', 'LANGUAGE', 'zh', '简体中文', '', '', null, '', null, '0');
INSERT INTO `cfg_parameter` VALUES ('23', 'TCL_TOMS', 'LANGUAGE', 'mm', '鸟语', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('3', 'TCL_TOMS', 'LANGUAGE', 'en', '英语', '', '', null, '', null, '0');
INSERT INTO `cfg_parameter` VALUES ('4', 'TCL_TOMS', 'LANGUAGE', 'af', '阿富汗语', '', '', null, '', null, '0');
INSERT INTO `cfg_parameter` VALUES ('22', 'TCL_TOMS', 'LANGUAGE', 'am', '亚美尼亚语', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('21', 'TCL_TOMS', 'LANGUAGE', 'ar', '阿根廷语', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('20', 'TCL_TOMS', 'LANGUAGE', 'bg', '保加利亚语', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('19', 'TCL_TOMS', 'LANGUAGE', 'ca', '加拿大语', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('18', 'TCL_TOMS', 'LANGUAGE', 'cs', '捷克文', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('17', 'TCL_TOMS', 'LANGUAGE', 'da', '丹麦文', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('16', 'TCL_TOMS', 'LANGUAGE', 'de', '德文', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('15', 'TCL_TOMS', 'LANGUAGE', 'el', '希腊文', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('14', 'TCL_TOMS', 'LANGUAGE', 'es', '西班牙文', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('13', 'TCL_TOMS', 'LANGUAGE', 'fr', '法文', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('12', 'TCL_TOMS', 'LANGUAGE', 'it', '立陶宛文', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('11', 'TCL_TOMS', 'LANGUAGE', 'ja', '日文', '', null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('10', 'TCL_TOMS', 'LANGUAGE', 'nl', '荷兰文', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('9', 'TCL_TOMS', 'LANGUAGE', 'pl', '波兰文', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('8', 'TCL_TOMS', 'LANGUAGE', 'pt', '葡萄牙文', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('7', 'TCL_TOMS', 'LANGUAGE', 'ru', '俄文', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('6', 'TCL_TOMS', 'LANGUAGE', 'sv', '瑞典文', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('5', 'TCL_TOMS', 'LANGUAGE', 'tr', '土耳其文', null, null, null, null, null, null);

-- ----------------------------
-- Table structure for city
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `CITY_ID` varchar(40) NOT NULL COMMENT '市编码',
  `CITY` varchar(40) DEFAULT NULL COMMENT '市',
  `CITY_EN` varchar(40) DEFAULT NULL COMMENT '市_英文',
  `CITY_LOCAL` varchar(40) DEFAULT NULL COMMENT '市_当地方言',
  `PROVINCE_ID` varchar(40) DEFAULT NULL COMMENT '省编码',
  `STATUS` char(1) DEFAULT NULL COMMENT '状态，1为启用，0为停用',
  PRIMARY KEY (`CITY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of city
-- ----------------------------

-- ----------------------------
-- Table structure for country
-- ----------------------------
DROP TABLE IF EXISTS `country`;
CREATE TABLE `country` (
  `COUNTRY_ID` varchar(40) NOT NULL COMMENT '国家编码',
  `COUNTRY` varchar(40) DEFAULT NULL COMMENT '国家(中文)',
  `COUNTRY_EN` varchar(40) DEFAULT NULL COMMENT '国家(英文)',
  `COUNTRY_LOCAL` varchar(40) DEFAULT NULL COMMENT '国家(当地方案)',
  `CONTINENT_ID` varchar(40) DEFAULT NULL COMMENT '洲编码',
  `STATUS` char(1) DEFAULT NULL COMMENT '状态，1为启用，0为停用',
  PRIMARY KEY (`COUNTRY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of country
-- ----------------------------

-- ----------------------------
-- Table structure for county
-- ----------------------------
DROP TABLE IF EXISTS `county`;
CREATE TABLE `county` (
  `COUNTY_ID` varchar(40) NOT NULL COMMENT '县编码',
  `COUNTY` varchar(40) DEFAULT NULL COMMENT '县',
  `COUNTY_EN` varchar(40) DEFAULT NULL COMMENT '县(英文)',
  `COUNTY_LOCAL` varchar(40) DEFAULT NULL COMMENT '县(当地)',
  `CITY_ID` varchar(40) DEFAULT NULL COMMENT '市编码',
  `STATUS` char(1) DEFAULT NULL COMMENT '状态，1为启用，0为停用',
  PRIMARY KEY (`COUNTY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of county
-- ----------------------------

-- ----------------------------
-- Table structure for customer_attribute
-- ----------------------------
DROP TABLE IF EXISTS `customer_attribute`;
CREATE TABLE `customer_attribute` (
  `CUSTOMER_ID` varchar(40) NOT NULL COMMENT '客户编码',
  `ATTR_NAME` varchar(60) DEFAULT NULL COMMENT '属性',
  `ATTR_VALUE` varchar(255) DEFAULT NULL COMMENT '属性值',
  PRIMARY KEY (`CUSTOMER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for customer_info
-- ----------------------------
DROP TABLE IF EXISTS `customer_info`;
CREATE TABLE `customer_info` (
  `CUSTOMER_ID` varchar(40) NOT NULL COMMENT '客户编码',
  `CUSTOMER_NAME` varchar(200) DEFAULT NULL COMMENT '客户名称',
  `PARTY_ID` varchar(40) DEFAULT NULL COMMENT '归属机构编码',
  `CREATE_BY` varchar(40) DEFAULT NULL COMMENT '创建人员',
  `CREATE_DATE` date DEFAULT NULL COMMENT '新建日期',
  `ENTER_DATE` date DEFAULT NULL COMMENT 'TCL品牌入驻日期',
  `COUNTRY_ID` varchar(40) DEFAULT NULL COMMENT '国家',
  `PROVINCE_ID` varchar(40) DEFAULT NULL COMMENT '省',
  `CITY_ID` varchar(40) DEFAULT NULL COMMENT '市',
  `COUNTY_ID` varchar(40) DEFAULT NULL COMMENT '县',
  `TOWN_ID` varchar(40) DEFAULT NULL COMMENT '镇',
  `DETAIL_ADDRESS` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `CONTACT_NAME` varchar(40) DEFAULT NULL COMMENT '联系人姓名',
  `PHONE` varchar(40) DEFAULT NULL COMMENT '联系人电话',
  `EMAIL` varchar(100) DEFAULT NULL,
  `WEBSITE` varchar(400) DEFAULT NULL COMMENT '客户网站',
  `STATUS` char(1) DEFAULT NULL COMMENT '状态，1为启用，0为停用,2待审批',
  `COMMENTS` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`CUSTOMER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer_info
-- ----------------------------

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `GROUP_ID` varchar(255) NOT NULL COMMENT '组ID',
  `GROUP_NAME` varchar(255) DEFAULT NULL COMMENT '组名称',
  `CREATE_BY` varchar(40) DEFAULT NULL COMMENT '角色描述',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建日期',
  `UPDATE_BY` varchar(40) DEFAULT NULL COMMENT '修改人',
  `UPDATE_DATE` datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`GROUP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of group
-- ----------------------------

-- ----------------------------
-- Table structure for group_data_permission
-- ----------------------------
DROP TABLE IF EXISTS `group_data_permission`;
CREATE TABLE `group_data_permission` (
  `GROUP_ID` varchar(200) DEFAULT NULL COMMENT '组ID',
  `PERMISSION_TYPE` varchar(255) DEFAULT NULL COMMENT '数据权限类型',
  `PERMISSION_VALUE` varchar(255) DEFAULT NULL COMMENT '数据权限数据'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of group_data_permission
-- ----------------------------

-- ----------------------------
-- Table structure for group_role
-- ----------------------------
DROP TABLE IF EXISTS `group_role`;
CREATE TABLE `group_role` (
  `GROUP_ID` varchar(40) DEFAULT NULL COMMENT '组ID',
  `ROLE_ID` varchar(40) DEFAULT NULL COMMENT '角色ID'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of group_role
-- ----------------------------

-- ----------------------------
-- Table structure for login_history
-- ----------------------------
DROP TABLE IF EXISTS `login_history`;
CREATE TABLE `login_history` (
  `USER_LOGIN_ID` varchar(40) DEFAULT NULL COMMENT '登陆账号',
  `LOGIN_DATETIME` date DEFAULT NULL COMMENT '登陆时间',
  `PASSWORD_USED` varchar(60) DEFAULT NULL COMMENT '登录使用密码',
  `SUCCESSFUL_LOGIN` char(1) DEFAULT NULL COMMENT '是否成功登录(0为成功，1为失败)',
  `PARTY_ID` varchar(40) DEFAULT NULL COMMENT '人员编码'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of login_history
-- ----------------------------

-- ----------------------------
-- Table structure for party
-- ----------------------------
DROP TABLE IF EXISTS `party`;
CREATE TABLE `party` (
  `PARTY_ID` varchar(40) NOT NULL COMMENT '组织编码',
  `PARTY_NAME` varchar(60) DEFAULT NULL COMMENT '组织名称',
  `FEDERAL_TAX_ID` varchar(60) DEFAULT NULL COMMENT '税务号',
  `STATUS` char(1) DEFAULT NULL COMMENT '状态(1为启用，0为停用)',
  `CREATE_DATE` date DEFAULT NULL COMMENT '创建日期',
  `CREATE_BY` varchar(40) DEFAULT NULL COMMENT '创建人员',
  `LAST_MODIFY_USER` varchar(40) DEFAULT NULL COMMENT '修改人',
  `LAST_MODIFY_DATE` date DEFAULT NULL COMMENT '最近更新日期',
  `GROUP_NAME_ABBR` varchar(60) DEFAULT NULL COMMENT '组织简称',
  `PARTY_ID_LAYER` varchar(40) DEFAULT NULL COMMENT '归属法人机构编码',
  `PARENT_PARTY_ID` varchar(40) DEFAULT NULL COMMENT '上级机构编码',
  `COMMENTS` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`PARTY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of party
-- ----------------------------

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `PERMISSION_ID` varchar(40) NOT NULL COMMENT '权限点编号',
  `PARENT_PERMISSION_ID` varchar(40) DEFAULT NULL COMMENT '上级权限',
  `PERMISSION_NAME` varchar(255) DEFAULT NULL COMMENT '权限名称',
  `IS_MENU` int(11) DEFAULT NULL COMMENT '是否菜单(1为菜单，0为非菜单)',
  `COMMENTS` varchar(255) DEFAULT NULL COMMENT '备注',
  `PERMISSION_CODE` varchar(40) DEFAULT NULL COMMENT '权限点简码',
  `PERMISSION_URL` varchar(255) DEFAULT NULL COMMENT '该权限点对应的URL',
  `BUTTON_ID` varchar(40) DEFAULT NULL COMMENT '页面元素ID',
  `PERMISSION_SEQ` int(10) DEFAULT NULL COMMENT '权限序号',
  PRIMARY KEY (`PERMISSION_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of permission
-- ----------------------------

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `PRODUCT_ID` varchar(40) NOT NULL COMMENT '产品编号',
  `party_id` varchar(40) NOT NULL COMMENT '所属机构',
  `PRODUCT_TYPE_ID` varchar(40) DEFAULT NULL COMMENT '产品类型',
  `INTRODUCTION_DATE` date DEFAULT NULL COMMENT '上市日期',
  `SALES_DISCONTINUATION_DATE` date DEFAULT NULL COMMENT '销售退市日期',
  `SUPPORT_DISCONTINUATION_DATE` date DEFAULT NULL COMMENT '售后支持截止日期',
  `PRODUCT_NAME` varchar(200) DEFAULT NULL COMMENT '产品名称',
  `MANUFACTURER_PARTY_ID` varchar(40) DEFAULT NULL COMMENT '制造商|事业部',
  `INTERNAL_NAME` varchar(60) DEFAULT NULL COMMENT '内部名称',
  `COMMENTS` varchar(255) DEFAULT NULL COMMENT '备注',
  `DESCRIPTION` varchar(255) DEFAULT NULL COMMENT '产品描述',
  `DESCRIPTION_EN` varchar(255) DEFAULT NULL COMMENT '产品描述－英文',
  `QUANTITY_UOM_ID` varchar(40) DEFAULT NULL COMMENT '数量单位编号',
  `COLOR_ID` varchar(10) DEFAULT NULL COMMENT '颜色编号',
  `VOLUME` double(5,2) DEFAULT NULL COMMENT '体积',
  `STATUS` char(1) DEFAULT NULL COMMENT '状态:1生效 0停用',
  `BRAND_ID` varchar(40) DEFAULT NULL COMMENT '品牌ID',
  `CATEGORY_ID` varchar(40) DEFAULT NULL COMMENT '大类ID',
  `PRODUCT_SPEC_ID` varchar(40) DEFAULT NULL COMMENT '小类1:规格编号',
  `PRODUCT_FUNC_ID` varchar(40) DEFAULT NULL COMMENT '小类2:功能编号',
  `PRODUCT_SCREEN_ID` varchar(40) DEFAULT NULL COMMENT '小类3:屏幕编号',
  `MODEL_NAME` varchar(40) DEFAULT NULL COMMENT '型号名称',
  `PHOTO` varchar(512) DEFAULT NULL COMMENT '产品图片',
  `product_type` varchar(255) DEFAULT NULL COMMENT '电视类型',
  `size` varchar(40) DEFAULT NULL COMMENT '屏幕尺寸',
  `display` varchar(40) DEFAULT NULL COMMENT '分辨率',
  `ratio` varchar(40) DEFAULT NULL COMMENT '屏幕比例',
  `power` varchar(40) DEFAULT NULL COMMENT '电源',
  `power_on` varchar(40) DEFAULT NULL COMMENT '开机功耗',
  `power_wait` varchar(40) DEFAULT NULL COMMENT '待机功耗',
  `netweight` varchar(40) DEFAULT NULL COMMENT '净重（不含底座）',
  `weight_include` varchar(40) DEFAULT NULL COMMENT '净重（含底座）',
  `weight` varchar(40) DEFAULT NULL COMMENT '毛重（含包装箱）',
  `interface` varchar(40) DEFAULT NULL COMMENT '接口类型',
  `network` varchar(40) DEFAULT NULL COMMENT '网络连接方式',
  `os` varchar(40) DEFAULT NULL COMMENT '操作系统',
  `file_path` varchar(512) DEFAULT NULL COMMENT '更多：见pdf文件(后台上传的pdf文件）',
  PRIMARY KEY (`PRODUCT_ID`,`party_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of product
-- ----------------------------

-- ----------------------------
-- Table structure for province
-- ----------------------------
DROP TABLE IF EXISTS `province`;
CREATE TABLE `province` (
  `PROVINCE_ID` varchar(40) NOT NULL COMMENT '省编码',
  `PROVINCE` varchar(40) DEFAULT NULL COMMENT '省',
  `PROVINCE_EN` varchar(40) DEFAULT NULL COMMENT '省_英文',
  `PROVINCE_LOCAL` varchar(100) DEFAULT NULL COMMENT '省_当地语言',
  `COUNTRY_ID` varchar(40) DEFAULT NULL COMMENT '国家编码',
  `STATUS` char(1) DEFAULT NULL COMMENT '状态，1为启用，0为停用',
  PRIMARY KEY (`PROVINCE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of province
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `ROLE_ID` varchar(255) NOT NULL COMMENT '角色ID',
  `ROLE_NAME` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `CREATE_BY` varchar(40) DEFAULT NULL COMMENT '创建人',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建日期',
  `UPDATE_BY` varchar(40) DEFAULT NULL COMMENT '修改人',
  `UPDATE_DATE` datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`ROLE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `ROLE_ID` varchar(40) DEFAULT NULL COMMENT '角色ID',
  `PERMISSION_ID` varchar(40) DEFAULT NULL COMMENT '权限ID'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_permission
-- ----------------------------

-- ----------------------------
-- Table structure for shop_info
-- ----------------------------
DROP TABLE IF EXISTS `shop_info`;
CREATE TABLE `shop_info` (
  `SHOP_ID` varchar(40) NOT NULL COMMENT '门店编码',
  `SHOP_NAME` varchar(255) DEFAULT NULL COMMENT '门店名称',
  `CUSTOMER_ID` varchar(40) DEFAULT NULL COMMENT '客户编码',
  `CREATE_BY` varchar(40) DEFAULT NULL COMMENT '创建人员',
  `CREATE_DATE` date DEFAULT NULL COMMENT '新建日期',
  `ENTER_DATE` date DEFAULT NULL COMMENT 'TCL品牌入驻日期',
  `COUNTRY_ID` varchar(40) DEFAULT NULL COMMENT '国家',
  `PROVINCE_ID` varchar(40) DEFAULT NULL COMMENT '省',
  `CITY_ID` varchar(40) DEFAULT NULL COMMENT '市',
  `COUNTY_ID` varchar(40) DEFAULT NULL COMMENT '县',
  `TOWN_ID` varchar(40) DEFAULT NULL COMMENT '镇',
  `DETAIL_ADDRESS` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `CONTACT_NAME` varchar(40) DEFAULT NULL COMMENT '联系人姓名',
  `PHONE` varchar(40) DEFAULT NULL COMMENT '联系人电话',
  `EMAIL` varchar(100) DEFAULT NULL,
  `LNG` int(11) DEFAULT NULL COMMENT '经度',
  `LAT` int(11) DEFAULT NULL COMMENT '维度',
  `STATUS` char(1) DEFAULT NULL COMMENT '状态，1为启用，0为停用,2待审批',
  `COMMENTS` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`SHOP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shop_info
-- ----------------------------

-- ----------------------------
-- Table structure for shop_salesman_rel
-- ----------------------------
DROP TABLE IF EXISTS `shop_salesman_rel`;
CREATE TABLE `shop_salesman_rel` (
  `SHOP_ID` varchar(40) DEFAULT NULL COMMENT '客户编码',
  `USER_LOGIN_ID` varchar(40) DEFAULT NULL COMMENT '人员编码',
  `FROM_DATE` date DEFAULT NULL COMMENT '开始日期',
  `END_DATE` date DEFAULT NULL COMMENT '结束日期（如果业务员在管辖此客户，那么此字段为空）',
  `IS_DEFAULT` char(1) DEFAULT NULL COMMENT '是否默认（Y是，N否）',
  `SALES_TYPE` varchar(40) DEFAULT NULL COMMENT '业务员类型（1：业务员  2：促销员）'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shop_salesman_rel
-- ----------------------------

-- ----------------------------
-- Table structure for town
-- ----------------------------
DROP TABLE IF EXISTS `town`;
CREATE TABLE `town` (
  `TOWN_ID` varchar(40) NOT NULL COMMENT '镇编码',
  `TOWN` varchar(40) DEFAULT NULL COMMENT '镇',
  `TOWN_EN` varchar(40) DEFAULT NULL COMMENT '镇_英文',
  `TOWN_LOCAL` varchar(40) DEFAULT NULL COMMENT '镇_当地方言',
  `COUNTY_ID` varchar(40) DEFAULT NULL COMMENT '县编码',
  `STATUS` char(1) DEFAULT NULL COMMENT '状态，1为启用，0为停用',
  PRIMARY KEY (`TOWN_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of town
-- ----------------------------

-- ----------------------------
-- Table structure for user_group
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `USER_LOGIN_ID` varchar(255) DEFAULT NULL COMMENT '用户ID',
  `GROUP_ID` varchar(255) NOT NULL COMMENT '组ID'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_group
-- ----------------------------

-- ----------------------------
-- Table structure for user_login
-- ----------------------------
DROP TABLE IF EXISTS `user_login`;
CREATE TABLE `user_login` (
  `USER_LOGIN_ID` varchar(40) DEFAULT NULL COMMENT '登录ID',
  `USER_NAME` varchar(40) DEFAULT NULL COMMENT '姓名',
  `PASSWORD` varchar(40) DEFAULT NULL COMMENT '密码',
  `PASSWORD_HINT` varchar(254) DEFAULT NULL COMMENT '密码提示',
  `ENABLED` varchar(1) DEFAULT NULL COMMENT '是否可用（1/0）',
  `DISABLED_DATE_TIME` date DEFAULT NULL COMMENT '失效日期',
  `LOGIN_TYPE` varchar(40) DEFAULT NULL COMMENT '登录类型(LOCAL/LDAP)',
  `CREATE_BY` varchar(40) DEFAULT NULL COMMENT '创建人',
  `CREATE_DATE` date DEFAULT NULL COMMENT '创建日期',
  `USER_EMAIL` varchar(40) DEFAULT NULL COMMENT '用户邮箱',
  `USER_WORK_NUM` varchar(20) DEFAULT NULL COMMENT '工号',
  `USER_MC_ID` varchar(40) DEFAULT NULL COMMENT '微信ID',
  `USER_TEL_NUM` varchar(20) DEFAULT NULL COMMENT '用户电话',
  `USER_LOCALE` varchar(10) DEFAULT NULL COMMENT '用户语言环境'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_login
-- ----------------------------
INSERT INTO `user_login` VALUES ('admin', '系统管理员', '1', '', 'Y', '2099-04-05', 'LOCAL', '', '2015-09-07', 'admin@tcl.com', '000000', '', '', 'zh');

-- ----------------------------
-- Table structure for wx_department
-- ----------------------------
DROP TABLE IF EXISTS `wx_department`;
CREATE TABLE `wx_department` (
  `ID` varchar(40) NOT NULL COMMENT '微信机构ID',
  `NAME` varchar(40) DEFAULT NULL COMMENT '微信机构名称',
  `PARENTID` varchar(40) DEFAULT NULL COMMENT '上级ID',
  `V_DATE` varchar(40) DEFAULT NULL COMMENT '同步时间',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_department
-- ----------------------------

-- ----------------------------
-- Table structure for wx_user_info
-- ----------------------------
DROP TABLE IF EXISTS `wx_user_info`;
CREATE TABLE `wx_user_info` (
  `USER_ID` varchar(200) DEFAULT NULL COMMENT '微信用户ID',
  `USER_NAME` varchar(200) DEFAULT NULL COMMENT '用户姓名',
  `DEPARTMENT` varchar(40) DEFAULT NULL COMMENT '微信部门',
  `POSITION` varchar(40) DEFAULT NULL COMMENT '职位',
  `MOBILE` varchar(20) DEFAULT NULL COMMENT '电话',
  `GENDER` int(11) DEFAULT NULL COMMENT '性别(0未知/1男/2女)',
  `EMAIL` varchar(80) DEFAULT NULL COMMENT '用户邮箱',
  `WEIXINID` varchar(80) DEFAULT NULL COMMENT '微信ID',
  `AVATAR` varchar(255) DEFAULT NULL COMMENT '未知',
  `STATUS` varchar(19) DEFAULT NULL COMMENT '状态',
  `EXTATTR` varchar(255) DEFAULT NULL COMMENT '附加属性JSON',
  `V_DATE` datetime DEFAULT NULL COMMENT '同步时间',
  `WORK_NUM` varchar(20) DEFAULT NULL COMMENT '工号'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wx_user_info
-- ----------------------------

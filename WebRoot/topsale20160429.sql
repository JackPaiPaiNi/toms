/*
MySQL Data Transfer
Source Host: 10.120.99.237
Source Database: topsale
Target Host: 10.120.99.237
Target Database: topsale
Date: 2016/4/29 8:58:58
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for cfg_parameter
-- ----------------------------
CREATE TABLE `cfg_parameter` (
  `UIID` int(9) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `PDOMAIN` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模块标示',
  `PTYPE` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '属性ID',
  `PKEY` varchar(254) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '选项KEY',
  `PVALUE` longtext COLLATE utf8_unicode_ci COMMENT '选项值',
  `PDESC` varchar(254) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '扩展属性',
  `SYS_CREATE_BY` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
  `SYS_CREATE_DATE` date DEFAULT NULL COMMENT '创建日期',
  `SYS_UPDATE_BY` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人',
  `SYS_UPDATE_DATE` date DEFAULT NULL COMMENT '修改日期',
  `PSEQ` double DEFAULT NULL COMMENT '序号',
  PRIMARY KEY (`UIID`)
) ENGINE=MyISAM AUTO_INCREMENT=52 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for city
-- ----------------------------
CREATE TABLE `city` (
  `CITY_ID` varchar(40) COLLATE utf8_unicode_ci NOT NULL COMMENT '市编码',
  `CITY` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '市',
  `CITY_EN` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '市_英文',
  `CITY_LOCAL` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '市_当地方言',
  `PROVINCE_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '省编码',
  `STATUS` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态，1为启用，0为停用',
  PRIMARY KEY (`CITY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for country
-- ----------------------------
CREATE TABLE `country` (
  `COUNTRY_ID` varchar(40) COLLATE utf8_unicode_ci NOT NULL COMMENT '国家编码',
  `COUNTRY` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '国家(中文)',
  `COUNTRY_EN` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '国家(英文)',
  `COUNTRY_LOCAL` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '国家(当地方案)',
  `STATUS` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态，1为启用，0为停用',
  PRIMARY KEY (`COUNTRY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for county
-- ----------------------------
CREATE TABLE `county` (
  `COUNTY_ID` varchar(40) COLLATE utf8_unicode_ci NOT NULL COMMENT '县编码',
  `COUNTY` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '县',
  `COUNTY_EN` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '县(英文)',
  `COUNTY_LOCAL` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '县(当地)',
  `CITY_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '市编码',
  `STATUS` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态，1为启用，0为停用',
  PRIMARY KEY (`COUNTY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for customer_attribute
-- ----------------------------
CREATE TABLE `customer_attribute` (
  `CUSTOMER_ID` varchar(40) COLLATE utf8_unicode_ci NOT NULL COMMENT '客户编码',
  `ATTR_NAME` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '属性',
  `ATTR_VALUE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '属性值',
  PRIMARY KEY (`CUSTOMER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for customer_info
-- ----------------------------
CREATE TABLE `customer_info` (
  `CUSTOMER_ID` varchar(40) COLLATE utf8_unicode_ci NOT NULL COMMENT '客户编码',
  `CUSTOMER_NAME` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '客户名称',
  `PARTY_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '归属机构编码',
  `CREATE_BY` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人员',
  `CREATE_DATE` date DEFAULT NULL COMMENT '新建日期',
  `ENTER_DATE` date DEFAULT NULL COMMENT 'TCL品牌入驻日期',
  `COUNTRY_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '国家',
  `PROVINCE_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '省',
  `CITY_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '市',
  `COUNTY_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '县',
  `TOWN_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '镇',
  `DETAIL_ADDRESS` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '详细地址',
  `CONTACT_NAME` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '联系人姓名',
  `PHONE` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '联系人电话',
  `EMAIL` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `WEBSITE` varchar(400) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '客户网站',
  `STATUS` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态，1为启用，0为停用,2待审批',
  `COMMENTS` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`CUSTOMER_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for login_history
-- ----------------------------
CREATE TABLE `login_history` (
  `USER_LOGIN_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '登陆账号',
  `LOGIN_DATETIME` datetime DEFAULT NULL COMMENT '登陆时间',
  `PASSWORD_USED` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '登录使用密码',
  `SUCCESSFUL_LOGIN` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否成功登录(0为成功，1为失败)',
  `PARTY_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '组织编码',
  `COMMENTS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '登录消息'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for party
-- ----------------------------
CREATE TABLE `party` (
  `PARTY_ID` varchar(40) COLLATE utf8_unicode_ci NOT NULL COMMENT '组织编码',
  `PARTY_NAME` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '组织名称',
  `FEDERAL_TAX_ID` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '税务号',
  `STATUS` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态(1为启用，0为停用)',
  `CREATE_DATE` date DEFAULT NULL COMMENT '创建日期',
  `CREATE_BY` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人员',
  `LAST_MODIFY_USER` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人',
  `LAST_MODIFY_DATE` date DEFAULT NULL COMMENT '最近更新日期',
  `GROUP_NAME_ABBR` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '组织简称',
  `PARTY_ID_LAYER` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '归属法人机构编码',
  `PARENT_PARTY_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上级机构编码',
  `COMMENTS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`PARTY_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
CREATE TABLE `permission` (
  `PERMISSION_ID` varchar(40) COLLATE utf8_unicode_ci NOT NULL COMMENT '权限点编号',
  `PARENT_PERMISSION_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上级权限',
  `PERMISSION_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '权限名称',
  `LABEL_KEY` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '国际化KEY',
  `PERMISSION_SEQ` int(10) DEFAULT NULL COMMENT '权限序号',
  `IS_MENU` int(11) DEFAULT NULL COMMENT '是否菜单(1为菜单，0为非菜单)',
  `COMMENTS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `PERMISSION_CODE` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '权限点简码',
  `PERMISSION_URL` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '该权限点对应的URL',
  `BUTTON_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '页面元素ID',
  PRIMARY KEY (`PERMISSION_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for product
-- ----------------------------
CREATE TABLE `product` (
  `PRODUCT_ID` varchar(40) COLLATE utf8_unicode_ci NOT NULL COMMENT '产品编号',
  `party_id` varchar(40) COLLATE utf8_unicode_ci NOT NULL COMMENT '所属机构',
  `PRODUCT_TYPE_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '产品类型',
  `INTRODUCTION_DATE` date DEFAULT NULL COMMENT '上市日期',
  `SALES_DISCONTINUATION_DATE` date DEFAULT NULL COMMENT '销售退市日期',
  `SUPPORT_DISCONTINUATION_DATE` date DEFAULT NULL COMMENT '售后支持截止日期',
  `PRODUCT_NAME` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '产品名称',
  `MANUFACTURER_PARTY_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '制造商|事业部',
  `INTERNAL_NAME` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '内部名称',
  `COMMENTS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '产品描述',
  `DESCRIPTION_EN` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '产品描述－英文',
  `QUANTITY_UOM_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数量单位编号',
  `COLOR_ID` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '颜色编号',
  `VOLUME` double(5,2) DEFAULT NULL COMMENT '体积',
  `STATUS` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态:1生效 0停用',
  `BRAND_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '品牌ID',
  `CATEGORY_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '大类ID',
  `PRODUCT_SPEC_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '小类1:规格编号',
  `PRODUCT_FUNC_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '小类2:功能编号',
  `PRODUCT_SCREEN_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '小类3:屏幕编号',
  `MODEL_NAME` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '型号名称',
  `PHOTO` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '产品图片',
  `product_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '电视类型',
  `size` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '屏幕尺寸',
  `display` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '分辨率',
  `ratio` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '屏幕比例',
  `power` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '电源',
  `power_on` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '开机功耗',
  `power_wait` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '待机功耗',
  `netweight` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '净重（不含底座）',
  `weight_include` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '净重（含底座）',
  `weight` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '毛重（含包装箱）',
  `interface` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '接口类型',
  `network` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '网络连接方式',
  `os` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '操作系统',
  `file_path` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更多：见pdf文件(后台上传的pdf文件）',
  PRIMARY KEY (`PRODUCT_ID`,`party_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for province
-- ----------------------------
CREATE TABLE `province` (
  `PROVINCE_ID` varchar(40) COLLATE utf8_unicode_ci NOT NULL COMMENT '省编码',
  `PROVINCE` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '省',
  `PROVINCE_EN` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '省_英文',
  `PROVINCE_LOCAL` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '省_当地语言',
  `COUNTRY_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '国家编码',
  `STATUS` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态，1为启用，0为停用',
  PRIMARY KEY (`PROVINCE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for role
-- ----------------------------
CREATE TABLE `role` (
  `ROLE_ID` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '角色ID',
  `ROLE_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '角色名称',
  `CREATE_BY` varchar(40) COLLATE utf8_unicode_ci DEFAULT 'admin' COMMENT '创建人',
  `CREATE_DATE` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `UPDATE_BY` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人',
  `UPDATE_DATE` timestamp NULL DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`ROLE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for role_data_permission
-- ----------------------------
CREATE TABLE `role_data_permission` (
  `ROLE_ID` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '组ID',
  `PERMISSION_TYPE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据权限类型',
  `PERMISSION_VALUE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据权限数据'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
CREATE TABLE `role_permission` (
  `ROLE_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '角色ID',
  `PERMISSION_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '权限ID',
  `CHECK_STATE` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '选中状态(checked:选择，unchecked:未选择，indetermin:半选择)'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for shop_info
-- ----------------------------
CREATE TABLE `shop_info` (
  `SHOP_ID` varchar(40) COLLATE utf8_unicode_ci NOT NULL COMMENT '门店编码',
  `SHOP_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '门店名称',
  `CUSTOMER_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '客户编码',
  `CREATE_BY` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人员',
  `CREATE_DATE` date DEFAULT NULL COMMENT '新建日期',
  `ENTER_DATE` date DEFAULT NULL COMMENT 'TCL品牌入驻日期',
  `COUNTRY_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '国家',
  `PROVINCE_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '省',
  `CITY_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '市',
  `COUNTY_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '县',
  `TOWN_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '镇',
  `DETAIL_ADDRESS` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '详细地址',
  `CONTACT_NAME` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '联系人姓名',
  `PHONE` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '联系人电话',
  `EMAIL` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `LNG` float DEFAULT NULL COMMENT '经度',
  `LAT` float DEFAULT NULL COMMENT '维度',
  `STATUS` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态，1为启用，0为停用,2待审批',
  `COMMENTS` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`SHOP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_barcode
-- ----------------------------
CREATE TABLE `t_barcode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `barcode` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `hq_model` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`barcode`),
  UNIQUE KEY `id` (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_camera
-- ----------------------------
CREATE TABLE `t_camera` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `serverid` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `latitude` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `longitude` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `address` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `shop_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '门店id',
  `datadate` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_modelmap
-- ----------------------------
CREATE TABLE `t_modelmap` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `branch_model` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '分公司型号',
  `hq_model` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '总部型号',
  `party_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '所属国家',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `id` (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_sale
-- ----------------------------
CREATE TABLE `t_sale` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `model` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `remark` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `datadate` varchar(12) COLLATE utf8_unicode_ci DEFAULT NULL,
  `country` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_sale_target
-- ----------------------------
CREATE TABLE `t_sale_target` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_sample
-- ----------------------------
CREATE TABLE `t_sample` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `model` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `remark` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `datadate` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `party_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for town
-- ----------------------------
CREATE TABLE `town` (
  `TOWN_ID` varchar(40) COLLATE utf8_unicode_ci NOT NULL COMMENT '镇编码',
  `TOWN` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '镇',
  `TOWN_EN` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '镇_英文',
  `TOWN_LOCAL` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '镇_当地方言',
  `COUNTY_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '县编码',
  `STATUS` char(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态，1为启用，0为停用',
  PRIMARY KEY (`TOWN_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for user_login
-- ----------------------------
CREATE TABLE `user_login` (
  `USER_LOGIN_ID` varchar(40) COLLATE utf8_unicode_ci NOT NULL COMMENT '登录ID',
  `USER_NAME` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '姓名',
  `PASSWORD` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '密码',
  `PASSWORD_HINT` varchar(254) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '密码提示',
  `ENABLED` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否可用（1/0）',
  `DISABLED_DATE_TIME` date DEFAULT NULL COMMENT '失效日期',
  `LOGIN_TYPE` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '登录类型(LOCAL/LDAP)',
  `CREATE_BY` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
  `CREATE_DATE` date DEFAULT NULL COMMENT '创建日期',
  `USER_EMAIL` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户邮箱',
  `USER_WORK_NUM` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '工号',
  `USER_MC_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '微信ID',
  `USER_TEL_NUM` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户电话',
  `USER_LOCALE` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户语言环境',
  `PARTY_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '机构',
  PRIMARY KEY (`USER_LOGIN_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for user_role_mapping
-- ----------------------------
CREATE TABLE `user_role_mapping` (
  `USER_LOGIN_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '组ID',
  `ROLE_ID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '角色ID'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for wx_department
-- ----------------------------
CREATE TABLE `wx_department` (
  `ID` varchar(40) COLLATE utf8_unicode_ci NOT NULL COMMENT '微信机构ID',
  `NAME` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '微信机构名称',
  `PARENTID` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上级ID',
  `V_DATE` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '同步时间',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for wx_user_info
-- ----------------------------
CREATE TABLE `wx_user_info` (
  `USER_ID` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '微信用户ID',
  `USER_NAME` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户姓名',
  `DEPARTMENT` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '微信部门',
  `POSITION` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '职位',
  `MOBILE` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '电话',
  `GENDER` int(11) DEFAULT NULL COMMENT '性别(0未知/1男/2女)',
  `EMAIL` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户邮箱',
  `WEIXINID` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '微信ID',
  `AVATAR` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '未知',
  `STATUS` varchar(19) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态',
  `EXTATTR` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '附加属性JSON',
  `V_DATE` datetime DEFAULT NULL COMMENT '同步时间',
  `WORK_NUM` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '工号'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Function structure for getAllPartyByPID
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `getAllPartyByPID`(parentId VARCHAR(40)) RETURNS varchar(2048) CHARSET utf8
BEGIN
       DECLARE sTemp VARCHAR(2048);
       DECLARE sTempChd VARCHAR(2048);
    
       SET sTemp = '';
			 SET sTempChd =parentId;
    
       WHILE sTempChd is not null DO
         SET sTemp = concat(sTemp,',',sTempChd);
         SELECT group_concat(PARTY_ID) INTO sTempChd FROM party where FIND_IN_SET(PARENT_PARTY_ID,sTempChd)>0;
       END WHILE;
       RETURN sTemp;
		END;;
DELIMITER ;

-- ----------------------------
-- Function structure for getAllPermissionByPID
-- ----------------------------
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `getAllPermissionByPID`(parentId VARCHAR(40)) RETURNS varchar(2048) CHARSET utf8
BEGIN
       DECLARE sTemp VARCHAR(2048);
       DECLARE sTempChd VARCHAR(2048);
    
       SET sTemp = '';
			 SET sTempChd =parentId;
    
       WHILE sTempChd is not null DO
         SET sTemp = concat(sTemp,',',sTempChd);
         SELECT group_concat(PERMISSION_ID) INTO sTempChd FROM permission where FIND_IN_SET(PARENT_PERMISSION_ID,sTempChd)>0;
       END WHILE;
       RETURN sTemp;
		END;;
DELIMITER ;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `cfg_parameter` VALUES ('1', 'TCL_TOMS', 'LANGUAGE', 'zh', '简体中文', '', '', null, '', null, '0');
INSERT INTO `cfg_parameter` VALUES ('3', 'TCL_TOMS', 'LANGUAGE', 'en', 'ENGLISH', '', '', null, '', null, '0');
INSERT INTO `cfg_parameter` VALUES ('24', 'TCL_PRODUCT', 'TYPE', 'LED', 'LED', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('25', 'TCL_PRODUCT', 'TYPE', 'LCD', 'LCD', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('26', 'TCL_PRODUCT', 'SIZE', '1', '32', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('27', 'TCL_PRODUCT', 'SIZE', '2', '39', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('28', 'TCL_PRODUCT', 'SIZE', '3', '42', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('29', 'TCL_PRODUCT', 'SIZE', '4', '44', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('30', 'TCL_PRODUCT', 'SIZE', '5', '50', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('31', 'TCL_PRODUCT', 'SIZE', '6', '52', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('32', 'TCL_PRODUCT', 'SIZE', '7', '55', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('33', 'TCL_PRODUCT', 'SIZE', '8', '58', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('34', 'TCL_PRODUCT', 'SIZE', '9', '60', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('35', 'TCL_PRODUCT', 'COLOR', '1', '黑', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('36', 'TCL_PRODUCT', 'COLOR', '2', '白', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('37', 'TCL_PRODUCT', 'COLOR', '3', '银', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('38', 'TCL_PRODUCT', 'COLOR', '4', '橙', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('39', 'TCL_PRODUCT', 'COLOR', '5', '黄', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('40', 'TCL_PRODUCT', 'COLOR', '6', '红', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('41', 'TCL_PRODUCT', 'COLOR', '7', '绿', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('42', 'TCL_PRODUCT', 'COLOR', '8', '蓝', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('43', 'TCL_PRODUCT', 'SCALE', '1', '4:3', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('44', 'TCL_PRODUCT', 'SCALE', '2', '16:9', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('45', 'TCL_PRODUCT', 'SCALE', '3', '1:1', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('46', 'TCL_PRODUCT', 'DISPLAY', '1', '1024*768', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('47', 'TCL_PRODUCT', 'DISPLAY', '2', '1360*768', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('48', 'TCL_PRODUCT', 'DISPLAY', '3', '4500*3800', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('49', 'TCL_PRODUCT', 'DISPLAY', '4', '3823*2235', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('50', 'TCL_PRODUCT', 'BRAND', '1', 'TCL', null, null, null, null, null, null);
INSERT INTO `cfg_parameter` VALUES ('51', 'TCL_PRODUCT', 'BRAND', '2', 'LETV', null, null, null, null, null, null);
INSERT INTO `city` VALUES ('1', '深圳市', 'Shen Zhen', '深圳', '1', '1');
INSERT INTO `country` VALUES ('1', '中国', 'China', '中国', '1');
INSERT INTO `country` VALUES ('2', '英国', 'England', null, '1');
INSERT INTO `country` VALUES ('3', '美国', 'US', null, '1');
INSERT INTO `country` VALUES ('4', '越南', 'YueNan', null, '1');
INSERT INTO `county` VALUES ('1', '南山区', 'Nan Shan District', '南山', '1', '1');
INSERT INTO `county` VALUES ('2', '龙岗区', null, null, null, null);
INSERT INTO `county` VALUES ('3', '福田区', null, null, null, null);
INSERT INTO `county` VALUES ('4', '宝安区', null, null, null, null);
INSERT INTO `county` VALUES ('5', '盐田区', null, null, null, null);
INSERT INTO `customer_info` VALUES ('1', '张三', '1', '李四', null, null, '16:9', '', '', null, '白', '', '2222', '', '', '', '1', null);
INSERT INTO `customer_info` VALUES ('2', '李四', null, '李四', null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `login_history` VALUES ('admin', '2016-04-26 00:00:00', '1', '1', '999', null);
INSERT INTO `login_history` VALUES ('admin', '2016-04-26 13:58:32', '1', '1', '999', null);
INSERT INTO `login_history` VALUES ('admin', '2016-04-26 14:03:56', '1', '1', '999', 'success');
INSERT INTO `login_history` VALUES ('aaaa', '2016-04-26 14:04:23', 'aaa', '0', null, 'Without the user!');
INSERT INTO `login_history` VALUES ('admin', '2016-04-26 14:04:35', '1', '1', '999', 'success');
INSERT INTO `login_history` VALUES ('admin', '2016-04-27 14:13:54', '1', '1', '999', 'success');
INSERT INTO `login_history` VALUES ('admin', '2016-04-27 14:39:15', '1', '1', '999', 'success');
INSERT INTO `login_history` VALUES ('admin', '2016-04-27 14:56:28', '1', '1', '999', 'success');
INSERT INTO `login_history` VALUES ('admin', '2016-04-27 15:06:27', '1', '1', '999', 'success');
INSERT INTO `login_history` VALUES ('admin', '2016-04-27 16:05:49', '1', '1', '999', 'success');
INSERT INTO `login_history` VALUES ('admin', '2016-04-27 17:15:28', '1', '1', '999', 'success');
INSERT INTO `login_history` VALUES ('admin', '2016-04-27 17:16:00', '1', '1', '999', 'success');
INSERT INTO `login_history` VALUES ('admin', '2016-04-28 08:54:53', '1', '1', '999', 'success');
INSERT INTO `login_history` VALUES ('admin', '2016-04-28 09:01:38', '1', '1', '999', 'success');
INSERT INTO `login_history` VALUES ('admin', '2016-04-28 09:16:27', '1', '1', '999', 'success');
INSERT INTO `login_history` VALUES ('admin', '2016-04-28 09:24:42', '1', '1', '999', 'success');
INSERT INTO `login_history` VALUES ('admin', '2016-04-28 11:27:18', '1', '1', '999', 'success');
INSERT INTO `login_history` VALUES ('admin', '2016-04-28 13:36:36', '1', '1', '999', 'success');
INSERT INTO `party` VALUES ('999', '总部', null, '1', null, null, null, null, null, null, 'root', null);
INSERT INTO `party` VALUES ('1', '非洲分公司', null, '1', null, null, null, null, null, null, '999', null);
INSERT INTO `party` VALUES ('2', '菲律宾分公司', null, '1', null, null, null, null, null, null, '999', null);
INSERT INTO `party` VALUES ('3', '越南分公司', null, '1', null, null, null, null, null, null, '999', null);
INSERT INTO `party` VALUES ('4', '非洲北部业务部', null, '1', null, null, null, null, null, null, '1', null);
INSERT INTO `party` VALUES ('5', '非洲南部业务部', null, '1', null, null, null, null, null, null, '1', null);
INSERT INTO `party` VALUES ('6', '非洲中部业务部', null, '1', null, null, null, null, null, null, '1', null);
INSERT INTO `party` VALUES ('7', '菲律宾北部业务部', null, '1', null, null, null, null, null, null, '2', null);
INSERT INTO `party` VALUES ('8', '菲律宾中部业务部', null, '1', null, null, null, null, null, null, '2', null);
INSERT INTO `party` VALUES ('9', '菲律宾南部业务部', null, '1', null, null, null, null, null, null, '2', null);
INSERT INTO `party` VALUES ('10', '越南东部业务部', null, '1', null, null, null, null, null, null, '3', null);
INSERT INTO `party` VALUES ('11', '越南西部业务部', null, '1', null, null, null, null, null, null, '3', null);
INSERT INTO `party` VALUES ('12', '非洲北部片区1', 'qqq', '1', null, null, 'admin', '2016-04-14', 'qqq', 'qqq', '4', '');
INSERT INTO `party` VALUES ('13', '非洲中部片区1', null, '1', null, null, null, null, null, null, '6', null);
INSERT INTO `permission` VALUES ('999', 'root', '菜单树', 'permission.labelkey.root', '10', '1', null, null, null, null);
INSERT INTO `permission` VALUES ('1', '999', '主页', 'permission.labelkey.zy', '20', '1', '', 'fa-desktop', 'tomsIndexPage.action', '');
INSERT INTO `permission` VALUES ('2', '999', '产品维护', 'permission.labelkey.cpwh', '30', '1', '', 'fa-columns', 'loadProductPage.action', '');
INSERT INTO `permission` VALUES ('3', '999', '客户维护', 'permission.labelkey.khwh', '40', '1', '', 'fa-bar-chart-o', 'loadCustomerPage.action', '');
INSERT INTO `permission` VALUES ('4', '999', '门店维护', 'permission.labelkey.mdwh', '50', '1', '', 'fa-flask', 'loadShopPage.action', '');
INSERT INTO `permission` VALUES ('5', '999', '系统管理', 'permission.labelkey.xtgl', '60', '1', '', 'fa-th-large', '', '');
INSERT INTO `permission` VALUES ('6', '5', '用户维护', 'permission.labelkey.yhwh', '6010', '1', '', 'yhwh', 'loadUserLoginPage.action', '');
INSERT INTO `permission` VALUES ('7', '5', '机构维护', 'permission.labelkey.jgwh', '6020', '1', '', 'jgwh', 'loadPartyTreePage.action', '');
INSERT INTO `permission` VALUES ('8', '5', '菜单维护', 'permission.labelkey.cdwh', '6030', '1', null, 'cdwh', 'loadPermissionTreePage.action', null);
INSERT INTO `permission` VALUES ('9', '5', '角色维护', 'permission.labelkey.jswh', '6040', '1', null, 'jswh', 'loadRoleListPage.action', null);
INSERT INTO `product` VALUES ('1', '1', '', '2016-04-26', '2016-04-26', '2016-04-26', '55寸大彩电', '', '', '', '', '', '', '', '0.00', '0', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', 'MacOS', '');
INSERT INTO `product` VALUES ('asldfk', '1', '', '2016-04-19', '2016-04-19', '2016-04-19', 'lasdjf', '', '', '', '', '', '', '白', '0.00', '0', 'LETV', '', '', '', '', '', '', 'LED', '32', '1360*768', '16:9', 'sadf', '33', '2', '11', '99', '33', 'ksfd', 'slkwe', '', '');
INSERT INTO `product` VALUES ('11', '12', '', '2016-04-26', '2016-04-26', '2016-04-26', '222222', '', '', '', '', '', '', '', '0.00', '0', 'LETV', '', '', '', '', '', '', 'LED', '39', '1360*768', '', '', '', '', '', '', '', '', '', 'win', '');
INSERT INTO `province` VALUES ('1', '广东省', 'Guang Dong', '广东', '1', '1');
INSERT INTO `province` VALUES ('2', '河北省', 'He Bei', '河北', '1', '1');
INSERT INTO `role` VALUES ('101', '总部领导', null, null, null, null);
INSERT INTO `role` VALUES ('102', '总部平台部门领导', null, null, null, null);
INSERT INTO `role` VALUES ('103', '总部平台部门员工', null, null, null, null);
INSERT INTO `role` VALUES ('201', '业务中心领导', null, null, null, null);
INSERT INTO `role` VALUES ('202', '业务中心平台部门领导', null, null, null, null);
INSERT INTO `role` VALUES ('203', '业务中心平台部门员工', null, null, null, null);
INSERT INTO `role` VALUES ('200101', '业务中心一级分支机构领导', null, null, null, null);
INSERT INTO `role` VALUES ('200102', '业务中心一级分支机构平台部门领导', null, null, null, null);
INSERT INTO `role` VALUES ('200103', '业务中心一级分支机构平台部门员工', null, null, null, null);
INSERT INTO `role` VALUES ('200104', '业务中心一级分支机构平台管理员', null, null, null, null);
INSERT INTO `role` VALUES ('200100101', '业务中心二级分支机构领导', null, null, null, null);
INSERT INTO `role` VALUES ('200100102', '业务中心二级分支机构平台部门领导', null, null, null, null);
INSERT INTO `role` VALUES ('200100103', '业务中心二级分支机构平台部门员工', null, null, null, null);
INSERT INTO `role` VALUES ('200100104', '业务中心二级分支机构业务员', null, null, null, null);
INSERT INTO `role` VALUES ('200100105', '业务中心二级分支机构督导', null, null, null, null);
INSERT INTO `role` VALUES ('200100106', '业务中心二级分支机构促销员', null, null, null, null);
INSERT INTO `role` VALUES ('100', '总部平台超级管理员', 'admin', '2016-04-14 15:58:32', 'admin', '2016-04-20 14:10:51');
INSERT INTO `role` VALUES ('TEST', '测试角色', 'admin', '2016-04-19 09:45:14', 'admin', '2016-04-26 11:43:59');
INSERT INTO `role_data_permission` VALUES ('1', 'CUSTOMER', '1');
INSERT INTO `role_data_permission` VALUES ('200100104', 'SHOP', '1');
INSERT INTO `role_data_permission` VALUES ('200100104', 'SHOP', '2');
INSERT INTO `role_data_permission` VALUES ('100', 'PARTY', '11');
INSERT INTO `role_data_permission` VALUES ('100', 'PARTY', '10');
INSERT INTO `role_data_permission` VALUES ('100', 'PARTY', '3');
INSERT INTO `role_data_permission` VALUES ('100', 'PARTY', '9');
INSERT INTO `role_data_permission` VALUES ('100', 'PARTY', '8');
INSERT INTO `role_data_permission` VALUES ('100', 'PARTY', '7');
INSERT INTO `role_data_permission` VALUES ('100', 'PARTY', '2');
INSERT INTO `role_data_permission` VALUES ('100', 'PARTY', '13');
INSERT INTO `role_data_permission` VALUES ('100', 'PARTY', '6');
INSERT INTO `role_data_permission` VALUES ('100', 'PARTY', '5');
INSERT INTO `role_data_permission` VALUES ('100', 'PARTY', '12');
INSERT INTO `role_data_permission` VALUES ('100', 'PARTY', '4');
INSERT INTO `role_data_permission` VALUES ('100', 'PARTY', '1');
INSERT INTO `role_data_permission` VALUES ('100', 'PARTY', '999');
INSERT INTO `role_permission` VALUES ('TEST', '10', 'unchecked');
INSERT INTO `role_permission` VALUES ('TEST', '9', 'unchecked');
INSERT INTO `role_permission` VALUES ('TEST', '8', 'unchecked');
INSERT INTO `role_permission` VALUES ('TEST', '7', 'unchecked');
INSERT INTO `role_permission` VALUES ('100', '999', 'indetermin');
INSERT INTO `role_permission` VALUES ('TEST', '999', 'indetermin');
INSERT INTO `role_permission` VALUES ('100', '4', 'unchecked');
INSERT INTO `role_permission` VALUES ('100', '3', 'unchecked');
INSERT INTO `role_permission` VALUES ('100', '2', 'unchecked');
INSERT INTO `role_permission` VALUES ('TEST', '6', 'unchecked');
INSERT INTO `role_permission` VALUES ('TEST', '5', 'unchecked');
INSERT INTO `role_permission` VALUES ('TEST', '4', 'checked');
INSERT INTO `role_permission` VALUES ('100', '1', 'unchecked');
INSERT INTO `role_permission` VALUES ('100', '9', 'checked');
INSERT INTO `role_permission` VALUES ('100', '8', 'checked');
INSERT INTO `role_permission` VALUES ('100', '7', 'checked');
INSERT INTO `role_permission` VALUES ('100', '6', 'checked');
INSERT INTO `role_permission` VALUES ('100', '5', 'checked');
INSERT INTO `role_permission` VALUES ('TEST', '3', 'checked');
INSERT INTO `role_permission` VALUES ('TEST', '2', 'checked');
INSERT INTO `role_permission` VALUES ('TEST', '1', 'checked');
INSERT INTO `shop_info` VALUES ('1', '马呢拉测试门店', '1', null, '2016-04-15', '2016-04-15', null, null, null, null, null, 'gundanjie 66, niubiqu,manila', 'ladk.crsting.daniel', '8433948249384', 'dsfkjas@lal.com', '113.923', '22.5723', null, null);
INSERT INTO `shop_info` VALUES ('2', '马呢拉MART门店', '2', null, null, null, null, null, null, null, null, null, null, null, null, '113.923', '22.5723', null, null);
INSERT INTO `shop_info` VALUES ('safd', 'asfsdf', '1', 'admin', '2016-04-25', null, '', '', '', '', '', '', '', '', 'wea@111.com', '0', '0', '1', '');
INSERT INTO `t_barcode` VALUES ('1', '9787545401585', 'H9800', '2016-04-19 10:36:38');
INSERT INTO `t_modelmap` VALUES ('1', 'M9800', 'H9800', '2', '2016-04-19 10:36:21');
INSERT INTO `town` VALUES ('1', '西丽', 'Xi Li', '西丽', '1', '1');
INSERT INTO `user_login` VALUES ('admin', '系统管理员', '1', '', '1', '2016-05-26', 'LOCAL', '', '2099-12-31', 'admin@tcl.com', '000000', '', '', 'en', '999');
INSERT INTO `user_login` VALUES ('aaa', 'aaaa', 'aaa', 'aaa', '1', '2016-04-23', 'LOCAL', 'admin', '2016-04-25', 'aaaa@qq.com', '', '', '', 'en', '1');
INSERT INTO `user_login` VALUES ('111', '111', '111', '111', '1', '2020-10-31', 'LDAP', 'admin', '2016-04-25', 'aaaaa@qq.com', '1111', '', '', 'zh', '999');
INSERT INTO `user_role_mapping` VALUES ('yingwh', '200100104');
INSERT INTO `user_role_mapping` VALUES ('111', 'TEST');
INSERT INTO `user_role_mapping` VALUES ('admin', '100');
INSERT INTO `user_role_mapping` VALUES ('aaa', 'TEST');
INSERT INTO `user_role_mapping` VALUES ('admin', 'TEST');
INSERT INTO `user_role_mapping` VALUES ('', '');

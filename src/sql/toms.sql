CREATE TABLE PERSON(
   PERSON_ID   VARCHAR(40) COMMENT '人员编码' NOT NULL PRIMARY KEY  ,
   LAST_NAME   VARCHAR(60)   COMMENT '姓',
   FIRST_NAME   VARCHAR(60)   COMMENT '名',
   MIDDLE_NAME   VARCHAR(60),
   PERSONAL_TITLE   VARCHAR(60)   COMMENT '职务',
   SUFFIX   VARCHAR(60),
   NICKNAME   VARCHAR(60),
   GENDER   CHAR   COMMENT '性别',
   BIRTH_DATE   DATE   COMMENT '生日',
   EDUCATION   VARCHAR(40)   COMMENT '学历',
   ENTER_DATE   DATE   COMMENT '入司日期',
   LEAVE_DATE   DATE   COMMENT '离职日期',
   STATUS   CHAR   COMMENT '状态(0为启用，1为停用)',
   MOBILE   VARCHAR(60)   COMMENT '手机号码',
   EMAIL   VARCHAR(60)   COMMENT '邮箱',
   OFFICE_PHONE   VARCHAR(60)   COMMENT '办公室电话',
   FAX   VARCHAR(60)   COMMENT '传真',
   QQ   VARCHAR(60)   COMMENT 'QQ号码',
   DEPARTMENT_ID   VARCHAR(40)   COMMENT '所属部门编码',
   PARTY_ID   VARCHAR(40)   COMMENT '归属机构'
);

CREATE TABLE USER_LOGIN(
   USER_LOGIN_ID   VARCHAR(40)    COMMENT '登陆账号' NOT NULL PRIMARY KEY,
   USER_NAME   VARCHAR(40)    COMMENT '登陆账号名',
   PERSON_ID   VARCHAR(40)    COMMENT '人员编码',
   CURRENT_PASSWORD   VARCHAR(60)   COMMENT '密码',
   PASSWORD_HINT   VARCHAR(255)   COMMENT '密码提醒',
   ENABLED   CHAR(1)   COMMENT '是否可用(1为启用，0为停用)',
   DISABLED_DATE_TIME   DATE   COMMENT '账号失效日期',
   SUCCESSIVE_FAILED_LOGINS   INT,
   PASSWORD_CHECK_TYPE   VARCHAR(40)   COMMENT '密码验证方式',
   CREATE_BY   VARCHAR(40)   COMMENT '账号创建人员',
   CREATE_DATE   DATE   COMMENT '创建日期',
   DEFAULT_ROLE_TYPE_ID   VARCHAR(40)   COMMENT '默认用户角色'
);

CREATE TABLE LOGIN_HISTORY(
   USER_LOGIN_ID   VARCHAR(40)    COMMENT '登陆账号' NOT NULL PRIMARY KEY,
   LOGIN_DATETIME   DATE   COMMENT '登陆时间',
   PASSWORD_USED   VARCHAR(60)   COMMENT '登录使用密码',
   SUCCESSFUL_LOGIN   CHAR(1)   COMMENT '是否成功登录(0为成功，1为失败)',
   PARTY_ID   VARCHAR(40)    COMMENT '人员编码'
);

CREATE TABLE ROLE_TYPE(
   ROLE_TYPE_ID   VARCHAR(40)   COMMENT '角色编码' NOT NULL PRIMARY KEY,
   PARENT_TYPE_ID   VARCHAR(40)   COMMENT '上级角色',
   DESCRIPTION   VARCHAR(255)   COMMENT '角色描述'
);

CREATE TABLE ROLE_GROUP(
   ROLE_GROUP_ID   VARCHAR(40)   COMMENT '角色组编码' NOT NULL PRIMARY KEY,
   DESCRIPTION   VARCHAR(255)   COMMENT '角色描述'
);

CREATE TABLE ROLE_TYPE_GROUP(
   ROLE_GROUP_ID   VARCHAR(40)   COMMENT '角色组编码' NOT NULL PRIMARY KEY,
   ROLE_TYPE_ID   VARCHAR(40)   COMMENT '角色编码'
);

CREATE TABLE PERMISSION(
   PERMISSION_ID   VARCHAR(40)   COMMENT '权限点编号' NOT NULL PRIMARY KEY,
   PARENT_PERMISSION_ID   VARCHAR(40)   COMMENT '上级权限',
   PERMISSION_NAME   VARCHAR(255)   COMMENT '权限名称',
   IS_MENU   INTEGER   COMMENT '是否菜单(1为菜单，0为非菜单)',
   COMMENTS   VARCHAR(255)   COMMENT '备注',
   PERMISSION_CODE   VARCHAR(40)   COMMENT '权限点简码',
   PERMISSION_URL   VARCHAR(255)   COMMENT '该权限点对应的URL'
);

CREATE TABLE ROLE_GROUP_PERMISSION(
   ROLE_GROUP_ID   VARCHAR(40)   COMMENT '角色组编码' NOT NULL PRIMARY KEY,
   PERMISSION_ID   VARCHAR(40)   COMMENT '权限点编号'
);

CREATE TABLE ROLE_GROUP_PARTY(
   ROLE_GROUP_ID   VARCHAR(40)   COMMENT '角色组编码' NOT NULL PRIMARY KEY,
   PARTY_ID   VARCHAR(40)   COMMENT '法人机构编码'
);

CREATE TABLE PARTY(
   PARTY_ID   VARCHAR(40)   COMMENT '组织编码' NOT NULL PRIMARY KEY,
   PARTY_NAME   VARCHAR(60)   COMMENT '组织名称',
   PARTY_NAME_EN   VARCHAR(60)   COMMENT '组织名称_英语',
   PARTY_NAME_LOCAL   VARCHAR(60)   COMMENT '组织名称_本地',
   FEDERAL_TAX_ID   VARCHAR(60)   COMMENT '税务号',
   COMMENTS   VARCHAR(255)   COMMENT '备注',
   STATUS   CHAR(1)   COMMENT '状态(1为启用，0为停用)',
   CREATE_DATE   DATE   COMMENT '创建日期',
   CREATE_BY   VARCHAR(40)   COMMENT '创建人员',
   LAST_MODIFY_DATE   DATE   COMMENT '最近更新日期',
   GROUP_NAME_ABBR   VARCHAR(60)   COMMENT '组织简称',
   PARTY_ID_LAYER   VARCHAR(40)   COMMENT '归属法人机构编码',
   PARENT_PARTY_ID   VARCHAR(40)   COMMENT '上级机构编码',
   CONTINENT_ID   VARCHAR(40)   COMMENT '洲',
   COUNTRY_ID   VARCHAR(40)   COMMENT '国家',
   PROVINCE_ID   VARCHAR(40)   COMMENT '省',
   CITY_ID   VARCHAR(40)   COMMENT '市',
   COUNTY_ID   VARCHAR(40)   COMMENT '县',
   TOWN_ID   VARCHAR(40)   COMMENT '镇',
   DETAIL_ADDRESS   VARCHAR(200)   COMMENT '详细地址'    
);


CREATE TABLE USER_PARTY_ROLE_GROUP(
   USER_ROLE_GROUP_ID   INT   COMMENT '序号' NOT NULL PRIMARY KEY,
   USER_LOGIN_ID   VARCHAR(40)   COMMENT '登录账户',
   ROLE_GROUP_ID   VARCHAR(60)   COMMENT '角色组名称',
   PARTY_ID   VARCHAR(40)
);

CREATE TABLE PARTY_ATTRIBUTE(
   PARTY_ID   VARCHAR(40)   COMMENT '客户编码' NOT NULL PRIMARY KEY,
   ATTR_NAME   VARCHAR(60)   COMMENT '属性',
   ATTR_VALUE   VARCHAR(255)   COMMENT '属性值'
);

CREATE TABLE CONTINENTS(
   CONTINENT_ID   VARCHAR(40)   COMMENT '洲编码' NOT NULL PRIMARY KEY,
   CONTINENT_NAME   VARCHAR(40)   COMMENT '洲名',
   STATUS   CHAR(1)   COMMENT '状态，1为启用，0为停用'
);


CREATE TABLE COUNTRY(
   COUNTRY_ID   VARCHAR(40)   COMMENT '国家编码' NOT NULL PRIMARY KEY,
   COUNTRY   VARCHAR(40)   COMMENT '国家(中文)',
   COUNTRY_EN   VARCHAR(40)   COMMENT '国家(英文)',
   COUNTRY_LOCAL   VARCHAR(40)   COMMENT '国家(当地方案)',
   CONTINENT_ID   VARCHAR(40)   COMMENT '洲编码',
   STATUS   CHAR(1)   COMMENT '状态，1为启用，0为停用'
);

CREATE TABLE PROVINCE(
   PROVINCE_ID   VARCHAR(40)   COMMENT '省编码' NOT NULL PRIMARY KEY,
   PROVINCE   VARCHAR(40)   COMMENT '省',
   PROVINCE_EN   VARCHAR(40)   COMMENT '省_英文',
   PROVINCE_LOCAL   VARCHAR(100)   COMMENT '省_当地语言',
   COUNTRY_ID   VARCHAR(40)   COMMENT '国家编码',
   STATUS   CHAR(1)   COMMENT '状态，1为启用，0为停用'
);

CREATE TABLE CITY(
   CITY_ID   VARCHAR(40)   COMMENT '市编码' NOT NULL PRIMARY KEY,
   CITY   VARCHAR(40)   COMMENT '市',
   CITY_EN   VARCHAR(40)   COMMENT '市_英文',
   CITY_LOCAL   VARCHAR(40)   COMMENT '市_当地方言',
   PROVINCE_ID   VARCHAR(40)   COMMENT '省编码',
   STATUS   CHAR(1)   COMMENT '状态，1为启用，0为停用'
);

CREATE TABLE COUNTY(
   COUNTY_ID   VARCHAR(40)   COMMENT '县编码' NOT NULL PRIMARY KEY,
   COUNTY   VARCHAR(40)   COMMENT '县',
   COUNTY_EN   VARCHAR(40)   COMMENT '县(英文)',
   COUNTY_LOCAL   VARCHAR(40)   COMMENT '县(当地)',
   CITY_ID   VARCHAR(40)   COMMENT '市编码',
   STATUS   CHAR(1)   COMMENT '状态，1为启用，0为停用'
);

CREATE TABLE TOWN(
   TOWN_ID   VARCHAR(40)   COMMENT '镇编码' NOT NULL PRIMARY KEY,
   TOWN   VARCHAR(40)   COMMENT '镇',
   TOWN_EN   VARCHAR(40)   COMMENT '镇_英文',
   TOWN_LOCAL   VARCHAR(40)   COMMENT '镇_当地方言',
   COUNTY_ID   VARCHAR(40)   COMMENT '县编码',
   STATUS   CHAR(1)   COMMENT '状态，1为启用，0为停用'
);

CREATE TABLE MARKET_LEVEL(
   MARKET_LEVEL_ID   VARCHAR(40)   COMMENT '市场等级编码' NOT NULL PRIMARY KEY,
   MARKET_LEVEL   VARCHAR(40)   COMMENT '市场等级编码',
   COUNTRY_ID   VARCHAR(40)   COMMENT '国家编码',
   STATUS   CHAR(1)   COMMENT '状态，1为启用，0为停用',
   ORDER_BY   INT   COMMENT '排序号',
   COMMENTS   VARCHAR(100)   COMMENT '备注'
);

CREATE TABLE CUSTOMER_INFO(
   CUSTOMER_ID   VARCHAR(40)   COMMENT '客户编码' NOT NULL PRIMARY KEY,
   CUSTOMER_NAME   VARCHAR(200)   COMMENT '客户名称',
   CUSTOMER_NAME_NAME   VARCHAR(200)   COMMENT '客户英语名称',
   CUSTOMER_NAME_LOCAL   VARCHAR(200)   COMMENT '客户当地语言名称',
   PARTY_ID   VARCHAR(40)   COMMENT '归属机构编码',
   CREATE_BY   VARCHAR(40)   COMMENT '创建人员',
   CREATE_DATE   DATE   COMMENT '新建日期',
   ENTER_DATE   DATE   COMMENT 'TCL品牌入驻日期',
   CUSTOMER_TYPE_ID   VARCHAR(40)   COMMENT '客户类型',
   CHANNEL_ID   VARCHAR(40)   COMMENT '客户归属渠道',
   MARKET_LEVEL_ID   VARCHAR(40)   COMMENT '市场等级',
   CONTINENT_ID   VARCHAR(40)   COMMENT '洲',
   COUNTRY_ID   VARCHAR(40)   COMMENT '国家',
   PROVINCE_ID   VARCHAR(40)   COMMENT '省',
   CITY_ID   VARCHAR(40)   COMMENT '市',
   COUNTY_ID   VARCHAR(40)   COMMENT '县',
   TOWN_ID   VARCHAR(40)   COMMENT '镇',
   DETAIL_ADDRESS   VARCHAR(200)   COMMENT '详细地址',
   CONTACT_NAME   VARCHAR(40)   COMMENT '联系人姓名',
   PHONE   VARCHAR(40)   COMMENT '联系人电话',
   EMAIL   VARCHAR(100),
   WEBSITE   VARCHAR(400)   COMMENT '客户网站',
   REGION_ID   VARCHAR(40)   COMMENT '片区',
   STATUS   CHAR(1)   COMMENT '状态，1为启用，0为停用,2待审批',
   COMMENTS   VARCHAR(100)   COMMENT '备注',
   USER_DEFINE_1   VARCHAR(200)   COMMENT '自定义字段1',
   USER_DEFINE_2   VARCHAR(200)   COMMENT '自定义字段2',
   USER_DEFINE_3   VARCHAR(200)   COMMENT '自定义字段3',
   USER_DEFINE_4   VARCHAR(200)   COMMENT '自定义字段4',
   USER_DEFINE_5   VARCHAR(200)   COMMENT '自定义字段5',
   USER_DEFINE_6   VARCHAR(200)   COMMENT '自定义字段6',
   USER_DEFINE_7   VARCHAR(200)   COMMENT '自定义字段7',
   USER_DEFINE_8   VARCHAR(200)   COMMENT '自定义字段8',
   USER_DEFINE_9   VARCHAR(200)   COMMENT '自定义字段9',
   USER_DEFINE_10   VARCHAR(200)   COMMENT '自定义字段10'
);

CREATE TABLE CUSTOMER_TYPE(
   CUSTOMER_TYPE_ID   VARCHAR(40)   COMMENT '客户类型编码' NOT NULL PRIMARY KEY,
   CUSTOMER_TYPE   VARCHAR(40)   COMMENT '客户类型描述',
   COUNTRY_ID   VARCHAR(40)   COMMENT '国家编码',
   STATUS   CHAR(1)   COMMENT '状态，1为启用，0为停用',
   ORDER_BY   INT   COMMENT '排序号',
   COMMENTS   VARCHAR(100)   COMMENT '备注'
);

CREATE TABLE CHANNEL_TYPE(
   CHANNEL_TYPE_ID   VARCHAR(40)   COMMENT '渠道类型编码' NOT NULL PRIMARY KEY,
   CHANNEL_TYPE   VARCHAR(40)   COMMENT '渠道类型描述',
   COUNTRY_ID   VARCHAR(40)   COMMENT '国家编码',
   STATUS   CHAR(1)   COMMENT '状态，1为启用，0为停用',
   ORDER_BY   INT   COMMENT '排序号',
   COMMENTS   VARCHAR(100)   COMMENT '备注'
);

CREATE TABLE CUSTOMER_ATTRIBUTE(
   CUSTOMER_ID   VARCHAR(40)   COMMENT '客户编码' NOT NULL PRIMARY KEY,
   ATTR_NAME   VARCHAR(60)   COMMENT '属性',
   ATTR_VALUE   VARCHAR(255)   COMMENT '属性值'
);

CREATE TABLE SHOP_INFO(
   SHOP_ID   VARCHAR(40)   COMMENT '门店编码' NOT NULL PRIMARY KEY,
   SHOP_NAME   VARCHAR(255)   COMMENT '门店名称',
   SHOP_NAME_EN   VARCHAR(255)   COMMENT '门店名称—英语',
   SHOP_NAME_LOCAL   VARCHAR(255)   COMMENT '门店名称-当地语言',
   CUSTOMER_ID   VARCHAR(40)   COMMENT '客户编码',
   CREATE_BY   VARCHAR(40)   COMMENT '创建人员',
   CREATE_DATE   DATE   COMMENT '新建日期',
   ENTER_DATE   DATE   COMMENT 'TCL品牌入驻日期',
   SHOP_LEVEL_ID   VARCHAR(40)   COMMENT '门店类型',
   CHANNEL_ID   VARCHAR(40)   COMMENT '门店归属渠道',
   MARKET_LEVEL_ID   VARCHAR(40)   COMMENT '市场等级',
   CONTINENT_ID   VARCHAR(40)   COMMENT '洲',
   COUNTRY_ID   VARCHAR(40)   COMMENT '国家',
   PROVINCE_ID   VARCHAR(40)   COMMENT '省',
   CITY_ID   VARCHAR(40)   COMMENT '市',
   COUNTY_ID   VARCHAR(40)   COMMENT '县',
   TOWN_ID   VARCHAR(40)   COMMENT '镇',
   DETAIL_ADDRESS   VARCHAR(200)   COMMENT '详细地址',
   CONTACT_NAME   VARCHAR(40)   COMMENT '联系人姓名',
   PHONE   VARCHAR(40)   COMMENT '联系人电话',
   EMAIL   VARCHAR(100),
   LNG   INT   COMMENT '经度',
   LAT   INT   COMMENT '维度',
   REGION_ID   VARCHAR(40)   COMMENT '片区',
   STATUS   CHAR(1)   COMMENT '状态，1为启用，0为停用,2待审批',
   COMMENTS   VARCHAR(100)   COMMENT '备注',
   USER_DEFINE_1   VARCHAR(200)   COMMENT '自定义字段1',
   USER_DEFINE_2   VARCHAR(200)   COMMENT '自定义字段2',
   USER_DEFINE_3   VARCHAR(200)   COMMENT '自定义字段3',
   USER_DEFINE_4   VARCHAR(200)   COMMENT '自定义字段4',
   USER_DEFINE_5   VARCHAR(200)   COMMENT '自定义字段5',
   USER_DEFINE_6   VARCHAR(200)   COMMENT '自定义字段6',
   USER_DEFINE_7   VARCHAR(200)   COMMENT '自定义字段7',
   USER_DEFINE_8   VARCHAR(200)   COMMENT '自定义字段8',
   USER_DEFINE_9   VARCHAR(200)   COMMENT '自定义字段9',
   USER_DEFINE_10   VARCHAR(200)   COMMENT '自定义字段10'
);

CREATE TABLE SHOP_LEVEL(
   SHOP_LEVEL_ID   VARCHAR(40)   COMMENT '门店类别编码' NOT NULL PRIMARY KEY,
   SHOP_LEVEL   VARCHAR(40)   COMMENT '门店类别编码',
   COUNTRY_ID   VARCHAR(40)   COMMENT '国家编码',
   STATUS   CHAR(1)   COMMENT '状态，1为启用，0为停用',
   ORDER_BY   INT   COMMENT '排序号',
   COMMENTS   VARCHAR(100)   COMMENT '备注'
);

CREATE TABLE USER_PARTY(
   USER_PARTY_ID   INT   COMMENT '序号' NOT NULL PRIMARY KEY,
   USER_LOGIN_ID   VARCHAR(40)   COMMENT '登录账户',
   PARTY_ID   VARCHAR(40)   COMMENT '机构编码',
   COMMENTS   VARCHAR(100)   COMMENT '备注'
);

CREATE TABLE USER_PARTY_ROLE(
   USER_PARTY_ID   INT   COMMENT '序号' NOT NULL PRIMARY KEY,
   USER_LOGIN_ID   VARCHAR(40)   COMMENT '登录账户',
   PARTY_ID   VARCHAR(40)   COMMENT '机构编码',
   ROLE_GROUP_ID   VARCHAR(40)   COMMENT '角色组编码',
   COMMENTS   VARCHAR(100)   COMMENT '备注'
);

CREATE TABLE REGION_INFO(
   REGION_ID   INT   COMMENT '片区编码' NOT NULL PRIMARY KEY,
   PARTY_ID   VARCHAR(40)   COMMENT '归属机构',
   REGION_NAME   VARCHAR(500)   COMMENT '片区名称',
   COMMENTS   VARCHAR(500)   COMMENT '描述',
   STATUS   CHAR(1)   COMMENT '状态(1为启用，0为停用)'
);

CREATE TABLE CUSTOMER_TYPE_PARTY(
   CUSTOMER_TYPE_PARTY_ID   INT   COMMENT '主键序号' NOT NULL PRIMARY KEY,
   PARTY_ID   VARCHAR(40)   COMMENT '机构编码（法人）',
   CUSTOMER_TYPE_ID   VARCHAR(40)   COMMENT '客户类型编码'
);

CREATE TABLE CHANNEL_TYPE_PARTY(
   CHANNEL_TYPE_PARTY_ID   INT   COMMENT '主键序号' NOT NULL PRIMARY KEY,
   PARTY_ID   VARCHAR(40)   COMMENT '机构编码（法人）',
   CHANNEL_TYPE_ID   VARCHAR(40)   COMMENT '渠道类型编码'
);

CREATE TABLE CUSTOMER_SALESMAN_REL(
   CUS_REL_ID   INT   COMMENT '主键序号' NOT NULL PRIMARY KEY,
   CUSTOMER_ID   VARCHAR(40)   COMMENT '客户编码',
   PERSON_ID   VARCHAR(40)   COMMENT '人员编码',
   FROM_DATE   DATE   COMMENT '开始日期',
   END_DATE   DATE   COMMENT '结束日期（如果业务员在管辖此客户，那么此字段为空）',
   IS_DEFAULT   CHAR(1)   COMMENT '是否默认（Y是，N否）'
);

CREATE TABLE SHOP_SALESMAN_REL(
   SHOP_REL_ID   INT   COMMENT '主键序号' NOT NULL PRIMARY KEY,
   SHOP_ID   VARCHAR(40)   COMMENT '客户编码',
   PERSON_ID   VARCHAR(40)   COMMENT '人员编码',
   FROM_DATE   DATE   COMMENT '开始日期',
   END_DATE   DATE   COMMENT '结束日期（如果业务员在管辖此客户，那么此字段为空）',
   IS_DEFAULT   CHAR(1)   COMMENT '是否默认（Y是，N否）'
);

CREATE TABLE WX_DEPARTMENT(
   WX_DEP_ID   VARCHAR(40)   COMMENT '机构编码' NOT NULL PRIMARY KEY,
   DEP_NAME   VARCHAR(40)   COMMENT '机构名称',
   PARTY_ID   VARCHAR(40)   COMMENT '对应组织机构编码'
);

CREATE TABLE WX_USER_INFO(
   WX_USER_ID   VARCHAR(40)   COMMENT '微信用户ID' NOT NULL PRIMARY KEY,
   WX_ID   VARCHAR(40)   COMMENT '微信号',
   DEP_IDS   VARCHAR(200)   COMMENT '部门ID群',
   FULL_NAME   VARCHAR(40)   COMMENT '姓名',
   PHONE   VARCHAR(40),
   POSISION   VARCHAR(40)   COMMENT '职位',
   EMAIL   VARCHAR(40)
);

CREATE TABLE PRODUCT(
   PRODUCT_ID   VARCHAR(40)   COMMENT '产品编号' not null primary key,
   PRODUCT_TYPE_ID   VARCHAR(40)   COMMENT '产品类型',
   INTRODUCTION_DATE   DATE   COMMENT '上市日期',
   SALES_DISCONTINUATION_DATE   DATE   COMMENT '销售退市日期',
   SUPPORT_DISCONTINUATION_DATE   DATE   COMMENT '售后支持截止日期',
   PRODUCT_NAME   VARCHAR(200)   COMMENT '产品名称',
   MANUFACTURER_PARTY_ID   VARCHAR(40)   COMMENT '制造商|事业部',
   INTERNAL_NAME   VARCHAR(60)   COMMENT '内部名称',
   COMMENTS   VARCHAR(255)   COMMENT '备注',
   DESCRIPTION   VARCHAR(255)   COMMENT '产品描述',
   DESCRIPTION_EN   VARCHAR(255)   COMMENT '产品描述－英文',
   QUANTITY_UOM_ID   VARCHAR(40)   COMMENT '数量单位编号',
   COLOR_ID   VARCHAR(10)   COMMENT '颜色编号',
   VOLUME   DOUBLE (5,2)   COMMENT '体积',
   STATUS   CHAR(1)   COMMENT '状态:1生效 0停用',
   BRAND_ID   VARCHAR(40)   COMMENT '品牌ID',
   CATEGORY_ID   VARCHAR(40)   COMMENT '大类ID',
   PRODUCT_SPEC_ID   VARCHAR(40)   COMMENT '小类1:规格编号',
   PRODUCT_FUNC_ID   VARCHAR(40)   COMMENT '小类2:功能编号',
   PRODUCT_SCREEN_ID   VARCHAR(40)   COMMENT '小类3:屏幕编号',
   MODEL_NAME   VARCHAR(40)   COMMENT '型号名称'
);

CREATE TABLE PRODUCT_TYPE(
   PRODUCT_TYPE_ID   VARCHAR(40)   COMMENT '产品类型编码' not null primary key,
   DESCRIPTION   VARCHAR(255)   COMMENT '描述',
   STATUS   CHAR(1)   COMMENT '状态：1为启用，0为停用'
);


CREATE TABLE PRODUCT_ATTRIBUTE(
   PRODUCT_ID   VARCHAR(40)   COMMENT '产品编码' not null primary key,
   ATTR_NAME   VARCHAR(60)   COMMENT '产品属性名称',
   ATTR_VALUE   VARCHAR(255)   COMMENT '产品属性值'
);



CREATE TABLE PRODUCT_BRAND(
   BRAND_ID   VARCHAR(40)   COMMENT '品牌编码' not null primary key,
   BRAND_NAME   VARCHAR(60)   COMMENT '品牌名称',
   STATUS   CHAR(1)   COMMENT '状态（0为启用，1为停用)',
   TYPE   CHAR(1)   COMMENT '门店覆盖监测品牌，Y表示监测品牌',
   CHANNEL_TYPE   CHAR(1)   COMMENT '门店零售份额监测品牌：Y表示监测品牌'
);

CREATE TABLE PRODUCT_GRADE(
   GRADE_ID   VARCHAR(10)   COMMENT '产品等级编码' not null primary key,
   GRADE_NAME   VARCHAR(40)   COMMENT '产品等级描述'
);

CREATE TABLE PRODUCT_COLOR(
   COLOR_ID   VARCHAR(10)   COMMENT '颜色编码' not null primary key,
   COLOR_NAME   VARCHAR(40)   COMMENT '颜色描述',
   STATUS   CHAR(1)   COMMENT '状态'
);

CREATE TABLE PRODUCT_FAMILY(
   PRODUCT_FAMILY_ID   VARCHAR(20)   COMMENT '产品线编码' not null primary key,
   PRODUCT_FAMILY_NAME   VARCHAR(40)   COMMENT '产品线描述',
   BRAND_ID   VARCHAR(40)   COMMENT '品牌编码',
   TYPE_ID   VARCHAR(40)   COMMENT '产品大类'
);

CREATE TABLE PRODUCT_CATEGORY(
   PRODUCT_CATEGORY_ID   VARCHAR(40)   COMMENT '大类编号' not null primary key,
   PRODUCT_CATEGORY_NAME   VARCHAR(60)   COMMENT '大类名称',
   STATUS   CHAR(1)   COMMENT '状态（0为启用，1为停用)'
);

CREATE TABLE PRODUCT_FUNCTION(
   FUNC_ID   VARCHAR(40)   COMMENT '功能编号' not null primary key,
   FUNC_NAME   VARCHAR(60)   COMMENT '功能名称',
   PRODUCT_CATEGORY_ID   VARCHAR(40)   COMMENT '大类编号',
   STATUS   CHAR(1)   COMMENT '状态（1为启用，0为停用)'
);

CREATE TABLE PRODUCT_SPEC(
   SPEC_ID   VARCHAR(40)   COMMENT '规格编号' not null primary key,
   SPEC_NAME   VARCHAR(60)   COMMENT '规格名称',
   PRODUCT_CATEGORY_ID   VARCHAR(40)   COMMENT '大类编号',
   STATUS   CHAR(1)   COMMENT '状态（1为启用，0为停用)'
);

CREATE TABLE PRODUCT_SCREEN(
   SCREEN_ID   VARCHAR(40)   COMMENT '屏幕ID' not null primary key,
   SCREEN_NAME   VARCHAR(60)   COMMENT '屏幕分类名称',
   PRODUCT_CATEGORY_ID   VARCHAR(40)   COMMENT '大类编号',
   STATUS   CHAR(1)   COMMENT '状态（1为启用，0为停用)'
);

CREATE TABLE PRODUCT_FAMILY_DETAIL(
   PRODUCT_FAMILY_ID   VARCHAR(20)   COMMENT '产品线编码' not null primary key,
   BRAND_ID   VARCHAR(40)   COMMENT '品牌编码',
   CATEGORY_ID   VARCHAR(40)   COMMENT '产品大类编号'
);

CREATE TABLE UOM(
   UOM_ID   VARCHAR(40) not null primary key,
   UOM_TYPE_ID   VARCHAR(40),
   ABBREVIATION   VARCHAR(60),
   DESCRIPTION   VARCHAR(255)
);
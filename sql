-- Create table
create table PERSON_EXTEND
(
  psn_code        NUMBER(18) not null,
  psn_verson      VARCHAR2(17),
  psn_xml         SYS.XMLTYPE,
  update_time     DATE,
  update_psn_code NUMBER(18)
);
-- Add comments to the table
comment on table PERSON_EXTEND
  is '人员信息扩展表';
-- Add comments to the columns
comment on column PERSON_EXTEND.psn_code
  is '人员code';
comment on column PERSON_EXTEND.psn_verson
  is '人员更新版本号';
comment on column PERSON_EXTEND.psn_xml
  is '人员xml';
comment on column PERSON_EXTEND.update_time
  is '最新更新时间';
comment on column PERSON_EXTEND.update_psn_code
  is '更新人code';
-- Create/Recreate primary, unique and foreign key constraints
alter table PERSON_EXTEND
  add constraint PK_PERSON_EXTEND_PSN_CODE primary key (PSN_CODE)
  using index;


  -- Create table
create table PERSON_TMP
(
  psn_code   NUMBER(18) not null,
  org_code   NUMBER(18),
  zh_name    VARCHAR2(40 CHAR),
  card_type  NUMBER(1),
  card_code  VARCHAR2(100 CHAR),
  mobile     VARCHAR2(40 CHAR),
  tel        VARCHAR2(40 CHAR),
  fax        VARCHAR2(40 CHAR),
  email      VARCHAR2(100 CHAR),
  position   VARCHAR2(100 CHAR),
  login_name VARCHAR2(100 CHAR),
  gender     CHAR(1),
  role       NUMBER(18),
  is_import  CHAR(1) default 0,
  status     CHAR(1),
  password   VARCHAR2(200 CHAR)
);

insert into person_extend (PSN_CODE, PSN_VERSON, PSN_XML, UPDATE_TIME, UPDATE_PSN_CODE)
values (333, '2', '<?xml version="1.0" encoding="UTF-8"?><data><basic_info><psn_code>100018476</psn_code><zh_name>添加11</zh_name><en_name/><gender_value/><other_name/><card_type_value> </card_type_value><card_type_name> </card_type_name><card_code/><ethnicity_value/><ethnicity_name/><birthday/><org_name/><org_code/><department/><position/><prof_title_value/><prof_title_name/><educate_value/><educate_name/><degree_value/><degree_name/><degree_country_value/><degree_country_name/><degree_year/><graduate_college/><degree_college/><doctoral_value/><doctoral_name/><academician_value/><academician_name/><receive_title_value/><receive_title_name/><post_code/><http/><address/><bank_name/><bank_orgname/><bank_account/><work_experience/><working_performance/><allowance/><honorary_title/><social_appointments/><other/></basic_info><content><file_code8/></content><valid><valid_card/></valid><contact><major/><major_work/><email>abcncc1@123.com</email><tel_work/><tel_home/><mobile>123456xxxxx</mobile><fax/></contact><researches><research seq_no="1"><zh_keywords><keyword seq_no="1">1</keyword><keyword seq_no="2"/><keyword seq_no="3"/><keyword seq_no="4"/><keyword seq_no="5"/></zh_keywords><subject_code>120</subject_code><en_keywords><keyword seq_no="1"/><keyword seq_no="2"/><keyword seq_no="3"/><keyword seq_no="4"/><keyword seq_no="5"/></en_keywords><subject_name>信息科学与系统科学</subject_name><research_area_code/><research_area_name/></research><research seq_no="2"><zh_keywords><keyword seq_no="1">1</keyword><keyword seq_no="2"/><keyword seq_no="3"/><keyword seq_no="4"/><keyword seq_no="5"/></zh_keywords><subject_code>130</subject_code><en_keywords><keyword seq_no="1"/><keyword seq_no="2"/><keyword seq_no="3"/><keyword seq_no="4"/><keyword seq_no="5"/></en_keywords><subject_name>力学</subject_name><research_area_code/><research_area_name/></research><research seq_no="3"><subject_code/><subject_name/><research_area_code/><research_area_name/></research><research seq_no="4"><subject_code/><subject_name/><research_area_code/><research_area_name/></research><research seq_no="5"><subject_code/><subject_name/><research_area_code/><research_area_name/></research></researches><disciplines><discipline seq_no="1"><zh_keywords><keyword seq_no="1">1</keyword><keyword seq_no="2"/><keyword seq_no="3"/><keyword seq_no="4"/><keyword seq_no="5"/></zh_keywords><discipline_code>B</discipline_code><en_keywords><keyword seq_no="1"/><keyword seq_no="2"/><keyword seq_no="3"/><keyword seq_no="4"/><keyword seq_no="5"/></en_keywords><discipline_name>化学科学</discipline_name></discipline><discipline seq_no="2"><discipline_code/><discipline_name/></discipline><discipline seq_no="3"><discipline_code/><discipline_name/></discipline><discipline seq_no="4"><discipline_code/><discipline_name/></discipline><discipline seq_no="5"><discipline_code/><discipline_name/></discipline></disciplines></data>', null, 444);

insert into person_extend (PSN_CODE, PSN_VERSON, PSN_XML, UPDATE_TIME, UPDATE_PSN_CODE)
values (444, '321', '<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<data>
<basic_info>
<psn_code>100185376</psn_code>
<zh_name>湖南申报人yb</zh_name>
<en_name/>
<gender_value>M</gender_value>
<region_value>398</region_value>
<region_name>哈萨克斯坦</region_name>
<card_type_value>2</card_type_value>
<card_type_name>其他</card_type_name>
<card_code>511702197404273798</card_code>
<birthday>1972-04-10</birthday>
<age>46</age>
<org_name>吕杨企业湖南1</org_name>
<org_code>10031229</org_code>
<department>无</department>
<org_type2_value>01</org_type2_value>
<org_type2_name>政府机关</org_type2_name>
<position_value>02</position_value>
<position_name>副董事长</position_name>
<zc_level_value>02</zc_level_value>
<zc_level_name>副高级</zc_level_name>
<working_property_value>02</working_property_value>
<working_property_name>开发</working_property_name>
<educate_value>1119</educate_value>
<educate_name>大专以下</educate_name>
<degree_value>D</degree_value>
<degree_name>其他</degree_name>
<study_value>02</study_value>
<study_name>否</study_name>
<hezuoprj_value>02</hezuoprj_value>
<hezuoprj_name>否</hezuoprj_name>
<study_country/>
<sx_language/>
<receive_country_value>06</receive_country_value>
<receive_country_name>国家杰出青年科学基金获得者</receive_country_name>
<receive_province_value>03</receive_province_value>
<receive_province_name>中科院“百人计划”人选</receive_province_name>
<receive_other_value>02</receive_other_value>
<receive_other_name>国家级重点实验室（工程技术研究中心）带头人</receive_other_name>
<org_city_value>430100</org_city_value>
<org_city_name/>
<expert_type_value>01</expert_type_value>
<expert_type_name>战略</expert_type_name>
<yj_area>研究方向1；研究方向2</yj_area>
<prjreview_value>01</prjreview_value>
<prjreview_name/>
<bank_name/>
<bank_account/>
<bank_account_name/>
<subject_code>110.24</subject_code>
<subject_name>数学-代数几何学</subject_name>
<discipline_code>A06</discipline_code>
<discipline_name>A06.NSFC-中物院联合基金</discipline_name>
<personal_profile/>
<old_name/>
<other_name>11</other_name>
<native_heath/>
<place_origin/>
<ethnicity_value>98</ethnicity_value>
<ethnicity_name>外国血统</ethnicity_name>
<political_outlook_value/>
<political_outlook_name/>
<position_level/>
<position>行政职务yb</position>
<prof_title_value>011</prof_title_value>
<prof_title_name>教授</prof_title_name>
<degree_country_value>398</degree_country_value>
<degree_country_name>哈萨克斯坦</degree_country_name>
<degree_year/>
<graduate_college>11111</graduate_college>
<degree_college>111</degree_college>
<doctoral_value/>
<doctoral_name/>
<yuanshi_value/>
<yuanshi_name/>
<receive_title_value/>
<receive_title_name/>
<http>11111111111</http>
<address>申报人通讯地址yb</address>
<post_code>415501</post_code>
<specialty1/>
<specialty2/>
<work_experience/>
<roleflag3>true</roleflag3>
<roleflag5>false</roleflag5>
<roleflag27>false</roleflag27>
<first_name/>
<last_name/>
<dep_college/>
<dep_college_value/>
<academician_value>C</academician_value>
<academician_name>外籍院士</academician_name>
<post_doctor_value>T</post_doctor_value>
<post_doctor_name>在站</post_doctor_name>
<teaching_master/>
<is_committeeman/>
<education_committee/>
<studying_prono1/>
<studying_prono2/>
<studying_prono3/>
</basic_info>
<content>
<file_code8/>
<content seq_no="101843">
<name>湖南省科技经费管理与使用调查问卷</name>
<check_describe>湖南省科技经费管理与使用调查问卷</check_describe>
<attach_template>
<template_file_code>035bdbfe6933449db9ed11e4ce3b9102</template_file_code>
<template_name>20160707湖南省科技经费管理与使用调查问卷（高校、科研院所）</template_name>
<template_img>/egrantres/images/ico_word.gif</template_img>
</attach_template>
<berif>否</berif>
<berif_his/>
<file_name_his/>
<file_date_his/>
<file_code_his/>
<file_code_rehis/>
<file_code_pre/>
<file_name_pre/>
<file_date_pre/>
<file_name/>
<file_date/>
<type>0</type>
<file_code/>
<is_split/>
</content>
</content>
<valid>
<valid_card>1</valid_card>
</valid>
<contact>
<email>23@qq.com</email>
<mobile>15230214565</mobile>
<major>所学专业yb</major>
<major_work>从事专业yb</major_work>
<tel_work>07363415212121212</tel_work>
<tel_home/>
<fax>1111111</fax>
<bak_email/>
<qq/>
</contact>
<researches>
<research seq_no="1">
<zh_keywords>
<keyword seq_no="1">中文关键词1</keyword>
<keyword seq_no="2">中文关键词2</keyword>
<keyword seq_no="3">中文关键词3</keyword>
<keyword seq_no="4">中文关键词4</keyword>
<keyword seq_no="5">中文关键词5</keyword>
</zh_keywords>
<subject_code>130.301</subject_code>
<en_keywords>
<keyword seq_no="1"/>
<keyword seq_no="2"/>
<keyword seq_no="3"/>
<keyword seq_no="4"/>
<keyword seq_no="5"/>
</en_keywords>
<subject_name>流变学</subject_name>
<research_area_code/>
<research_area_name/>
<familiar_value>02</familiar_value>
<familiar_name>一般</familiar_name>
<interest_value>01</interest_value>
<interest_name>1</interest_name>
</research>
<research seq_no="2">
<subject_code/>
<subject_name/>
<research_area_code/>
<research_area_name/>
<familiar_value/>
<familiar_name/>
<interest_value/>
<interest_name/>
<zh_keywords>
<keyword seq_no="1"/>
<keyword seq_no="2"/>
<keyword seq_no="3"/>
<keyword seq_no="4"/>
<keyword seq_no="5"/>
</zh_keywords>
<en_keywords>
<keyword seq_no="1"/>
<keyword seq_no="2"/>
<keyword seq_no="3"/>
<keyword seq_no="4"/>
<keyword seq_no="5"/>
</en_keywords>
</research>
<research seq_no="3">
<subject_code/>
<subject_name/>
<research_area_code/>
<research_area_name/>
<familiar_value/>
<familiar_name/>
<interest_value/>
<interest_name/>
</research>
<research seq_no="4">
<subject_code/>
<subject_name/>
<research_area_code/>
<research_area_name/>
<familiar_value/>
<familiar_name/>
<interest_value/>
<interest_name/>
</research>
<research seq_no="5">
<subject_code/>
<subject_name/>
<research_area_code/>
<research_area_name/>
<familiar_value/>
<familiar_name/>
<interest_value/>
<interest_name/>
</research>
</researches>
<disciplines>
<discipline seq_no="1">
<zh_keywords>
<keyword seq_no="1">222</keyword>
<keyword seq_no="2">333</keyword>
<keyword seq_no="3">444</keyword>
<keyword seq_no="4">555</keyword>
<keyword seq_no="5">666</keyword>
</zh_keywords>
<discipline_code>B020101</discipline_code>
<en_keywords>
<keyword seq_no="1"/>
<keyword seq_no="2"/>
<keyword seq_no="3"/>
<keyword seq_no="4"/>
<keyword seq_no="5"/>
</en_keywords>
<discipline_name>B020101.有机合成反应</discipline_name>
<familiar0_value>01</familiar0_value>
<familiar0_name>熟悉</familiar0_name>
<interest0_value>02</interest0_value>
<interest0_name>2</interest0_name>
</discipline>
<discipline seq_no="2">
<discipline_code/>
<discipline_name/>
<familiar0_value/>
<familiar0_name/>
<interest0_value/>
<interest0_name/>
</discipline>
<discipline seq_no="3">
<discipline_code/>
<discipline_name/>
<familiar0_value/>
<familiar0_name/>
<interest0_value/>
<interest0_name/>
</discipline>
<discipline seq_no="4">
<discipline_code/>
<discipline_name/>
<familiar0_value/>
<familiar0_name/>
<interest0_value/>
<interest0_name/>
</discipline>
<discipline seq_no="5">
<discipline_code/>
<discipline_name/>
<familiar0_value/>
<familiar0_name/>
<interest0_value/>
<interest0_name/>
</discipline>
</disciplines>
<techs>
<tech seq_no="1">
<zh_keywords>
<keyword seq_no="1">443</keyword>
<keyword seq_no="2">4546</keyword>
<keyword seq_no="3">657565</keyword>
<keyword seq_no="4">4353453</keyword>
<keyword seq_no="5">t6546</keyword>
</zh_keywords>
<tech_code>202000</tech_code>
<en_keywords>
<keyword seq_no="1"/>
<keyword seq_no="2"/>
<keyword seq_no="3"/>
<keyword seq_no="4"/>
<keyword seq_no="5"/>
</en_keywords>
<tech_name>2.2 生化药物</tech_name>
</tech>
<tech seq_no="2">
<tech_code/>
<tech_name/>
</tech>
<tech seq_no="3">
<tech_code/>
<tech_name/>
</tech>
<tech seq_no="4">
<tech_code/>
<tech_name/>
</tech>
<tech seq_no="5">
<tech_code/>
<tech_name/>
</tech>
</techs>
<exp_attachs>
<exp_attach seq_no="1">
<attachtype_value>01</attachtype_value>
<attachtype_name>技术职称证明文件</attachtype_name>
<name>sdfsf</name>
<entity_file_name>703231084001.pdf</entity_file_name>
<file_code>8ed635dbf5d5447db890a6d488e1c7f3</file_code>
<file_date>2017-09-15 17:40:06</file_date>
</exp_attach>
</exp_attachs>
<proposal>
<sum/>
</proposal>
<industrys>
<industry seq_no="1">
<zh_keywords>
<keyword seq_no="1">yhhtry</keyword>
<keyword seq_no="2">454654</keyword>
<keyword seq_no="3">fghrty54</keyword>
<keyword seq_no="4">e45445</keyword>
<keyword seq_no="5">fthtr</keyword>
</zh_keywords>
<industry_code>B0820</industry_code>
<en_keywords>
<keyword seq_no="1"/>
<keyword seq_no="2"/>
<keyword seq_no="3"/>
<keyword seq_no="4"/>
<keyword seq_no="5"/>
</en_keywords>
<industry_name>锰矿、铬矿采选</industry_name>
</industry>
<industry seq_no="2">
<industry_code/>
<industry_name/>
</industry>
<industry seq_no="3">
<industry_code/>
<industry_name/>
</industry>
<industry seq_no="4">
<industry_code/>
<industry_name/>
</industry>
<industry seq_no="5">
<industry_code/>
<industry_name/>
</industry>
</industrys>
<classifications>
<classification seq_no="1">
<zh_keywords>
<keyword seq_no="1">tfhty</keyword>
<keyword seq_no="2">56gfdg</keyword>
<keyword seq_no="3">dgdfhfh</keyword>
<keyword seq_no="4">ghfgdfg</keyword>
<keyword seq_no="5">ghff</keyword>
</zh_keywords>
<classification_code>B81</classification_code>
<en_keywords>
<keyword seq_no="1"/>
<keyword seq_no="2"/>
<keyword seq_no="3"/>
<keyword seq_no="4"/>
<keyword seq_no="5"/>
</en_keywords>
<classification_name>逻辑学（论理学）</classification_name>
</classification>
<classification seq_no="2">
<classification_code/>
<classification_name/>
</classification>
<classification seq_no="3">
<classification_code/>
<classification_name/>
</classification>
<classification seq_no="4">
<classification_code/>
<classification_name/>
</classification>
<classification seq_no="5">
<classification_code/>
<classification_name/>
</classification>
</classifications>
<spec_resume>
<educations>
<education seq_no="1">
<form_year/>
<form_month/>
<to_year/>
<to_month/>
<college/>
<profession/>
<advisor/>
<degree/>
<awarded_country/>
<awarded_year/>
</education>
<education seq_no="2">
<form_year/>
<form_month/>
<to_year/>
<to_month/>
<college/>
<profession/>
<advisor/>
<degree/>
<awarded_country/>
<awarded_year/>
</education>
<education seq_no="3">
<form_year/>
<form_month/>
<to_year/>
<to_month/>
<college/>
<profession/>
<advisor/>
<degree/>
<awarded_country/>
<awarded_year/>
</education>
<education seq_no="4">
<form_year/>
<form_month/>
<to_year/>
<to_month/>
<college/>
<profession/>
<advisor/>
<degree/>
<awarded_country/>
<awarded_year/>
</education>
<education seq_no="5">
<form_year/>
<form_month/>
<remark>研究生在读</remark>
</education>
<education seq_no="6">
<form_year/>
<form_month/>
<to_year/>
<to_month/>
<college/>
<profession/>
<advisor/>
<degree/>
<awarded_country/>
<awarded_year/>
</education>
<education seq_no="7">
<form_year/>
<form_month/>
<to_year/>
<to_month/>
<college/>
<profession/>
<advisor/>
<degree/>
<awarded_country/>
<awarded_year/>
</education>
<education seq_no="8">
<form_year/>
<form_month/>
<remark>博士在读</remark>
</education>
</educations>
<work_experiences>
<work_experience seq_no="1">
<form_year/>
<form_month/>
<to_year/>
<to_month/>
<unit/>
<department/>
<title/>
</work_experience>
</work_experiences>
<postdoctor>
<is_postdoctor_value/>
<is_postdoctor_name/>
<unit/>
<advisor/>
<form_year/>
<form_month/>
<to_year/>
<to_month/>
<is_onjob_value/>
<is_onjob_name/>
</postdoctor>
</spec_resume>
</data>', null, 123);
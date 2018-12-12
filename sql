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

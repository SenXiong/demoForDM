package com.example.demo;

import com.example.demo.tools.XMLHelper;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Type;
import org.w3c.dom.Document;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="PERSON_EXTEND")
public class PersonExtend {

    private Long psnCode;

    private String psnVerson;

    private Document psnXml;

    private java.sql.Date updateTime;

    private Long updatePsnCode;

    @Id
    @GeneratedValue
    public Long getPsnCode() {
        return psnCode;
    }

    public void setPsnCode(Long psnCode) {
        this.psnCode = psnCode;
    }

    @Column(name = "PSN_VERSON")
    public String getPsnVerson() {
        return psnVerson;
    }

    public void setPsnVerson(String psnVerson) {
        this.psnVerson = psnVerson;
    }

    /*@ColumnTransformer(read = "to_clob(PSN_XML)", write = "?")
    @Column(name = "PSN_XML", columnDefinition = "XMLType")*/
    @Type(type="com.example.demo.tools.OracleXmlType")
    public Document getPsnXml() {
        return psnXml;
    }

    public void setPsnXml(Document psnXml) {
        this.psnXml = psnXml;
    }

    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "UPDATE_PSN_CODE")
    public Long getUpdatePsnCode() {
        return updatePsnCode;
    }

    public void setUpdatePsnCode(Long updatePsnCode) {
        this.updatePsnCode = updatePsnCode;
    }

    @Override
    public String toString()  {
        return this.getPsnCode()+"   "+this.getUpdatePsnCode() + "   " + XMLHelper.parseDoc(this.getPsnXml()).asXML();  //这里xml的格式直接return到页面的话，<xml>这种标签格式会被编码拦截，可打断点或直接输出console查看。
    }
}

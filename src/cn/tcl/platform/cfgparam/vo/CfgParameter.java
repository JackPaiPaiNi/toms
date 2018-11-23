package cn.tcl.platform.cfgparam.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("CfgParameter")
public class CfgParameter {
    /**
     * UIID[null]
     */
    private BigDecimal uiid;

    /**
     * PDOMAIN[null]
     */
    private String pdomain;

    /**
     * PTYPE[null]
     */
    private String ptype;

    /**
     * PKEY[null]
     */
    private String pkey;

    /**
     * PVALUE[null]
     */
    private String pvalue;

    /**
     * PDESC[null]
     */
    private String pdesc;

    /**
     * SYS_CREATE_BY[null]
     */
    private String sysCreateBy;

    /**
     * SYS_CREATE_DATE[null]
     */
    private Date sysCreateDate;

    /**
     * SYS_UPDATE_BY[null]
     */
    private String sysUpdateBy;

    /**
     * SYS_UPDATE_DATE[null]
     */
    private Date sysUpdateDate;

    /**
     * PSEQ[null]
     */
    private BigDecimal pseq;

    /**
     * 获取字段：UIID[null]的值
     */
    public BigDecimal getUiid() {
        return uiid;
    }

    /**
     * 设置字段：UIID[null]的值
     */
    public void setUiid(BigDecimal uiid) {
        this.uiid = uiid;
    }

    /**
     * 获取字段：PDOMAIN[null]的值
     */
    public String getPdomain() {
        return pdomain;
    }

    /**
     * 设置字段：PDOMAIN[null]的值
     */
    public void setPdomain(String pdomain) {
        this.pdomain = pdomain;
    }

    /**
     * 获取字段：PTYPE[null]的值
     */
    public String getPtype() {
        return ptype;
    }

    /**
     * 设置字段：PTYPE[null]的值
     */
    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    /**
     * 获取字段：PKEY[null]的值
     */
    public String getPkey() {
        return pkey;
    }

    /**
     * 设置字段：PKEY[null]的值
     */
    public void setPkey(String pkey) {
        this.pkey = pkey;
    }

    /**
     * 获取字段：PVALUE[null]的值
     */
    public String getPvalue() {
        return pvalue;
    }

    /**
     * 设置字段：PVALUE[null]的值
     */
    public void setPvalue(String pvalue) {
        this.pvalue = pvalue;
    }

    /**
     * 获取字段：PDESC[null]的值
     */
    public String getPdesc() {
        return pdesc;
    }

    /**
     * 设置字段：PDESC[null]的值
     */
    public void setPdesc(String pdesc) {
        this.pdesc = pdesc;
    }

    /**
     * 获取字段：SYS_CREATE_BY[null]的值
     */
    public String getSysCreateBy() {
        return sysCreateBy;
    }

    /**
     * 设置字段：SYS_CREATE_BY[null]的值
     */
    public void setSysCreateBy(String sysCreateBy) {
        this.sysCreateBy = sysCreateBy;
    }

    /**
     * 获取字段：SYS_CREATE_DATE[null]的值
     */
    public Date getSysCreateDate() {
        return sysCreateDate;
    }

    /**
     * 设置字段：SYS_CREATE_DATE[null]的值
     */
    public void setSysCreateDate(Date sysCreateDate) {
        this.sysCreateDate = sysCreateDate;
    }

    /**
     * 获取字段：SYS_UPDATE_BY[null]的值
     */
    public String getSysUpdateBy() {
        return sysUpdateBy;
    }

    /**
     * 设置字段：SYS_UPDATE_BY[null]的值
     */
    public void setSysUpdateBy(String sysUpdateBy) {
        this.sysUpdateBy = sysUpdateBy;
    }

    /**
     * 获取字段：SYS_UPDATE_DATE[null]的值
     */
    public Date getSysUpdateDate() {
        return sysUpdateDate;
    }

    /**
     * 设置字段：SYS_UPDATE_DATE[null]的值
     */
    public void setSysUpdateDate(Date sysUpdateDate) {
        this.sysUpdateDate = sysUpdateDate;
    }

    /**
     * 获取字段：PSEQ[null]的值
     */
    public BigDecimal getPseq() {
        return pseq;
    }

    /**
     * 设置字段：PSEQ[null]的值
     */
    public void setPseq(BigDecimal pseq) {
        this.pseq = pseq;
    }
}
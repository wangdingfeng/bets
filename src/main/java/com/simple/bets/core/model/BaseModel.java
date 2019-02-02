package com.simple.bets.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @ProjectName: bets
 * @Package: com.simple.bets.core.model
 * @ClassName: BaseModel
 * @Author: wangdingfeng
 * @Description: 数据基本类型字段
 * @Date: 2019/1/8 10:48
 * @Version: 1.0
 */
public abstract class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 正常状态
     */
    private static final String SYS_STATUS_NORMAL = "0";
    /**
     * 删除状态
     */
    private static final String SYS_STATUS_delete = "1";

    @Column(name = "remarks")
    protected String remarks;    // 备注

    @Column(name = "creator")
    protected String creator;    // 创建者

    @Column(name = "create_time")
    protected Date createTime;    // 创建日期

    @Column(name = "operator")
    protected String operator;    // 更新者

    @Column(name = "modify_time")
    protected Date modifyTime;    // 更新日期

    @Column(name = "status")
    protected String status;    //业务状态

    @Column(name = "sys_status")
    protected String sysStatus;    // 状态（0：正常；1：删除）

    public BaseModel() {
        super();
        this.sysStatus = SYS_STATUS_NORMAL;
    }

    @Length(min = 0, max = 1024)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @JsonIgnore
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonIgnore
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @JsonIgnore
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @JsonIgnore
    public String getSysStatus() {
        return sysStatus;
    }

    public void setSysStatus(String sysStatus) {
        this.sysStatus = sysStatus;
    }

    /**
     * 保存共有属性
     * @param isAdd 是否新增 true 新增 false 更新
     * @param operator 操作人
     */
    public void setBaseData(boolean isAdd,String operator){
        if(isAdd){
            this.setCreateTime(new Date());
            this.setCreator(operator);
            this.setSysStatus(SYS_STATUS_NORMAL);
            this.setStatus(SYS_STATUS_NORMAL);
        }
        this.setOperator(operator);
        this.setModifyTime(new Date());
    }
}

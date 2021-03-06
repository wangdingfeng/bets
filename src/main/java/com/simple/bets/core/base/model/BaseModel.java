package com.simple.bets.core.base.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simple.bets.modular.sys.model.UserModel;
import com.simple.bets.modular.sys.utils.UserUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Transient;
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
    private static final String STATUS_NORMAL = "0";
    /**
     * 删除状态
     */
    private static final String STATUS_DELETE = "1";

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
    protected String status;    //系统
    @Transient
    protected String creatorName;//创建人翻译
    @Transient
    protected String operatorName;//创建人翻译

    @JsonIgnore
    @Transient
    protected Object example;


    public BaseModel() {
        super();
        this.status = STATUS_NORMAL;
    }

    @Length(min = 0, max = 1024)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getExample() {
        return example;
    }

    public void setExample(Object example) {
        this.example = example;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getOperatorName() {
        return creatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * 保存共有属性
     * @param isAdd 是否新增 true 新增 false 更新
     */
    public void setBaseData(boolean isAdd){
        UserModel user = UserUtils.getPrincipal();
        if(isAdd){
            this.setCreateTime(new Date());
            this.setCreator(null == user ? "system": user.getUsername());
            this.setStatus(STATUS_NORMAL);
        }
        this.setOperator(null == user ? "system": user.getUsername());
        this.setModifyTime(new Date());
    }
}

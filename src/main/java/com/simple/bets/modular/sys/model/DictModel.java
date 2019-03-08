package com.simple.bets.modular.sys.model;

import com.simple.bets.core.annotation.ExportConfig;
import com.simple.bets.core.base.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author wangdingfeng
 * @Description 字典信息表
 * @Date 17:40 2019/1/14
 **/

@Table(name = "sys_dict")
public class DictModel extends BaseModel {

    private static final long serialVersionUID = 7780820231535870010L;

    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "dict_id")
    @ExportConfig(value = "字典ID")
    private Long dictId;

    @Column(name = "dict_label")
    @ExportConfig(value = "字典Key")
    private String dictLabel;

    @Column(name = "dict_value")
    @ExportConfig(value = "字典Value")
    private String dictValue;

    @Column(name = "dict_type")
    private String dictType;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "is_parent")
    private String isParent = "1";

    /**
     * 字典状态
     */
    @Column(name = "dict_status")
    private String dictStatus;

    /**
     * @return DICT_ID
     */
    public Long getDictId() {
        return dictId;
    }

    /**
     * @param dictId
     */
    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }

    public String getDictStatus() {
        return dictStatus;
    }

    public void setDictStatus(String dictStatus) {
        this.dictStatus = dictStatus;
    }
}
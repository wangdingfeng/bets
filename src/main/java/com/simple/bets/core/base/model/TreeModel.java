package com.simple.bets.core.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;

/**
 * 树形基础 Model
 * @Author: wangdingfeng
 * @Date: 2019/1/8 10:48
 */
public abstract class TreeModel extends BaseModel {

	private static final long serialVersionUID = 1L;

	public static final String TREE_LEAF_YES = "0";

	public static final String TREE_LEAF_NO = "1";

	@Column(name = "parent_id")
	protected Long parentId = 0L;
	@Column(name = "parent_ids")
	protected String parentIds ; // 所有父级编号
	@Column(name = "sort")
	protected Integer sort;		// 排序
	@Column(name = "tree_leaf")
	protected String treeLeaf;      // 是否树形叶子
	@Column(name = "tree_level")
	protected Integer treeLevel; // 树形层级

	public TreeModel() {
		super();
	}
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}


	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

    public String getTreeLeaf() {
        return treeLeaf;
    }

    public void setTreeLeaf(String treeLeaf) {
        this.treeLeaf = treeLeaf;
    }

    public Integer getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(Integer treeLevel) {
        this.treeLevel = treeLevel;
    }

    public boolean getIsRoot()
    {
		return "0".equals(getParentId());
	}

    public Boolean getIsTreeLeaf()
    {
        return Boolean.valueOf("1".equals(treeLeaf));
    }

    @JsonIgnore
    public static String getRootId(){
        return "0";
    }

	public String getParentIds() {
		return parentIds;
	}
}

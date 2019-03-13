package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.core.common.util.TreeUtils;
import com.simple.bets.core.base.model.Tree;
import com.simple.bets.core.base.service.impl.ServiceImpl;
import com.simple.bets.modular.sys.dao.OfficeMapper;
import com.simple.bets.modular.sys.model.MenuModel;
import com.simple.bets.modular.sys.model.OfficeModel;
import com.simple.bets.modular.sys.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: demo
 * @Package: com.simple.bets.modular.sys.service.impl
 * @ClassName: OfficeServiceImpl
 * @Author: wangdingfeng
 * @Description: ${description}
 * @Date: 2019/1/8 14:37
 * @Version: 1.0
 */
@Service("officeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OfficeServiceImpl extends ServiceImpl<OfficeModel> implements OfficeService {

    @Autowired
    OfficeMapper officeMapper;

    @Override
    public List<OfficeModel> findAllList(OfficeModel office) {
        return officeMapper.findAllList(office);
    }

    @Override
    public Tree<OfficeModel> getAllOfficeTree(OfficeModel office) {
        office.setParentId(null);
        List<Tree<OfficeModel>> treeList = new ArrayList<>();
        List<OfficeModel> offices = officeMapper.findAllList(office);
        offices.forEach(office1 -> {
            Tree<OfficeModel> tree = new Tree<>();
            tree.setId(office1.getId().toString());
            tree.setParentId(office1.getParentId().toString());
            tree.setText(office1.getName());
            treeList.add(tree);
        });
        return TreeUtils.build(treeList);
    }

    @Override
    public void saveOrUpdate(OfficeModel office) {

        OfficeModel newParent = super.findById(office.getParentId());
        Long oldParentId = null;
        String oldParentIds = "";
        int oldParentTLevel = 0;


        if (null == office.getId()) {
            //新增根节点
            if (null == newParent) {
                office.setTreeLeaf(OfficeModel.TREE_LEAF_NO);
                office.setTreeLevel(0);
            } else {
                office.setTreeLeaf(OfficeModel.TREE_LEAF_NO);
                office.setTreeLevel(newParent.getTreeLevel() + 1);
                if (newParent.getIsTreeLeaf()) {
                    newParent.setTreeLeaf(OfficeModel.TREE_LEAF_YES);
                    super.update(newParent);
                }
            }
            // 设置新的父节点串
            office.setParentIds(newParent == null ? "0" : (newParent.getParentIds() + "," + office.getParentId()));
            super.save(office);
        } else {

            // 数据库中当前的menu的ParentId，还未更新
            OfficeModel copyOffice = findById(office.getId());
            oldParentId = copyOffice.getParentId();
            // 获取修改前的parentIds，用于更新子节点的parentIds
            oldParentIds = copyOffice.getParentIds();


            OfficeModel oldParent = findById(oldParentId);
            oldParentTLevel = oldParent == null ? -1 : oldParent.getTreeLevel();

            // 设置新的父节点串
            if (!oldParentId.equals(office.getParentId())) {
                office.setParentIds(newParent == null ? "0" : (newParent.getParentIds() + "," + office.getParentId()));
                //获取新的父类层级
                office.setTreeLevel(newParent.getTreeLevel() + 1);
            }
            super.update(office);

            // 判断menu父节点是否发生了改变
            if (!oldParentId.equals(office.getParentId())) {
                // 第一步：判断原来的父节点下还有没有子菜单
                if (oldParent != null) {
                    List<OfficeModel> list1 = this.officeMapper.findSubOfficeListByPid(oldParentId);
                    // 原来的父节点下没有子节点了，并且节点treeleaf属性不等于1
                    if (list1.size() <= 0 && !oldParent.getIsTreeLeaf()) {
                        oldParent.setTreeLeaf(OfficeModel.TREE_LEAF_NO);
                        super.update(oldParent);
                    }
                }

                // 第二步：1.更新子节点 parentIds
                List<OfficeModel> list2 = findSubOfficeListByPid(office.getId());

                int diffValue = newParent.getTreeLevel() - oldParentTLevel;

                for (OfficeModel e : list2) {
                    // 更新子节点 parentIds
                    e.setParentIds(e.getParentIds().replace(oldParentIds, office.getParentIds()));
                    // 更新menu子节点的treelevel值
                    e.setTreeLevel(e.getTreeLevel() + diffValue);
                    this.update(e);
                }

                // 第三步：新父节点如果treeLeaf==1，则需要更新treeLeaf==0
                if (newParent.getIsTreeLeaf()) {
                    newParent.setTreeLeaf(OfficeModel.TREE_LEAF_YES);
                    this.update(newParent);
                }
            }
        }
    }

    private List<OfficeModel> findSubOfficeListByPid(Long officeId) {
        Example example = new Example(MenuModel.class);
        example.createCriteria().andLike("parentIds", "," + officeId + ",");
        List<OfficeModel> list = super.selectByExample(example);
        return list;
    }
}

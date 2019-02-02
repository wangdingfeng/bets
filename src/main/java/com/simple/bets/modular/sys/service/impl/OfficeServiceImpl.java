package com.simple.bets.modular.sys.service.impl;

import com.simple.bets.core.common.util.TreeUtils;
import com.simple.bets.core.model.Tree;
import com.simple.bets.core.service.impl.ServiceImpl;
import com.simple.bets.modular.sys.dao.OfficeMapper;
import com.simple.bets.modular.sys.model.Office;
import com.simple.bets.modular.sys.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
public class OfficeServiceImpl extends ServiceImpl<Office> implements OfficeService {

    @Autowired
    OfficeMapper officeMapper;

    @Override
    public List<Office> findAllList(Office office) {
        return officeMapper.findAllList(office);
    }

    @Override
    public Tree<Office> getAllOfficeTree(Office office) {
        List<Tree<Office>> treeList = new ArrayList<>();
        List<Office>  offices = officeMapper.findAllList(office);
        offices.forEach(office1 -> {
            Tree<Office> tree = new Tree<>();
            tree.setId(office1.getId().toString());
            tree.setParentId(office1.getParentId().toString());
            tree.setText(office1.getName());
            treeList.add(tree);
        });
        return TreeUtils.build(treeList);
    }
}

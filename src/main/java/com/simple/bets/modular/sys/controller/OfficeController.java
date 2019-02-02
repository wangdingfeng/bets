package com.simple.bets.modular.sys.controller;

import com.simple.bets.core.model.Tree;
import com.simple.bets.modular.sys.model.Office;
import com.simple.bets.modular.sys.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ProjectName: bets
 * @Package: com.simple.bets.modular.sys.controller
 * @ClassName: OfficeController
 * @Author: wangdingfeng
 * @Description: 获取机构信息
 * @Date: 2019/1/8 14:38
 * @Version: 1.0
 */
@Controller
@RequestMapping("/office")
public class OfficeController {

    private static final String PAGE_SUFFIX = "sys/office";

    @Autowired
    private OfficeService officeService;

    /**
     * 跳转列表页面
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        return PAGE_SUFFIX+"/office-list";
    }

    /**
     * 部门list
     * @param office
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public List<Office> list(Office office){
        office.setName(null);
        if(null == office.getParentId()){
            office.setParentId(0L);
        }
        List<Office> list = officeService.findAllList(office);
        return list;
    }

    /**
     * 获取部门tree
     * @return
     */
    @RequestMapping("/getOfficeTree")
    @ResponseBody
    public Tree<Office> getOfficeTree(){
        Tree<Office> tree = officeService.getAllOfficeTree(new Office());
        return tree;
    }
}

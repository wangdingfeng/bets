package com.simple.bets.modular.sys.controller;

import com.simple.bets.core.annotation.Log;
import com.simple.bets.core.base.model.ResponseResult;
import com.simple.bets.core.base.model.Tree;
import com.simple.bets.modular.sys.model.OfficeModel;
import com.simple.bets.modular.sys.service.OfficeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    private Logger logger = LoggerFactory.getLogger(getClass());

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
    public List<OfficeModel> list(OfficeModel office){
        List<OfficeModel> list = officeService.findAllList(office);
        return list;
    }

    /**
     * 添加 编辑
     * @return
     */
    @RequestMapping("/form")
    public String form(OfficeModel office, Model model){
        if(null != office.getId()){
            office = officeService.findById(office.getId());
        }
        model.addAttribute("office",office);
        return PAGE_SUFFIX+"/office-form";
    }

    /**
     * 保存or更新
     * @param office
     * @return
     */
    @Log("编辑部门")
    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public ResponseResult saveOrUpdate(OfficeModel office){
        try {
            officeService.saveOrUpdate(office);
            return ResponseResult.ok("操作成功！");
        } catch (Exception e) {
            logger.error("操作失败", e);
            return ResponseResult.error("操作失败失败，请联系网站管理员！");
        }
    }

    /**
     * 获取部门tree
     * @return
     */
    @RequestMapping("/getOfficeTree")
    @ResponseBody
    public ResponseResult getOfficeTree(){
        try {
            Tree<OfficeModel> tree = officeService.getAllOfficeTree(new OfficeModel());
            return ResponseResult.ok(tree);
        } catch (Exception e) {
            logger.error("获取菜单树失败", e);
            return ResponseResult.error("获取菜单树失败！");
        }
    }
}

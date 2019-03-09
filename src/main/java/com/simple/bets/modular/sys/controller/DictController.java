package com.simple.bets.modular.sys.controller;

import com.simple.bets.core.common.util.Page;
import com.simple.bets.core.base.controller.BaseController;
import com.simple.bets.core.base.model.ResponseResult;
import com.simple.bets.modular.sys.model.DictModel;
import com.simple.bets.core.annotation.Log;
import com.simple.bets.modular.sys.service.DictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author wangdingfeng
 * @Description 字典管理
 * @Date 16:08 2019/1/14
 **/
@Controller
@RequestMapping("/sys/dict")
public class DictController extends BaseController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String PAGE_SUFFIX = "modular/sys/dict";

    @Autowired
    private DictService dictService;

    /**
     * 跳转到 字典列表
     *
     * @return
     */
    @Log("获取字典信息")
    @RequestMapping("/list")
    @RequiresPermissions("dict:list")
    public String list() {
        return PAGE_SUFFIX + "/dict-list";
    }

    /**
     * @Author wangdingfeng
     * @Description 字典列表
     * @Date 16:08 2019/1/14
     **/

    @RequestMapping("/listData")
    @ResponseBody
    public Page<DictModel> listData(DictModel dict, HttpServletRequest request, HttpServletResponse response) {
        return dictService.queryPage(new Page<DictModel>(request, response), dict);
    }

    /**
     * 跳转到子菜单信息
     *
     * @return
     */
    @RequestMapping("/dictChildren")
    public String dictChildren(DictModel dict, Model model) {
        dict = dictService.findById(dict.getDictId());
        model.addAttribute("dict", dict);
        return PAGE_SUFFIX + "/dict-chr-list";
    }

    /**
     * 查看编辑
     *
     * @param dict
     * @return
     */
    @RequestMapping("/form")
    public String form(DictModel dict, Model model) {
        if (null != dict.getDictId()) {
            dict = dictService.findById(dict.getDictId());
        }
        model.addAttribute("dict", dict);
        return PAGE_SUFFIX + "/dict-from";
    }

    /**
     * 新增|更新字典数据
     *
     * @param dict
     * @return
     */
    @Log("新增|更新字典")
    @RequiresPermissions("dict:edit")
    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public ResponseResult saveOrUpdate(DictModel dict) {
        try {
            this.dictService.saveOrUpdate(dict);
            return ResponseResult.ok("新增字典成功！");
        } catch (Exception e) {
            log.error("新增字典失败", e);
            return ResponseResult.error("新增字典失败，请联系网站管理员！");
        }
    }

    /**
     * 删除菜单
     *
     * @param dictId
     * @return
     */
    @Log("删除字典")
    @RequiresPermissions("dict:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseResult deleteDict(Long dictId) {
        try {
            this.dictService.delete(dictId);
            return ResponseResult.ok("删除字典成功！");
        } catch (Exception e) {
            log.error("删除字典失败", e);
            return ResponseResult.error("删除字典失败，请联系网站管理员！");
        }
    }
}

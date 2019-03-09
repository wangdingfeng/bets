package com.simple.bets.modular.sys.controller;

import com.simple.bets.core.base.controller.BaseController;
import com.simple.bets.core.base.model.ResponseResult;
import com.simple.bets.core.base.model.Tree;
import com.simple.bets.modular.sys.model.MenuModel;
import com.simple.bets.core.annotation.Log;
import com.simple.bets.modular.sys.service.MenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author wangdingfeng
 * @Description 菜单控制层
 * @Date 9:40 2019/1/14
 **/
@Controller
@RequestMapping("/sys/menu")
public class MenuController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PAGE_SUFFIX = "modular/sys/menu";

    @Autowired
    private MenuService menuService;


    /**
     * 跳转到菜单信息界面
     *
     * @return
     */
    @Log("获取菜单信息")
    @RequestMapping("/list")
    @RequiresPermissions("menu:list")
    public String list() {
        return PAGE_SUFFIX + "/menu-list";
    }


    /**
     * @Author wangdingfeng
     * @Description 获取所有的菜单
     * @Date 15:28 2019/1/11
     **/
    @RequestMapping("/listData")
    @ResponseBody
    public List<MenuModel> listData(MenuModel menu) {
        return menuService.findAllMenus(menu);
    }

    /**
     * @return
     * @Author wangdingfeng
     * @Description //TODO
     * @Date 9:43 2019/1/14
     **/
    @RequestMapping("/form")
    public String form(MenuModel menu, Model model) {
        if (null != menu.getId()) {
            menu = menuService.findById(menu.getId());
            // 获取排序号，最末节点排序号+30
            List<MenuModel> list = new ArrayList<>();
            List<MenuModel> sourcelist = menuService.findAllMenus(new MenuModel());
            MenuModel.sortList(list, sourcelist, menu.getParentId(), false);
            if (list.size() > 0) {
                menu.setSort(list.get(list.size() - 1).getSort() + 30);
            }
        }else{
            menu.setSort(30);
        }
        model.addAttribute("menu", menu);
        return PAGE_SUFFIX + "/menu-form";
    }

    /**
     * 保存or更新
     *
     * @param menu
     * @return
     */
    @Log("保存|更新菜单")
    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public ResponseResult saveOrUpdate(MenuModel menu) {
        String name;
        if (MenuModel.TYPE_MENU.equals(menu.getType())) {
            name = "菜单";
        } else {
            name = "按钮";
        }
        try {
            menuService.saveOrUpdate(menu);
            return ResponseResult.ok("操作" + name + "成功！");
        } catch (Exception e) {
            logger.error("操作" + name + "失败", e);
            return ResponseResult.error("操作" + name + "失败，请联系网站管理员！");
        }
    }

    /**
     * 获取菜单树
     *
     * @return
     */
    @RequestMapping("/tree")
    @ResponseBody
    public ResponseResult getMenuTree(boolean isAll) {
        try {
            Tree<MenuModel> tree = this.menuService.getMenuTree(isAll);
            return ResponseResult.ok(tree);
        } catch (Exception e) {
            logger.error("获取菜单树失败", e);
            return ResponseResult.error("获取菜单树失败！");
        }
    }

    /**
     * 获取当前用户的菜单
     *
     * @param userName 账户名
     * @return
     */
    @RequestMapping("/getUserMenu")
    @ResponseBody
    public ResponseResult getUserMenu(String userName) {
        try {
            List<Tree<MenuModel>> tree = this.menuService.getUserMenu(userName);
            return ResponseResult.ok(tree);
        } catch (Exception e) {
            logger.error("获取用户菜单失败", e);
            return ResponseResult.error("获取用户菜单失败！");
        }
    }

    /**
     * 选择图标
     *
     * @return
     */
    @RequestMapping("/iconselect")
    public String iconselect(HttpServletRequest request, Model model) {
        model.addAttribute("value", request.getParameter("value"));
        return PAGE_SUFFIX + "/tagIconselect";
    }

    /**
     * 批量修改菜单排序
     */
    @Log("批量修改菜单排序")
    @RequestMapping(value = "updateSort")
    @ResponseBody
    public ResponseResult updateSort(Long[] ids, Integer[] sorts) {
        for (int i = 0; i < ids.length; i++) {
            MenuModel menu = new MenuModel(ids[i]);
            menu.setSort(sorts[i]);
            menuService.updateMenuSort(menu);
        }
        return ResponseResult.ok("保存菜单排序成功");
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     */
    @Log("删除菜单")
    @RequiresPermissions("menu:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseResult deleteMenus(Long id) {
        try {
            menuService.deleteMenu(id);
            return ResponseResult.ok("删除成功！");
        } catch (Exception e) {
            logger.error("获取菜单失败", e);
            return ResponseResult.error("删除失败，请联系网站管理员！");
        }
    }


    @Log("获取系统所有URL")
    @GetMapping("menu/urlList")
    @ResponseBody
    public List<Map<String, String>> getAllUrl() {
        return this.menuService.getAllUrl("1");
    }

}

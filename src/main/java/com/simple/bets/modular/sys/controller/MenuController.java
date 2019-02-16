package com.simple.bets.modular.sys.controller;

import com.simple.bets.core.common.util.FileUtil;
import com.simple.bets.core.controller.BaseController;
import com.simple.bets.core.model.ResponseResult;
import com.simple.bets.core.model.Tree;
import com.simple.bets.modular.sys.model.Menu;
import com.simple.bets.core.annotation.Log;
import com.simple.bets.modular.sys.service.MenuService;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/menu")
public class MenuController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PAGE_SUFFIX = "sys/menu";

    @Autowired
    private MenuService menuService;


    /**
     * 跳转到菜单信息界面
     *
     * @return
     */
    @Log("获取菜单信息")
    @RequestMapping("/index")
    @RequiresPermissions("menu:list")
    public String index() {
        return PAGE_SUFFIX + "/menu-list";
    }


    /**
     * @Author wangdingfeng
     * @Description 获取所有的菜单
     * @Date 15:28 2019/1/11
     **/
    @RequestMapping("/list")
    @ResponseBody
    public List<Menu> list(Menu menu) {
        return menuService.findAllMenus(menu);
    }

    /**
     * @Author wangdingfeng
     * @Description //TODO
     * @Date 9:43 2019/1/14
     * @Param 
     * @return 
     **/
    @RequestMapping("/form")
    public String form(Menu menu, Model model) {
        if (null != menu.getId()) {
            menu = menuService.findById(menu.getId());
        }
        model.addAttribute("menu", menu);
        return PAGE_SUFFIX + "/menu-form";
    }

    /**
     * 获取菜单树
     * @return
     */
    @RequestMapping("/tree")
    @ResponseBody
    public ResponseResult getMenuTree() {
        try {
            Tree<Menu> tree = this.menuService.getMenuTree();
            return ResponseResult.ok(tree);
        } catch (Exception e) {
            logger.error("获取菜单树失败", e);
            return ResponseResult.error("获取菜单树失败！");
        }
    }

    /**
     * 获取当前用户的菜单
     * @param userName 账户名
     * @return
     */
    @RequestMapping("/getUserMenu")
    @ResponseBody
    public ResponseResult getUserMenu(String userName) {
        try {
            List<Tree<Menu>> tree = this.menuService.getUserMenu(userName);
            return ResponseResult.ok(tree);
        } catch (Exception e) {
            logger.error("获取用户菜单失败", e);
            return ResponseResult.error("获取用户菜单失败！");
        }
    }

    /**
     * 选择图标
     * @return
     */
    @RequestMapping("/iconselect")
    public String iconselect(HttpServletRequest request, Model model) {
        model.addAttribute("value", request.getParameter("value"));
        return PAGE_SUFFIX+"/tagIconselect";
    }

    /**
     * 校验单签菜单名称
     * @param menuName
     * @param type
     * @param oldMenuName
     * @return
     */
    @RequestMapping("menu/checkMenuName")
    @ResponseBody
    public boolean checkMenuName(String menuName, String type, String oldMenuName) {
        if (StringUtils.isNotBlank(oldMenuName) && menuName.equalsIgnoreCase(oldMenuName)) {
            return true;
        }
        Menu result = this.menuService.findByNameAndType(menuName, type);
        return result == null;
    }

    @Log("新增菜单/按钮")
    @RequiresPermissions("menu:add")
    @RequestMapping("menu/add")
    @ResponseBody
    public ResponseResult addMenu(Menu menu) {
        String name;
        if (Menu.TYPE_MENU.equals(menu.getType())) {
            name = "菜单";
        } else {
            name = "按钮";
        }
        try {
            this.menuService.addMenu(menu);
            return ResponseResult.ok("新增" + name + "成功！");
        } catch (Exception e) {
            logger.error("新增{}失败", name, e);
            return ResponseResult.error("新增" + name + "失败，请联系网站管理员！");
        }
    }

    @Log("删除菜单")
    @RequiresPermissions("menu:delete")
    @RequestMapping("menu/delete")
    @ResponseBody
    public ResponseResult deleteMenus(String ids) {
        try {
            this.menuService.deleteMeuns(ids);
            return ResponseResult.ok("删除成功！");
        } catch (Exception e) {
            logger.error("获取菜单失败", e);
            return ResponseResult.error("删除失败，请联系网站管理员！");
        }
    }

    @Log("修改菜单/按钮")
    @RequiresPermissions("menu:update")
    @RequestMapping("menu/update")
    @ResponseBody
    public ResponseResult updateMenu(Menu menu) {
        String name;
        if (Menu.TYPE_MENU.equals(menu.getType()))
            name = "菜单";
        else
            name = "按钮";
        try {
            this.menuService.updateMenu(menu);
            return ResponseResult.ok("修改" + name + "成功！");
        } catch (Exception e) {
            logger.error("修改{}失败", name, e);
            return ResponseResult.error("修改" + name + "失败，请联系网站管理员！");
        }
    }


    @Log("获取系统所有URL")
    @GetMapping("menu/urlList")
    @ResponseBody
    public List<Map<String, String>> getAllUrl() {
        return this.menuService.getAllUrl("1");
    }

}

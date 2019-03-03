package com.simple.bets.modular.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.simple.bets.core.common.util.FileUtil;
import com.simple.bets.core.common.util.Page;
import com.simple.bets.core.controller.BaseController;
import com.simple.bets.core.model.ResponseResult;
import com.simple.bets.core.model.Tree;
import com.simple.bets.modular.sys.model.Menu;
import com.simple.bets.modular.sys.model.Role;
import com.simple.bets.core.annotation.Log;
import com.simple.bets.modular.sys.model.RoleMenu;
import com.simple.bets.modular.sys.model.User;
import com.simple.bets.modular.sys.service.MenuService;
import com.simple.bets.modular.sys.service.RoleMenuService;
import com.simple.bets.modular.sys.service.RoleService;
import com.simple.bets.modular.sys.service.UserRoleService;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;

/**
 * @Author wangdingfeng
 * @Description 角色管理
 * @Date   2019/1/23
 **/

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {


    private static final String PAGE_SUFFIX = "sys/role";

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserRoleService userRoleService;


    /**
     * 角色管理
     * @return
     */
    @Log("获取角色信息")
    @RequestMapping("/index")
    @RequiresPermissions("role:list")
    public String index() {
        return PAGE_SUFFIX+"/role-list";
    }
    /**
     * 分页查询数据
     * @param role
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Page<Role> list(Role role, HttpServletRequest request, HttpServletResponse response){
        Page<Role> page= roleService.queryPage(new Page<Role>(request, response), role);
        return page;
    }

    /**
     * 新增 编辑 跳转
     * @param role
     * @param model
     * @return
     */
    @Log("新增|编辑-角色信息")
    @RequiresPermissions("role:add")
    @RequestMapping("/form")
    public String form(Role role, Model model){
        if(null != role.getRoleId()){
            role = roleService.findById(role.getRoleId());
            List<Long> menuIds = roleMenuService.findRoleMenu(role.getRoleId());
            role.setMenuIds(StringUtils.join(menuIds,","));
        }
        Tree<Menu> tree = this.menuService.getMenuTree(true);
        String treeJson =JSONObject.toJSONString(tree);
        model.addAttribute("treeJson",treeJson);
        model.addAttribute("role",role);
        return PAGE_SUFFIX+"/role-form";
    }

    /**
     * 保存|编辑
     * @return
     */
    @Log("保存|更新角色")
    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public ResponseResult saveOrUpdate(Role role){
        if(null != role.getRoleId()){
            Role oldRole = roleService.findById(role.getRoleId());
            if(!checkRoleName(role.getRoleName(),oldRole.getRoleName())){
                return ResponseResult.error("当前填写角色名重复，请重新填写");
            }
        }else{
            if(!checkRoleName(role.getRoleName(),"")){
                return ResponseResult.error("当前填写角色名重复，请重新填写");
            }
        }
        try {
            roleService.saveOrUpdate(role);
        } catch (Exception e) {
            logger.error("保存or更新-角色信息失败", e);
            return ResponseResult.error("操作角色信息失败，请联系网站管理员！");
        }
        return ResponseResult.ok("保存成功");
    }

    @RequestMapping("/excel")
    @ResponseBody
    public ResponseResult roleExcel(Role role) {
        try {
            List<Role> list = this.roleService.findAllRole(role);
            return FileUtil.createExcelByPOIKit("角色表", list, Role.class);
        } catch (Exception e) {
            logger.error("导出角色信息Excel失败", e);
            return ResponseResult.error("导出Excel失败，请联系网站管理员！");
        }
    }

    /**
     * 检验角色名称
     * @param roleName
     * @param oldRoleName
     * @return
     */
    @RequestMapping("/checkRoleName")
    @ResponseBody
    public boolean checkRoleName(String roleName, String oldRoleName) {
        if (StringUtils.isNotBlank(oldRoleName) && roleName.equalsIgnoreCase(oldRoleName)) {
            return true;
        }
        Role result = this.roleService.findByName(roleName);
        return result == null;
    }

    /**
     * 删除角色信息
     * @param roleId 角色id
     * @return
     */
    @Log("删除角色")
    @RequiresPermissions("role:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseResult deleteRoles(Long roleId) {
        try {
            this.roleService.deleteRoles(roleId.toString());
            return ResponseResult.ok("删除角色成功！");
        } catch (Exception e) {
            logger.error("删除角色失败", e);
            return ResponseResult.error("删除角色失败，请联系网站管理员！");
        }
    }

    /**
     * 跳转到 角色对应的用户信息表
     * @param user
     * @param model
     * @return
     */
    @RequestMapping("/roleUserList")
    public String roleUserList(User user,Model model,Boolean exist){
        model.addAttribute("user",user);
        model.addAttribute("exist",exist);
        return PAGE_SUFFIX+"/role-assign";
    }

    /**
     * 获取 角色对应的用户信息数据
     * @param user
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/roleUserListData")
    @ResponseBody
    public Page<User> roleUserListData(User user, HttpServletRequest request, HttpServletResponse response,Boolean exist){
        return userRoleService.queryPage(new Page<User>(request, response), user,exist);
    }

    /**
     * 选择用户 dialog
     * @param role
     * @param selectData
     * @param checkbox
     * @param model
     * @return
     */
    @RequestMapping(value = "selectUserToRole")
    public String selectUserToRole(Role role, String selectData, String checkbox, Model model) {
        model.addAttribute("selectData", selectData);
        model.addAttribute("checkbox", checkbox);
        model.addAttribute("role", role);

        return PAGE_SUFFIX+"/selectUserToRole";
    }

    /**
     * 授权用户
     * @param role
     * @return
     */
    @RequestMapping("/assignRole")
    @ResponseBody
    public ResponseResult assignRole(Role role){
        try {
            userRoleService.assignRoleUser(role);
            return ResponseResult.ok("分配角色成功！");
        } catch (Exception e) {
            logger.error("分配角色失败",e);
            return ResponseResult.error("分配角色失败，请联系网站管理员！");
        }
    }

    /**
     * 删除用户角色
     * @param role
     * @return
     */
    @RequestMapping("/deleteUserRole")
    @ResponseBody
    public ResponseResult deleteUserRole(Role role){
        try {
            userRoleService.deleteUserRolesByRoleId(role);
            return ResponseResult.ok("删除角色成功！");
        } catch (Exception e) {
            logger.error("删除角色失败",e);
            return ResponseResult.error("删除角色失败，请联系网站管理员！");
        }
    }

}

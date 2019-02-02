package com.simple.bets.modular.sys.controller;

import com.simple.bets.core.common.util.FileUtil;
import com.simple.bets.core.common.util.Page;
import com.simple.bets.core.controller.BaseController;
import com.simple.bets.core.model.ResponseResult;
import com.simple.bets.modular.sys.model.Role;
import com.simple.bets.core.annotation.Log;
import com.simple.bets.modular.sys.service.RoleService;
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

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String PAGE_SUFFIX = "sys/role";

    @Autowired
    private RoleService roleService;


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
        if(StringUtils.isEmpty(role.getRoleName())){
            role.setRoleName(null);
        }
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
        }
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
            log.error("保存or更新-角色信息失败", e);
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
            log.error("导出角色信息Excel失败", e);
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
            log.error("删除角色失败", e);
            return ResponseResult.error("删除角色失败，请联系网站管理员！");
        }
    }
}

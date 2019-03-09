package com.simple.bets.modular.sys.controller;

import cn.hutool.core.util.RandomUtil;
import com.simple.bets.core.common.util.ImageBase64Util;
import com.simple.bets.core.common.util.Page;
import com.simple.bets.core.base.controller.BaseController;
import com.simple.bets.core.base.model.ResponseResult;
import com.simple.bets.core.annotation.Log;
import com.simple.bets.modular.sys.model.RoleModel;
import com.simple.bets.modular.sys.model.UserModel;
import com.simple.bets.modular.sys.service.OfficeService;
import com.simple.bets.modular.sys.service.RoleService;
import com.simple.bets.modular.sys.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理
 */
@Controller
@RequestMapping("/sys/user")
public class UserController extends BaseController {

    private static final String PAGE_SUFFIX = "modular/sys/user";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OfficeService officeService;

    /**
     * 查看用户列表
     * @param model
     * @return
     */
    @Log("查看用户列表")
    @RequestMapping("/list")
    @RequiresPermissions("user:list")
    public String list(Model model) {
        UserModel user = super.getCurrentUser();
        model.addAttribute("user", user);
        return PAGE_SUFFIX + "/user-list";
    }

    /**
     * 获取数据
     *
     * @return
     */
    @RequestMapping("/listData")
    @ResponseBody
    public Page<UserModel> listData(UserModel user, HttpServletRequest request, HttpServletResponse response) {
        Page<UserModel> page = userService.queryPage(new Page<UserModel>(request, response), user);
        return page;
    }

    /**
     * 添加修改页面
     *
     * @param user 用户
     * @return
     */
    @RequestMapping("/form")
    public String form(UserModel user, Model model) {
        if (null != user.getUserId()) {
            user = userService.findById(user.getUserId());
            model.addAttribute("user", user);
        }
        return PAGE_SUFFIX + "/user-form";
    }

    /**
     * 校验用户名
     *
     * @param username    新用户名
     * @param oldUsername 老用户名
     * @return
     */
    @RequestMapping("/checkUserName")
    @ResponseBody
    public boolean checkUserName(String username, String oldUsername) {
        if (StringUtils.isNotBlank(oldUsername) && username.equalsIgnoreCase(oldUsername)) {
            return true;
        }
        UserModel result = this.userService.findByName(username);
        return result == null;
    }

    /**
     *保存更新用户
     * @param user
     * @return
     */
    @Log("保存更新用户")
    @RequiresPermissions("user:edit")
    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    public ResponseResult saveOrUpdate(UserModel user) {
        //检验用户名是否重复
        if (null != user.getUserId()) {
            UserModel oldUser = this.userService.findById(user.getUserId());
            if (!checkUserName(user.getUsername(), oldUser.getUsername())) {
                return ResponseResult.error("当前填写用户名重复，请重新填写");
            }
        } else {
            if (!checkUserName(user.getUsername(), "")) {
                return ResponseResult.error("当前填写用户名重复，请重新填写");
            }
            if (!user.getPassword().equals(user.getUnpassword())) {
                return ResponseResult.error("两次输入的用户名和密码不正确");
            }
        }
        try {
            userService.saveOrUpdateUser(user);
        } catch (Exception e) {
            logger.error("保存用户异常", e);
            return ResponseResult.error("保存用户异常,请联系管理员");
        }
        return ResponseResult.ok("保存成功");
    }

    /**
     * 删除用户信息
     *
     * @param id 用户id
     * @return
     */
    @Log("删除用户")
    @RequiresPermissions("user:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseResult deleteUsers(Long id) {
        try {
            this.userService.delete(id);
            return ResponseResult.ok("删除用户成功！");
        } catch (Exception e) {
            logger.error("删除用户失败", e);
            return ResponseResult.error("删除用户失败，请联系网站管理员！");
        }
    }

/*    @RequestMapping("/checkPassword")
    @ResponseBody
    public boolean checkPassword(String password) {
        User user = getCurrentUser();
        String encrypt = MD5Utils.encrypt(user.getUsername().toLowerCase(), password);
        return user.getPassword().equals(encrypt);
    }*/

    /**
     * 修改密码
     * @param newPassword
     * @return
     */
    @Log("修改密码")
    @RequestMapping("/updatePassword")
    @ResponseBody
    public ResponseResult updatePassword(String newPassword) {
        try {
            this.userService.updatePassword(newPassword);
            return ResponseResult.ok("更改密码成功！");
        } catch (Exception e) {
            logger.error("修改密码失败", e);
            return ResponseResult.error("更改密码失败，请联系网站管理员！");
        }
    }

    /**
     * 跳转到个人信息修改界面
     *
     * @return
     */
    @RequestMapping("/toUserInfoPage")
    public String toUserInfoPage(Model model) {
        //查询用户信息
        UserModel user = userService.findByName(getCurrentUser().getUsername());
        ;
        //获取用户部门
        user.setDeptName(officeService.findById(user.getDeptId()).getName());
        model.addAttribute("user", user);
        //获取用户角色信息
        List<RoleModel> roleList = roleService.findUserRole(user.getUsername());
        List<String> StringList = new ArrayList<>(5);
        roleList.forEach(roleModel -> {
            StringList.add(roleModel.getRoleName());
        });
        String roles = StringUtils.join(StringList,",");
        model.addAttribute("roles", roles);
        return PAGE_SUFFIX + "/user-info";
    }

    /**
     * 处理头像
     * @return
     */
    @RequestMapping("/dealAvatar")
    public String dealAvatar(Model model) {
        //查询用户信息
        UserModel user = userService.findByName(getCurrentUser().getUsername());
        model.addAttribute("user", user);
        return PAGE_SUFFIX + "/imageclip";
    }

    /**
     * 更新个人信息
     * @param user
     * @return
     */
    @Log("更新个人信息")
    @RequestMapping("/updateUserProfile")
    @ResponseBody
    public ResponseResult updateUserProfile(UserModel user) {
        try {
            if (StringUtils.isNotEmpty(user.getImageBase64())) {
                String fileName = RandomUtil.randomString(16) + ".jpg";
                String savePath = "avatar" + File.separator + user.getUsername();
                user.setAvatar("/file" + File.separator + ImageBase64Util.generateImage(user.getImageBase64(), fileName, savePath));
            }
            this.userService.updateUserProfile(user);
            return ResponseResult.ok("更新个人信息成功！");
        } catch (Exception e) {
            logger.error("更新用户信息失败", e);
            return ResponseResult.error("更新用户信息失败，请联系网站管理员！");
        }
    }

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    @RequestMapping("/getUserProfile")
    @ResponseBody
    public ResponseResult getUserProfile(Long userId) {
        try {
            UserModel user = new UserModel();
            user.setUserId(userId);
            return ResponseResult.ok(this.userService.findUserProfile(user));
        } catch (Exception e) {
            logger.error("获取用户信息失败", e);
            return ResponseResult.error("获取用户信息失败，请联系网站管理员！");
        }
    }

    /**
     * 更换头像
     * @param imgName
     * @return
     */
    @Log("更换头像")
    @RequestMapping("/changeAvatar")
    @ResponseBody
    public ResponseResult changeAvatar(String imgName) {
        try {
            String[] img = imgName.split("/");
            String realImgName = img[img.length - 1];
            UserModel user = getCurrentUser();
            user.setAvatar(realImgName);
            this.userService.updateNotNull(user);
            return ResponseResult.ok("更新头像成功！");
        } catch (Exception e) {
            logger.error("更换头像失败", e);
            return ResponseResult.error("更新头像失败，请联系网站管理员！");
        }
    }
}

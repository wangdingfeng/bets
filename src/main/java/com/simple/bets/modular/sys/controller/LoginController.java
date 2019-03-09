package com.simple.bets.modular.sys.controller;

import com.simple.bets.core.common.util.IPUtils;
import com.simple.bets.core.common.util.MD5Utils;
import com.simple.bets.core.base.controller.BaseController;
import com.simple.bets.core.annotation.Log;
import com.simple.bets.core.base.model.Tree;
import com.simple.bets.modular.sys.model.MenuModel;
import com.simple.bets.modular.sys.model.UserModel;
import com.simple.bets.modular.sys.service.MenuService;
import com.simple.bets.modular.sys.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 登录
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;

    /**
     * 跳转登录页
     *
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "modular/login";
    }

    /**
     * 登录
     *
     * @return
     */
    @Log("用户登录")
    @PostMapping("/login")
    public String login(String username, String password, Boolean rememberMe, Model model, HttpServletRequest request) {
        // 密码 MD5 加密
        password = MD5Utils.encryptBasedDes(username.toLowerCase() + password);
        if (null == rememberMe) rememberMe = false;
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        try {
            Subject subject = getSubject();
            //如果已存在  退出账号重新登录
            if (subject != null) subject.logout();
            super.login(token);
            this.userService.updateLoginInfo(username,request);
            return "redirect:/index";
        } catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e) {
            logger.info(e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "login";
        } catch (AuthenticationException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    /**
     * 默认访问 主页
     *
     * @return
     */
    @RequestMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }

    /**
     * 欢迎页
     *
     * @return
     */
    @RequestMapping("/welcome")
    public String welcome() {
        return "modular/welcome";
    }

    /**
     * 登录成功 跳转页面
     *
     * @param model
     * @return
     */
    @Log("访问系统")
    @RequestMapping("/index")
    public String index(Model model) {
        // 登录成后，即可通过 Subject 获取登录的用户信息
        UserModel user = super.getCurrentUser();
        model.addAttribute("user", user);
        //获取当前用户菜单
        List<Tree<MenuModel>> menu = menuService.getUserMenu(user.getUsername());
        model.addAttribute("menu", menu);
        return "modular/index";
    }
}

package com.simple.bets.modular.sys.controller;

import com.simple.bets.core.common.util.MD5Utils;
import com.simple.bets.core.controller.BaseController;
import com.simple.bets.core.annotation.Log;
import com.simple.bets.modular.sys.model.User;
import com.simple.bets.modular.sys.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController extends BaseController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String username, String password, String code, Boolean rememberMe,Model model) {
/*        if (!StringUtils.isNotBlank(code)) {
            return ResponseBo.warn("验证码不能为空！");
        }
        Session session = super.getSession();
        String sessionCode = (String) session.getAttribute(CODE_KEY);
        if (!code.equalsIgnoreCase(sessionCode)) {
            return ResponseBo.warn("验证码错误！");
        }*/
        // 密码 MD5 加密
        password = MD5Utils.encrypt(username.toLowerCase(), password);
        if(null == rememberMe){
            rememberMe = false;
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        try {
            Subject subject = getSubject();
            if (subject != null)
                subject.logout();
            super.login(token);
            this.userService.updateLoginTime(username);
            return "redirect:/index";
        } catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e) {
            model.addAttribute("error",e.getMessage());
            return "login";
        } catch (AuthenticationException e) {
            model.addAttribute("error","认证失败！");
            return "login";
        }
    }

    @RequestMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }

    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome";
    }

    @GetMapping("/403")
    public String forbid() {
        return "403";
    }

    @Log("访问系统")
    @RequestMapping("/index")
    public String index(Model model) {
        // 登录成后，即可通过 Subject 获取登录的用户信息
        User user = super.getCurrentUser();
        model.addAttribute("user", user);
        return "index";
    }
}

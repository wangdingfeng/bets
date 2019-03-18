package com.simple.bets;

import com.simple.bets.modular.sys.utils.MailUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: bets
 * @Package: com.simple.bets
 * @ClassName: MailTest
 * @Author: wangdingfeng
 * @Description: ${description}
 * @Date: 2019/3/18 17:28
 * @Version: 1.0
 */
public class MailTest extends BaseTest{

    @Value("${spring.mail.username}")
    private String Sender; //读取配置文件中的参数

    //发送html 邮件
    @Test
    public void sendHtmlMail() {
        StringBuffer sb = new StringBuffer();
        sb.append("<h1>大标题-h1</h1>")
                .append("<p style='color:#F00'>你个大傻子</p>")
                .append("<p style='color:#F00'>不好好上班玩什么邮箱</p>")
                .append("<p style='text-align:right'>致：傻子</p>");
                MailUtils.sendSimpleMail(Sender,new String[]{Sender},"主题：模板邮件",sb.toString(),false);
    }
    //发送带静态附件的邮件
    @Test
    public void sendAttachmentsMail() {
        MailUtils.sendSimpleMail(Sender,new String[]{Sender},"标题：测试邮件发送内容","这是一个附件",true);
    }

    //发送带模板的邮件
    @Test
    public void sendTemplateMail(){
        Map<String, Object> model = new HashMap<>();
        model.put("username", "wangdingfeng");
        MailUtils.sendTemplateMail(Sender,new String[]{Sender},"主题：模板邮件",null,null,"src/main/resources/templates/mail.html",model);
    }
    @Test
    public void test(){
        Map<String, Object> model = new HashMap<>();
        model.put("username", "wangdingfeng");
        MailUtils.sendTemplateMail(Sender,new String[]{Sender},"主题：模板邮件",null,null,"src/main/resources/templates/mail.html",model);
    }

}

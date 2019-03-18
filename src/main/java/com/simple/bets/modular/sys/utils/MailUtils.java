package com.simple.bets.modular.sys.utils;

import com.simple.bets.core.common.lang.StringUtils;
import com.simple.bets.core.common.util.SpringContextUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.FileResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

/**
 * @ProjectName: bets
 * @Package: com.simple.bets.modular.sys.utils
 * @ClassName: MailUtils
 * @Author: wangdingfeng
 * @Description: 邮件发送工具类
 * @Date: 2019/3/18 17:47
 * @Version: 1.0
 */
@Component
public class MailUtils {

    private static Logger logger = LoggerFactory.getLogger(MailUtils.class);

    private static JavaMailSender mailSender = SpringContextUtils.getBean(JavaMailSender.class);

    @Value("${spring.mail.username}")
    private  static String Sender; //读取配置文件中的参数

    /**
     * 发送邮件
     * @param fromAccount  发件箱  不配置 默认为系统发送
     * @param toAccount 接受账户邮件地址
     * @param title 主题
     * @param content 内容
     * @param html 内容是否是html格式
     */
    public static void  sendSimpleMail(String fromAccount,String[] toAccount,String title,String content,boolean html){
        logger.info("发送邮件,接受用户邮件:"+toAccount.toString());
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            installMail(helper,fromAccount,toAccount,title,content,html);
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("发送普通邮件失败，情查看错误日志",e);
        }
    }

    /**
     * 发送邮件 带附件的邮件
     * @param fromAccount  发件箱  不配置 默认为系统发送
     * @param toAccount 接受账户邮件地址
     * @param title 主题
     * @param content 内容
     * @param html 内容是否是html格式
     * @param file 文件
     * @param fileName 文件名
     */
    public static void sendFileMail(String fromAccount,String[] toAccount,String title,String content,boolean html,File file,String fileName){
        logger.info("发送邮件,接受用户邮件:"+toAccount.toString());
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            installMail(helper,fromAccount,toAccount,title,content,html);
            //注意项目路径问题，自动补用项目路径
            FileSystemResource fileSystemResource = new FileSystemResource(file);
            //加入邮件
            helper.addAttachment(fileName, fileSystemResource);
            mailSender.send(message);
        } catch (Exception e){
            logger.error("发送普通邮件失败，情查看错误日志",e);
        }
    }

    /**
     * 发送模板 邮件
     * @param fromAccount  发件箱  不配置 默认为系统发送
     * @param toAccount 接受账户邮件地址
     * @param title 主题
     * @param file 文件
     * @param fileName 文件名
     * @param templatePath 模板路径
     * @param params 参数
     */
    public static void sendTemplateMail(String fromAccount, String[] toAccount, String title, File file, String fileName, String templatePath, Map<String,Object> params){
        logger.info("发送邮件,接受用户邮件:"+toAccount.toString());
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //读取 html 模板
            FileResourceLoader resourceLoader = new FileResourceLoader();
            Configuration cfg = Configuration.defaultConfiguration();
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
            Template template = gt.getTemplate(templatePath);
            //绑定参数
            template.binding(params);
            String html = template.render();
            installMail(helper,fromAccount,toAccount,title,html,true);
            if(null != file){
                //注意项目路径问题，自动补用项目路径
                FileSystemResource fileSystemResource = new FileSystemResource(file);
                //加入邮件
                helper.addAttachment(fileName, fileSystemResource);
            }
            mailSender.send(message);
        } catch (Exception e){
            logger.error("发送普通邮件失败，情查看错误日志",e);
        }
    }

    /**
     * 加载发送参数
     * @param helper
     * @param fromAccount  发件箱  不配置 默认为系统发送
     * @param toAccount 接受账户邮件地址
     * @param title 主题
     * @param content 内容
     * @param html 内容是否是html格式
     * @throws MessagingException
     */
    private static void installMail(MimeMessageHelper helper,String fromAccount,String[] toAccount,String title,String content,boolean html) throws MessagingException {
        helper.setFrom(StringUtils.isNotBlank(fromAccount) ? fromAccount:Sender);
        helper.setTo(toAccount);
        helper.setSubject(title);
        helper.setText(content,html);
    }

}

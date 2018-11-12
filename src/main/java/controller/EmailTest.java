package controller;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author lurenjia
 * @date 2018/11/12
 */
@RestController
@RequestMapping("/v1")
public class EmailTest {

    @Value("${spring.mail.auth}")
    private String auth;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.protocol}")
    private String protocol;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String authName;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.ssl}")
    private boolean isSSL;
    @Value("${spring.mail.default-encoding}")
    private String charset;
    @Value("${spring.mail.timeout}")
    private String timeout;

    @GetMapping("/sendEmail")
    public String sendEmail() {
        boolean isSend = sendEmail("这是一封测试邮件",
                new String[]{"836837404@qq.com"},
                null,
                "asdasd",
                null);
        return "发送邮件:" + isSend;
    }

    /**
     * 发送邮件
     *
     * @param subject     主题
     * @param toUsers     收件人
     * @param ccUsers     抄送
     * @param content     邮件内容
     * @param attachfiles 附件列表  List<Map<String, String>> key: name && file
     */
    private boolean sendEmail(String subject, String[] toUsers, String[] ccUsers, String content, List<Map<String, String>> attachfiles) {
        boolean flag = true;
        try {
            System.out.println(host + "" + authName + password);
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost(host);
            javaMailSender.setUsername(authName);
            javaMailSender.setPassword(password);
            javaMailSender.setDefaultEncoding(charset);
            javaMailSender.setProtocol(protocol);
            javaMailSender.setPort(port);

            Properties properties = new Properties();
            properties.setProperty("mail.smtp.auth", auth);
            properties.setProperty("mail.smtp.timeout", timeout);
            if (isSSL) {
                MailSSLSocketFactory sf = null;
                try {
                    sf = new MailSSLSocketFactory();
                    sf.setTrustAllHosts(true);
                    properties.put("mail.smtp.ssl.enable", "true");
                    properties.put("mail.smtp.ssl.socketFactory", sf);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
            javaMailSender.setJavaMailProperties(properties);

            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);
            messageHelper.setTo(toUsers);
            if (ccUsers != null && ccUsers.length > 0) {
                messageHelper.setCc(ccUsers);
            }
            System.out.println(authName);
            messageHelper.setFrom(authName);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);

            if (attachfiles != null && attachfiles.size() > 0) {
                for (Map<String, String> attachfile : attachfiles) {
                    String attachfileName = attachfile.get("name");
                    File file = new File(attachfile.get("file"));
                    messageHelper.addAttachment(attachfileName, file);
                }
            }
            javaMailSender.send(mailMessage);

        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

}

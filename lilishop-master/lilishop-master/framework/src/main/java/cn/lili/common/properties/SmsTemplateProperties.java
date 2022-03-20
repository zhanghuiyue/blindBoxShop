package cn.lili.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 短信模版设置
 *
 * @author Chopper
 */
@Data
@Configuration
public class SmsTemplateProperties {
    /**
     * 登录
     */
    private String lOGIN = "您的短信验证码为：@";
    /**
     * 注册
     */
    private String REGISTER = "您的短信验证码为：@";
    /**
     * 找回密码
     */
    private String FIND_USER = "您的短信验证码为：@";
    /**
     * 设置密码
     */
    private String UPDATE_PASSWORD = "您的短信验证码为：@";
    /**
     * 设置支付密码
     */
    private String WALLET_PASSWORD = "您的短信验证码为：@";
}

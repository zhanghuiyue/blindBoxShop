package cn.lili.modules.system.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 253短信配置
 * 这里在前台不做调整，方便客户直接把服务商的内容配置在我们平台
 * @author 李磊
 * @since 2022/03/20 15:23
 */
@Data
public class Sms253Setting implements Serializable {
    /**
     * 从上到下yi依次是
     * 节点地址
     * 账号
     * 秘钥
     * 签名
     */
    private String sendUrl;

    private String accessKeyId;

    private String accessSecret;

    private String signName;
}

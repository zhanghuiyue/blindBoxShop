package cn.lili.modules.sms.entity.dos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsSendResponse {
    /**
     * 响应时间
     */
    private String time;
    /**
     * 消息id
     */
    private String msgId;
    /**
     * 状态码说明（成功返回空）
     */
    private String errorMsg;
    /**
     * 状态码（详细参考提交响应状态码）
     */
    private String code;
}
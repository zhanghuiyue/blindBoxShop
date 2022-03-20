package cn.lili.modules.sms.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.lili.cache.Cache;
import cn.lili.cache.CachePrefix;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.properties.SmsTemplateProperties;
import cn.lili.common.properties.SystemSettingProperties;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.Base64Utils;
import cn.lili.common.utils.CommonUtil;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.service.MemberService;

import cn.lili.modules.sms.SmsUtil;
import cn.lili.modules.sms.entity.dos.SmsSendRequest;
import cn.lili.modules.sms.entity.dos.SmsSendResponse;
import cn.lili.modules.sms.entity.dos.SmsSign;
import cn.lili.modules.sms.entity.dos.SmsTemplate;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.entity.dto.Sms253Setting;
import cn.lili.modules.system.entity.dto.SmsSetting;
import cn.lili.modules.system.entity.enums.SettingEnum;
import cn.lili.modules.system.service.SettingService;
import cn.lili.modules.verification.entity.enums.VerificationEnums;
import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.google.gson.Gson;
import com.xkcoding.http.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短信网管阿里云实现
 *
 * @author Chopper
 * @version v4.0
 * @since 2020/11/30 15:44
 */
@Component
@Slf4j
public class SmsUtil253ImplService implements SmsUtil {

    @Autowired
    private Cache cache;
    @Autowired
    private SettingService settingService;
    @Autowired
    private MemberService memberService;

    @Autowired
    private SmsTemplateProperties smsTemplateProperties;

    @Autowired
    private SystemSettingProperties systemSettingProperties;

    @Override
    public void sendSmsCode(String mobile, VerificationEnums verificationEnums, String uuid) {
        //获取253短信配置
        Setting setting = settingService.get(SettingEnum.SMS253_SETTING.name());
        if (StrUtil.isBlank(setting.getSettingValue())) {
            throw new ServiceException(ResultCode.ALI_SMS253_SETTING_ERROR);
        }
        Sms253Setting sms253Setting = new Gson().fromJson(setting.getSettingValue(), Sms253Setting.class);

        //验证码
        String code = CommonUtil.getRandomNum();

        //发送内容 默认为登录验证
        String content;

        //如果某个模版需要自定义，则在此处进行调整
        switch (verificationEnums) {
            //登录
            case LOGIN: {
                content =sms253Setting.getSignName()+ smsTemplateProperties.getLOGIN();
                content = content.replace("@", code);
                break;
            }
            //注册
            case REGISTER: {
                content = sms253Setting.getSignName()+smsTemplateProperties.getREGISTER();
                content = content.replace("@", code);
                break;
            }
            //找回密码
            case FIND_USER: {
                content = sms253Setting.getSignName()+smsTemplateProperties.getFIND_USER();
                content = content.replace("@", code);
                break;
            }
            //修改密码
            case UPDATE_PASSWORD: {
                Member member = memberService.getById(UserContext.getCurrentUser().getId());
                if (member == null || StringUtil.isEmpty(member.getMobile())) {
                    return;
                }
                //更新为用户最新手机号
                mobile = member.getMobile();
                content =sms253Setting.getSignName()+ smsTemplateProperties.getUPDATE_PASSWORD();
                content = content.replace("@", code);
                break;
            }
            //设置支付密码
            case WALLET_PASSWORD: {
                Member member = memberService.getById(UserContext.getCurrentUser().getId());
                //更新为用户最新手机号
                mobile = member.getMobile();
                content =sms253Setting.getSignName()+ smsTemplateProperties.getWALLET_PASSWORD();
                content = content.replace("@", code);
                break;
            }
            //如果不是有效的验证码手段，则此处不进行短信操作
            default:
                return;
        }
        //如果是测试模式 默认验证码 6个1
        if (systemSettingProperties.getIsTestModel()) {
            code = "111111";
        } else {
            //发送短信
           this.sendSmsCode(content , mobile,sms253Setting.getAccessKeyId() ,sms253Setting.getAccessSecret(),sms253Setting.getSendUrl() );
        }
        //缓存中写入要验证的信息
        cache.put(cacheKey(verificationEnums, mobile, uuid), code, 300L);
    }

    @Override
    public boolean verifyCode(String mobile, VerificationEnums verificationEnums, String uuid, String code) {
        Object result = cache.get(cacheKey(verificationEnums, mobile, uuid));
        if (code.equals(result)) {
            //校验之后，删除
            cache.remove(cacheKey(verificationEnums, mobile, uuid));
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void sendSmsCode(String content, String mobile, String account ,String password ,String sendUrl) {

        //状态报告
        String report= "true";
        SmsSendRequest smsSingleRequest = new SmsSendRequest(account, password, content, mobile,report);
        String requestJson = JSON.toJSONString(smsSingleRequest);
        log.info("======requestString======= " + requestJson);
        String response = this.sendSmsByPost(requestJson ,sendUrl);
        if(response != null && !response.equals("")){
            log.info("======responseString======" + response);
            SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
            log.error(smsSingleResponse.getErrorMsg());
        }

    }


    @Override
    public void sendBatchSms(String signName, List<String> mobile, String templateCode) {



        List<String> sign = new ArrayList<String>();

        sign.addAll(mobile);
        sign.replaceAll(e -> signName);

        //手机号拆成多个小组进行发送
        List<List<String>> mobileList = new ArrayList<>();

        //签名名称多个小组
        List<List<String>> signNameList = new ArrayList<>();

        //循环分组
        for (int i = 0; i < (mobile.size() / 100 + (mobile.size() % 100 == 0 ? 0 : 1)); i++) {
            int endPoint = Math.min((100 + (i * 100)), mobile.size());
            mobileList.add(mobile.subList((i * 100), endPoint));
            signNameList.add(sign.subList((i * 100), endPoint));
        }

//       //发送短信
        for (int i = 0; i < mobileList.size(); i++) {
            SendBatchSmsRequest sendBatchSmsRequest = new SendBatchSmsRequest()
                    .setPhoneNumberJson(JSONUtil.toJsonStr(mobileList.get(i)))
                    .setSignNameJson(JSONUtil.toJsonStr(signNameList.get(i)))
                    .setTemplateCode(templateCode);
            try {
               /* client.sendBatchSms(sendBatchSmsRequest);*/
            } catch (Exception e) {
                log.error("批量发送短信错误", e);
            }
        }

    }



    public static String sendSmsByPost(String postContent , String sendUrl) {

        try {
            URL url = new URL(sendUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            httpURLConnection.setConnectTimeout(10000);//连接超时 单位毫秒
            httpURLConnection.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            httpURLConnection.connect();
            OutputStream os=httpURLConnection.getOutputStream();
            os.write(postContent.getBytes("UTF-8"));
            os.flush();

            StringBuilder sb = new StringBuilder();
            int httpRspCode = httpURLConnection.getResponseCode();
            if (httpRspCode == HttpURLConnection.HTTP_OK) {
                // 开始获取数据
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                return sb.toString();
            }
        } catch (Exception e) {
            log.warn("Ignore this exception"+e);
        }
        return null;
    }
    /**
     * 生成缓存key
     *
     * @param verificationEnums 验证场景
     * @param mobile            手机号码
     * @param uuid              用户标识 uuid
     * @return
     */
    static String cacheKey(VerificationEnums verificationEnums, String mobile, String uuid) {
        return CachePrefix.SMS_CODE.getPrefix() + verificationEnums.name() + uuid + mobile;
    }
}

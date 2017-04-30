package com.foxhunter.common.micro;

import com.foxhunter.common.config.AppConfig;
import com.foxhunter.common.util.JSonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

/**
 * 微信访问类。
 *
 * @author Ewing
 */
public class MicroAccessor {
    private static Logger logger = LoggerFactory.getLogger(MicroAccessor.class);

    private MicroAccessor() {
    }

    /**
     * 发送 GET 请求（HTTP）带参数。
     */
    public static String httpGet(String url, Map<String, Object> params) {
        String apiUrl = url;
        String result = null;
        StringBuilder param = new StringBuilder();
        try {
            boolean first = true;
            for (String key : params.keySet()) {
                if (first && !apiUrl.contains("?")) {
                    param.append("?");
                    first = false;
                } else {
                    param.append("&");
                }
                param.append(key).append("=").append(params.get(key));
            }
            apiUrl += param.toString();
            HttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(apiUrl);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, "UTF-8");
            }
        } catch (Exception e) {
            logger.error("Http访问异常：" + e.getMessage());
        }
        return result;
    }

    /**
     * 授权码登陆到微信（小程序专用）。
     */
    public static MicroAccessResult authForMicroApp(String code,String source) {
    	if(source!=null&&"1".equals(source)){
    		MicroAccessResult microAccessResult = new MicroAccessResult();
    		microAccessResult.setOpenid(code);
    		microAccessResult.setSession_key(code);
    		return microAccessResult;
    	}
        Map<String, Object> params = new HashMap<>();
        params.put("appid", AppConfig.WEIXIN_APPID);
        params.put("secret", AppConfig.WEIXIN_SECRET);
        params.put("grant_type", "authorization_code");
        params.put("js_code", code);
        String result = httpGet("https://api.weixin.qq.com/sns/jscode2session", params);
        return JSonUtils.readValue(result, MicroAccessResult.class);
    }
    
    public static boolean isMobile(String str) {
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
    /**
     * 授权码登陆到微信。
     */
    public static MicroAccessResult authToMicro(String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("appid", AppConfig.WEIXIN_ANDROID_ID);
        params.put("secret", AppConfig.WEIXIN_ANDROID_SECRET);
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        String result = httpGet("https://api.weixin.qq.com/sns/oauth2/access_token", params);
        return JSonUtils.readValue(result, MicroAccessResult.class);
    }

    /**
     * 获取微信用户信息。
     */
    public static MicroAccessResult getMicroUserInfo(String accessToken, String openid) {
        Map<String, Object> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("openid", openid);
        String result = httpGet("https://api.weixin.qq.com/sns/userinfo", params);
        return JSonUtils.readValue(result, MicroAccessResult.class);
    }

}

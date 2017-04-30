package com.foxhunter.common.util;

/**
 * 发送红包实体类
 *
 * @author xiuxiu
 */
public class SendRedPackPo {

    //32位随机产生的字符串
    private String nonce_str;
    //签名
    private String sign;
    //商户订单号
    private String mch_billno;
    //商户号
    private String mch_id;
    //子商户号
    private String sub_mch_id;
    //公众账号appid
    private String wxappid;
    //提供方名称
    private String nick_name;
    //商户名称
    private String send_name;
    //用户openid
    private String re_openid;
    //付款金额
    private String total_amount;
    //最小红包金额
    private String min_value;
    //最大红包金额
    private String max_value;
    //红包发放总人数
    private String total_num;
    //红包祝福语
    private String wishing;
    //Ip地址
    private String client_ip;
    //活动名称
    private String act_name;
    //备注
    private String remark;
    //商户logo的url
    private String logo_imgurl;
    //分享文案
    private String share_content;
    //分享链接
    private String share_url;
    //分享的图片
    private String share_imgurl;


    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMch_billno() {
        return mch_billno;
    }

    public void setMch_billno(String mch_billno) {
        this.mch_billno = mch_billno;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getSub_mch_id() {
        return sub_mch_id;
    }

    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }

    public String getWxappid() {
        return wxappid;
    }

    public void setWxappid(String wxappid) {
        this.wxappid = wxappid;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getSend_name() {
        return send_name;
    }

    public void setSend_name(String send_name) {
        this.send_name = send_name;
    }

    public String getRe_openid() {
        return re_openid;
    }

    public void setRe_openid(String re_openid) {
        this.re_openid = re_openid;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getMin_value() {
        return min_value;
    }

    public void setMin_value(String min_value) {
        this.min_value = min_value;
    }

    public String getMax_value() {
        return max_value;
    }

    public void setMax_value(String max_value) {
        this.max_value = max_value;
    }

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public String getWishing() {
        return wishing;
    }

    public void setWishing(String wishing) {
        this.wishing = wishing;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public String getAct_name() {
        return act_name;
    }

    public void setAct_name(String act_name) {
        this.act_name = act_name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLogo_imgurl() {
        return logo_imgurl;
    }

    public void setLogo_imgurl(String logo_imgurl) {
        this.logo_imgurl = logo_imgurl;
    }

    public String getShare_content() {
        return share_content;
    }

    public void setShare_content(String share_content) {
        this.share_content = share_content;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getShare_imgurl() {
        return share_imgurl;
    }

    public void setShare_imgurl(String share_imgurl) {
        this.share_imgurl = share_imgurl;
    }

}

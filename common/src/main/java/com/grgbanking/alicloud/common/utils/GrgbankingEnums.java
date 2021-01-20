package com.grgbanking.alicloud.common.utils;

/**
 * @Author machao
 * @Date 2017/10/10 10:57
 * 全局错误信息枚举类
 **/
public class GrgbankingEnums {
    public enum GrgbankingCommEnums {
        //成功
        SUCCESS("0", "成功"),
        FAIL("1", "失败"),
        EXCEPTION_FAIL("-1","程序异常"),
        RUNTIME_EXCEPTION_FAIL("-1", "程序异常"),
        TOKEN_EXPIRED_FAIL("-80001", "Token 已过期."),
        TOKEN_UNSUPPORTED_FAIL("-80003", "Token 接收特定格式/配置的JWT时引发异常，该格式/配置与应用程序预期的格式不匹配。."),
        TOKEN_INCORRECTLY_FAIL("-80004", "Token 不合法."),
        TOKEN_SIGNATURE_FAIL("-80005", "Token 计算签名或验证JWT的现有签名失败的异常."),
        TOKEN_ILLEGAL_ARGUMENT_FAIL("-80006", "Token 传递非法或不适当的参数."),
        PARAMS_FAIL("10001", "参数异常"),
        LOGIN_FAIL("100010","登录失败,用户名或密码错误,剩余重试次数：%s次"),
        LOGIN_FAIL_("100011","登录失败,请重新登录"),
        API_SWITCH_OFF("10002", "接口暂时关闭，无法调用"),
        LOGIN_EXPIRE_TIME_NOT_ARRIVE("100020","密码错误5次,请过%s分钟再登录"),
        JSON_TRANSLATE_FAIL("10003", "requestData数据解析失败"),
        JSON_TRANSLATE_ERROR("-10003", "requestData数据解析异常"),
        SAVE_IMG_TO_LOCAL("-10004","保存图片到本地异常"),
        IMAGE_TYPE_ERROR("-10005","图片格式错误"),
        IMAGE_EXISTS("-10006","该名称图片已经存在"),
        UNABLE_GET_IMAGE_URL("-10007","提供的图片URL无法访问"),
        TO_MANY_ITEMS("10004", "数据超过限制条数，接口每次接收最大条数为：?"),
        TEMPLATE_HANDLE_ERROR("20001","模板消息事件推送结果XML处理失败"),
        OUT_OF_MEMORY_ERROR("99999","压力过大，请稍后再试"),
        ;
        public String code;
        public String message;


        GrgbankingCommEnums(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

}

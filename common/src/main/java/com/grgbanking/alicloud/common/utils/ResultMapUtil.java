package com.grgbanking.alicloud.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author machao
 * @Date 2019/3/21 16:25
 **/
public class ResultMapUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResultMapUtil.class);
    public static ResultMap success(List<String> dataKey, List<Object> data) {
        ResultMap returnMap = new ResultMap();
        returnMap.success();
        for (int i = 0; i < dataKey.size(); i++) {
            returnMap.getData().put(dataKey.get(i), data.get(i));
        }
        return returnMap;
    }

    public static ResultMap success(String dataKey, Object data) {
        ResultMap returnMap = new ResultMap();
        returnMap.success();
        returnMap.getData().put(dataKey, data);
        return returnMap;
    }

    public static ResultMap success(String message) {
        ResultMap returnMap = new ResultMap();
        returnMap.setRetcode(GrgbankingEnums.GrgbankingCommEnums.SUCCESS.getCode());
        returnMap.setRetmsg(message);
        return returnMap;
    }

    public static ResultMap success() {
        ResultMap returnMap = new ResultMap();
        returnMap.success();
        return returnMap;
    }

    public static ResultMap success(Object obj) {
        ResultMap returnMap = new ResultMap();
        returnMap.success();
        returnMap.getData().put(obj.getClass().getSimpleName(), obj);
        return returnMap;
    }

    public static ResultMap error(GrgbankingEnums.GrgbankingCommEnums enumName) {
        ResultMap returnMap = new ResultMap();
        returnMap.setRetcode(enumName.getCode());
        returnMap.setRetmsg(enumName.getMessage());
        logger.error(enumName.getCode() + " ---> " + enumName.getMessage());
        return returnMap;
    }

    public static ResultMap error(GrgbankingEnums.GrgbankingCommEnums enumName, String retmsg) {
        ResultMap returnMap = new ResultMap();
        returnMap.setRetcode(enumName.getCode());
        returnMap.setRetmsg(retmsg);
        logger.error(enumName.getCode() + " ---> " + retmsg);
        return returnMap;
    }

    public static ResultMap error(ResultMap resultMap) {
        ResultMap returnMap = new ResultMap();
        returnMap.setRetcode(resultMap.getRetcode());
        returnMap.setRetmsg(resultMap.getRetmsg());
        logger.error(resultMap.getRetcode() + " ---> " + resultMap.getRetmsg());
        return returnMap;
    }

    public static ResultMap error(Exception e) {
        ResultMap returnMap = new ResultMap();
        returnMap.setRetcode(GrgbankingEnums.GrgbankingCommEnums.EXCEPTION_FAIL.getCode());
        returnMap.setRetmsg(GrgbankingEnums.GrgbankingCommEnums.EXCEPTION_FAIL.getMessage());
        logger.error(returnMap.getRetcode() + " ---> " + returnMap.getRetmsg(), e);
        return returnMap;
    }

    public static ResultMap exception(GrgbankingEnums.GrgbankingCommEnums enumName, Exception e) {
        ResultMap returnMap = new ResultMap();
        returnMap.setRetcode(enumName.getCode());
        returnMap.setRetmsg(e);
        logger.error(enumName.getCode() + " ---> " + (StringUtils.isEmpty(e.getMessage()) ? e.toString() : e.getMessage()));
        return returnMap;
    }

    public static ResultMap exception(Exception e) {
        ResultMap returnMap = new ResultMap();
        returnMap.setRetcode(GrgbankingEnums.GrgbankingCommEnums.EXCEPTION_FAIL.getCode());
        returnMap.setRetmsg(e);
        logger.error(GrgbankingEnums.GrgbankingCommEnums.EXCEPTION_FAIL.getCode() + " ---> " + (StringUtils.isEmpty(e.getMessage()) ? e.toString() : e.getMessage()));
        return returnMap;
    }
}

package com.grgbanking.alicloud.user.auth;

import com.alibaba.fastjson.JSON;
import com.grgbanking.alicloud.comm.service.ContextHolderUtil;
import com.grgbanking.alicloud.comm.service.JwtOperator;
import com.grgbanking.alicloud.common.utils.GrgbankingEnums;
import com.grgbanking.alicloud.common.utils.ResultMap;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * @author machao
 * 权限切面
 */
@Aspect
@Component
public class AuthAspect {
    public static final Logger logger = LoggerFactory.getLogger(AuthAspect.class);
    @Autowired
    private JwtOperator jwtOperator;

    @Around("@annotation(com.grgbanking.alicloud.user.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        ResultMap resultMap = new ResultMap();
        HttpServletResponse response = ContextHolderUtil.getResponse();
        HttpServletRequest request = ContextHolderUtil.getRequest();
        String token = request.getHeader("X-Token");
        logger.info("获取到的 X-Token：{}", token);
        try {
            if(jwtOperator.isTokenExpired(token)){
                return joinPoint.proceed();
            }else{
                resultMap.setRetcodeRetmsg(GrgbankingEnums.GrgbankingCommEnums.TOKEN_EXPIRED_FAIL);
            }
        } catch (ExpiredJwtException e) {
            logger.warn("token 已过期：{}", token);
            resultMap.setRetcodeRetmsg(GrgbankingEnums.GrgbankingCommEnums.TOKEN_EXPIRED_FAIL);
        } catch (UnsupportedJwtException e) {
            logger.error(GrgbankingEnums.GrgbankingCommEnums.TOKEN_UNSUPPORTED_FAIL.getMessage(), e);
            resultMap.setRetcodeRetmsg(GrgbankingEnums.GrgbankingCommEnums.TOKEN_UNSUPPORTED_FAIL);
        } catch (MalformedJwtException e) {
            logger.warn("Token 不合法：{}", token);
            resultMap.setRetcodeRetmsg(GrgbankingEnums.GrgbankingCommEnums.TOKEN_INCORRECTLY_FAIL);
        } catch (SignatureException e) {
            logger.warn(GrgbankingEnums.GrgbankingCommEnums.TOKEN_SIGNATURE_FAIL.getMessage());
            resultMap.setRetcodeRetmsg(GrgbankingEnums.GrgbankingCommEnums.TOKEN_SIGNATURE_FAIL);
        } catch (IllegalArgumentException e) {
            logger.warn(GrgbankingEnums.GrgbankingCommEnums.TOKEN_ILLEGAL_ARGUMENT_FAIL.getMessage());
            resultMap.setRetcodeRetmsg(GrgbankingEnums.GrgbankingCommEnums.TOKEN_ILLEGAL_ARGUMENT_FAIL);
        } /*catch (Exception e) {
            logger.error("token解析异常", e);
            map.put("resCode", -106);
            map.put("resMsg", "Token 解析异常.");
        }*/
        assert response != null;
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType("application/json");
        response.getWriter().write(JSON.toJSONString(resultMap));
        return null;
//        return joinPoint.proceed();
    }

}

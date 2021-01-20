package com.grgbanking.alicloud.content.interceptor;

import com.grgbanking.alicloud.comm.service.ContextHolderUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * 通过
 * @author machao
 */
public class TokenRelayRequestFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest request = ContextHolderUtil.getRequest();
        String token = request.getHeader("X-Token");
        template.header("X-Token", token);
    }
}

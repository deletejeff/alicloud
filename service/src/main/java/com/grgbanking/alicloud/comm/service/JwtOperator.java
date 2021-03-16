package com.grgbanking.alicloud.comm.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author machao
 * jsonwebtoken工具类,生成token
 */
@Component
public class JwtOperator {
    public static final Logger logger = LoggerFactory.getLogger(JwtOperator.class);

    /**
     * 密钥
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * token的有效期
     */
    @Value("${jwt.expire-time-in-second}")
    private Long expireTimeInSeconds;


    /**
     * 生成token
     * @param claims 参数，类似用户ID，用户信息等
     * @return token
     */
    public String generateToken(Map<String, Object> claims){
        Date createDate = new Date();
        Date expirationTime = new Date(System.currentTimeMillis() + this.expireTimeInSeconds * 1000);
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationTime)
                .setIssuedAt(createDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return token;
    }

    /**
     * token 是否过期
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        Claims claims = getClaims(token);
        Date expiration = claims.getExpiration();
        return new Date().before(expiration);
    }

    /**
     * 获取claims方法
     * @param token
     * @return
     */
    public Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(this.secret)
                .parseClaimsJws(token)
                .getBody();
    }

}

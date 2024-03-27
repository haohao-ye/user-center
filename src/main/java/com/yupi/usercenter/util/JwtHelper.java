package com.yupi.usercenter.util;

import io.jsonwebtoken.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author dkhaohao
 * @Title:
 * @Package
 * @Description:
 * @date 2024/3/114:45
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt.token")
public class JwtHelper {

    /**
     * 有效时间,单位毫秒 1000毫秒 == 1秒
     */
    private long tokenExpiration;
    /**
     * 当前程序签名秘钥
     */
    private String tokenSignKey;


    /**
     生成token字符串
     *
     * @param userId
     * @return
     */
    public String createToken(Long userId) {
        return Jwts.builder()
                .setSubject("YYGH-USER")
                //单位分钟
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration * 1000 * 60))
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
    }


    /**
     * 从token字符串获取userid
     * @param token
     * @return
     */
    public Long getUserId(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer) claims.get("userId");
        return userId.longValue();
    }


    /**
     * 判断token是否有效
     * @param token
     * @return
     */
    public boolean isExpiration(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(tokenSignKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration().before(new Date());
        } catch (Exception e) {
            //过期出现异常，返回true
            return true;
        }
    }
}



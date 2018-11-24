package com.test.jm.util;

import com.test.jm.dto.UserInfoDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;

public class TokenUtils {

    private static Logger logger = LoggerFactory.getLogger(TokenUtils.class);

    private static final String SECRET = "linweili";
    private static final String issuer = "295287765@qq.com";
    private static final String subject = "activity";

    private static Date createExpirationDate(LocalDateTime localDateTime){
        return null;
    }

    /**
     * 生成token
     * @param userid
     * @param expirationdate
     * @return
     */
    public static String createJwtToken(String userid, long expirationdate){

        long ttlMillis = expirationdate * 60 * 1000;

        // 签名算法 ，将对token进行签名
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成签发时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 通过秘钥签名JWT
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(userid)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        if (ttlMillis >= 60000) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 解密token
     * @param token
     * @return
     */
    public static Claims parseJWTToken(String token) {

        Claims claims = null;
        try{
            claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        }catch (Exception e){
//            e.printStackTrace();
            logger.info("token：{}，已失效", token);
            return claims;
        }

    }

    /**
     * 校验token是否过期

    public static boolean validTokenExpiration(String token){
        Date tokenDate = parseJWTToken(token).getExpiration();
        int expirationdate=(int)(new Date().getTime()-tokenDate.getTime())/1000;
        if(expirationdate > 60*60*24*3){
            return false;
        }
        return true;
    }*/



}

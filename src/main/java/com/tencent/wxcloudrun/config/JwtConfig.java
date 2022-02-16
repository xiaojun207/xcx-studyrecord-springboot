package com.tencent.wxcloudrun.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tencent.wxcloudrun.model.WxAccount;
import com.tencent.wxcloudrun.utils.ExpiryMap;
import com.tencent.wxcloudrun.utils.SHAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtConfig {

    // JWT 自定义密钥 我这里写死的
    private static final String SECRET_KEY = "7a113f4daaf338f65e4fa8f24836ca655b27e5d076fa64539fe714d6c5ecb509";
    // 有效期
    private static final long EXPIRE_TIME = 3600 * 24 * 30;

    private ExpiryMap<String, String> expiryMap = new ExpiryMap();

    public void setCache(String key, String value, long expireTime, TimeUnit timeUnit){
        // 这里应该使用redis缓存，但没有准备redis资源，所以用内存暂代
        expiryMap.put(key, value, expireTime * 1000);
    }

    public String getCache(String key){
        return expiryMap.get(key);
    }

    /**
     * 根据微信用户登陆信息创建 token
     * 注 : 这里的token会被缓存到redis中,用作为二次验证
     * redis里面缓存的时间应该和jwt token的过期时间设置相同
     *
     * @param wxAccount 微信用户信息
     * @return 返回 jwt token
     */
    public String createTokenByWxAccount(WxAccount wxAccount) {
        //1 . 加密算法进行签名得到token
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        String token = JWT.create()
                .withClaim("Id", wxAccount.getId())
                .withClaim("nickName", wxAccount.getNickName())
                .withClaim("wxOpenId", wxAccount.getOpenid())
                .withClaim("sessionKey", wxAccount.getSessionKey())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME * 1000))  //JWT 配置过期时间的正确姿势
                .sign(algorithm);
        //2 . Redis缓存JWT, 注 : 请和JWT过期时间一致
        String jwtId = SHAUtils.SHA(token);
        setCache("JWT:" + jwtId, token, EXPIRE_TIME, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 校验token是否正确，如果通过，则自动续期
     * 1 . 根据token解密，先从缓存中查找出cacheToken，匹配是否相同
     * 2 . 然后再对cacheToken进行解密，解密成功则 继续流程 和 进行token续期
     *
     * @param token 密钥
     * @return 返回是否校验通过
     */
    public boolean verifyToken(String token) {
        try {
            String jwtId = SHAUtils.SHA(token);
            //1 . 根据token解密，先从缓存中查找出cacheToken，匹配是否相同
            String cacheToken = getCache("JWT:" + jwtId);
            if (!cacheToken.equals(token))
                return false;
            //2 . 得到算法相同的JWTVerifier
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            DecodedJWT decodedJwt = getDecodedJWTByToken(cacheToken);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("Id", decodedJwt.getClaim("Id").asInt())
                    .withClaim("nickName", decodedJwt.getClaim("nickName").asString())
                    .withClaim("wxOpenId", decodedJwt.getClaim("wxOpenId").asString())
                    .withClaim("sessionKey", decodedJwt.getClaim("sessionKey").asString())
                    .acceptExpiresAt(System.currentTimeMillis() + EXPIRE_TIME * 1000)  //JWT 正确的配置续期姿势
                    .build();
            //3 . 验证token
            verifier.verify(cacheToken);
            //4 . Redis缓存JWT续期
            setCache("JWT:" + jwtId, cacheToken, EXPIRE_TIME, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) { //捕捉到任何异常都视为校验失败
            log.error("verifyToken.err:" + e.getMessage());
            return false;
        }
    }

    public DecodedJWT getDecodedJWTByToken(String token)  {
        return JWT.decode(token);
    }

    /**
     * 根据Token获取载体信息(注意坑点 : 就算token不正确，也有可能解密出wxOpenId,同下)
     */
    public WxAccount getWxAccountByToken(String token){
        DecodedJWT decodedJwt = getDecodedJWTByToken(token);
        WxAccount res = new WxAccount();
        res.setId(decodedJwt.getClaim("Id").asInt());
        res.setNickName(decodedJwt.getClaim("nickName").asString());
        res.setOpenid(decodedJwt.getClaim("wxOpenId").asString());
        res.setSessionKey(decodedJwt.getClaim("sessionKey").asString());
        return res;
    }

}

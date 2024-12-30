package gataway.common;



import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;

public class JWTUtils {

    //生成令牌
    public static String getJWT(Integer outTime, Long userId, String keys){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, outTime);

        String token = JWT.create()
                .withHeader(new HashMap<>())
                .withClaim("userId", userId)
                .withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(keys));
        return token;

    }


    //校验令牌
    public static Long verifyJWT(String token, String keys){
        try {
            JWTVerifier build = JWT.require(Algorithm.HMAC256(keys)).build();
            DecodedJWT decodedJWT = build.verify(token);
            Long userId = 0L;
            userId = decodedJWT.getClaim("userId").asLong();
            return userId;
        }catch (JWTVerificationException e){
            return -1L;
        }

    }


}

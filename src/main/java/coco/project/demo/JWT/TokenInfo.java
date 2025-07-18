package coco.project.demo.JWT;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

// Jwt 유효성 검사 및 생성 username 클레임 추출 //
@Component
public class TokenInfo {

    private static final Logger logger = LoggerFactory.getLogger(TokenInfo.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.ms}")
    private Long jwtExpirationMs;

    // JWT KEY Base64 Decoding //
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /*
        사용자 정보 기반 JWT
        username으로 JWT문자열 생성
     */
    public String generateJwtToken(String username) {
        return String.valueOf(Jwts.builder()
                .setSubject(username) // 토큰 주체
                .setIssuedAt(new Date()) // 발행시간
                .setExpiration(new Date((new Date() ).getTime() + jwtExpirationMs)) // 만료시간
                .signWith(key(), SignatureAlgorithm.HS256) // 비밀 키와 서명 알고리즘으로 서명
                .compact()); // JWT 문자열 압축
    }
    
    /*
        JWT Token에서 username 추출
     */
    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /*
        토큰 유효성 검사
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}
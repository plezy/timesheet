package lu.plezy.timesheet.authentication.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lu.plezy.timesheet.authentication.service.UserInfo;

/*
 * JwtProvider is an util class -> it implements useful functions:
 *
 * - generate a JWT token
 * - valiate a JWT token
 * - parse username from JWT token
 * 
 */
@Component
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${auth.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.jwtExpiration}")
    private int jwtExpiration;

    private static final String jwtType = "Bearer";
    private static final String jwtTypeStr = jwtType + " ";

    public static String getJwtType() {
        return jwtType;
    }

    public String getJwtTypeStr() {
        return jwtTypeStr;
    }

    public Integer getJwtExpiration() {
        return jwtExpiration;
    }

    private Key signingKey = null;

    private Key getSigningKey() {
        if (signingKey == null) {
            signingKey = new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
        }
        return signingKey;
    }

    public String generateJwtToken(Authentication authentication) {
        UserInfo userPrincipal = (UserInfo) authentication.getPrincipal();

        return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration * 1000)).signWith(getSigningKey())
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        logger.debug("Validating token {}", authToken);
        try {
            Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.info("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.info("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.info("JWT claims string is empty -> Message: {}", e);
        }

        return false;
    }

    public String getUserNameFromJwtToken(String authToken) {
        String username = Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(authToken).getBody().getSubject();
        logger.debug("Extracted username from token : {}", username);
        return username;
    }

    public Date getExpirationDateFromJwtToken(String authToken) {
        Date date = Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(authToken).getBody().getExpiration();
        logger.debug("Extracted expiration date from token : {}", date);
        return date;
    }

}
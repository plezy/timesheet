package lu.plezy.timesheet.authentication.jwt;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
public class JwtBlacklistManager {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    
    @Autowired
    private JwtProvider tokenProvider;

    @Data
    @RequiredArgsConstructor
    private class BlacklistInfo {
        @NonNull
        private Date expirationDate;
        private String sessionID = null;
    }

    private static HashMap<String, BlacklistInfo> blacklistMap = new HashMap<>();

    public void blacklist(String authToken, String sessionID) {

        String token = authToken;
        if (token.startsWith(tokenProvider.getJwtTypeStr())) {
            token = authToken.replace(tokenProvider.getJwtTypeStr(), "");
        }
        // if token is not valid, it is not necessary to blacklist the token
        logger.debug("Received request to blacklist token {} for sessionID {}", authToken, sessionID);
        if (tokenProvider.validateJwtToken(token)) {
            // if token already exists, do not add it
            if ( ! blacklistMap.containsKey(authToken)) {
                Date expiration = tokenProvider.getExpirationDateFromJwtToken(token);
                BlacklistInfo info = new BlacklistInfo(expiration);
                if (sessionID != null) {
                    info.setSessionID(sessionID);
                }
                blacklistMap.put(authToken, info);
                logger.debug("Token blacklisted !");
            }
        }
    }

    public boolean isBlacklisted(String authToken) {
        return blacklistMap.containsKey(authToken);
    }

    public void cleanBlacklistMap() {
        logger.debug("cleanBlacklistMap called");
        Date now = new Date();
        List<String> toRemove = null;
        for (String key : blacklistMap.keySet()) {
            BlacklistInfo info = blacklistMap.get(key);
            if (info.expirationDate.getTime() < now.getTime()) {
                if (toRemove == null) {
                    toRemove = new ArrayList<String>();
                }
                toRemove.add(key);
            }
        }

        if (toRemove != null) {
            for(String key : toRemove) {
                blacklistMap.remove(key);
                logger.debug("Key removed from blacklist : {}", key);
            }
        }
    }

    public void cleanBlacklistMap(String sessionID) {
        logger.debug("cleanBlacklistMap called for session {}", sessionID);
        Date now = new Date();
        List<String> toRemove = null;
        for (String key : blacklistMap.keySet()) {
            BlacklistInfo info = blacklistMap.get(key);
            if (info.getSessionID().equals(sessionID) || info.expirationDate.getTime() < now.getTime()) {
                if (toRemove == null) {
                    toRemove = new ArrayList<String>();
                }
                toRemove.add(key);
            }
        }

        if (toRemove != null) {
            for(String key : toRemove) {
                blacklistMap.remove(key);
                logger.debug("Key removed from blacklist : {}", key);
            }
        }
    }

    @Scheduled(fixedRate=60000)
    public void scheduleBlacklistCleaning() {
        cleanBlacklistMap();
    }

}
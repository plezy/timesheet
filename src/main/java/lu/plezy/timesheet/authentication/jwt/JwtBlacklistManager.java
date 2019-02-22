package lu.plezy.timesheet.authentication.jwt;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
public class JwtBlacklistManager {

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
        // if token is not valid, it is not necessary to blacklist the token
        if (tokenProvider.validateJwtToken(authToken)) {
            // if token already exists, do not add it
            if ( ! blacklistMap.containsKey(authToken)) {
                Date expiration = tokenProvider.getExpirationDateFromJwtToken(authToken);
                BlacklistInfo info = new BlacklistInfo(expiration);
                if (sessionID != null) {
                    info.setSessionID(sessionID);
                }
                blacklistMap.put(authToken, info);
            }
        }
    }

    public boolean isBlacklisted(String authToken) {
        return blacklistMap.containsKey(authToken);
    }

    public void cleanBlacklistMap() {
        Date now = new Date();
        for (String key : blacklistMap.keySet()) {
            BlacklistInfo info = blacklistMap.get(key);
            if (info.expirationDate.getTime() < now.getTime())
                blacklistMap.remove(key);
        }
    }

    public void cleanBlacklistMap(String sessionID) {
        Date now = new Date();
        for (String key : blacklistMap.keySet()) {
            BlacklistInfo info = blacklistMap.get(key);
            if (info.getSessionID().equals(sessionID) || info.expirationDate.getTime() < now.getTime())
                blacklistMap.remove(key);
        }
    }

}
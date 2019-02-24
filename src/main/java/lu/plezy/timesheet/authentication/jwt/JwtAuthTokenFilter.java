package lu.plezy.timesheet.authentication.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import lu.plezy.timesheet.authentication.service.UserDetailsServiceImpl;

/*
* This is a filter base class that is used to guarantee a single execution per request dispatch. It provides a doFilterInternal method with HttpServletRequest and HttpServletResponse arguments.
* 
* Inside JwtAuthTokenFilter class, the doFilterInternal method will:
* 
* - get JWT token from header
* - validate JWT
* - parse username from validated JWT
* - load data from users table, then build an authentication object
* - set the authentication object to Security Context
*
*/
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider tokenProvider;

    @Value("${auth.jwtHeader}")
    private String jwtHeader;
    
    @Autowired
    private JwtBlacklistManager blacklistManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            logger.debug("Running JwtAuthTokenFilter.doFilterInternal");
            String jwt = getJwt(request);
            if (jwt != null && tokenProvider.validateJwtToken(jwt)) {
                logger.debug("Got Valid JWT Token");

                if (blacklistManager.isBlacklisted(request.getHeader(jwtHeader))) {
                    throw new Exception("JWT token blacklisted");
                }

                String username = tokenProvider.getUserNameFromJwtToken(jwt);
                logger.debug("Found in JWT token username {}", username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.info("Can NOT set user authentication -> Message: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader(jwtHeader);

        if (authHeader != null && authHeader.startsWith(tokenProvider.getJwtTypeStr())) {
            return authHeader.replace(tokenProvider.getJwtTypeStr(), "");
        }

        return null;
    }
}
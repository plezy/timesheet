package lu.plezy.timesheet.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lu.plezy.timesheet.authentication.jwt.JwtBlacklistManager;
import lu.plezy.timesheet.authentication.jwt.JwtProvider;
import lu.plezy.timesheet.entities.messages.JwtResponse;
import lu.plezy.timesheet.entities.messages.LoginForm;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Value("${auth.jwtHeader}")
    private String jwtHeader;

    @Autowired
    private JwtBlacklistManager blacklistMgr;
    
    @PostMapping("/logon")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
 
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
 
        SecurityContextHolder.getContext().setAuthentication(authentication);
 
        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/renew")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> renewToken(Authentication authentication, @RequestHeader HttpHeaders headers) {
        String actualJwt = headers.getFirst(jwtHeader);
        String jwt = jwtProvider.generateJwtToken(authentication);
        blacklistMgr.blacklist(actualJwt, null);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
    
    @GetMapping("/logout")
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public void logout(Authentication authentication, @RequestHeader HttpHeaders headers) {
        String actualJwt = headers.getFirst(jwtHeader);
        blacklistMgr.blacklist(actualJwt, null);
    }
}
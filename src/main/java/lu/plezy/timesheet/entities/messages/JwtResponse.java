package lu.plezy.timesheet.entities.messages;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lu.plezy.timesheet.authentication.jwt.JwtProvider;

@Data
@RequiredArgsConstructor
public class JwtResponse {
    @NonNull
    private String token;
    private String type = JwtProvider.getJwtType();    
    @NonNull
    private String username;
    @NonNull
    private Collection<? extends GrantedAuthority> authorities;

}
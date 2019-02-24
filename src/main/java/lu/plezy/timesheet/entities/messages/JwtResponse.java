package lu.plezy.timesheet.entities.messages;

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
}
package pl.ekookna.magazynapp.admin.security.config;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.ekookna.magazynapp.admin.repository.UsersRepository;
import pl.ekookna.magazynapp.admin.repository.entity.Users;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AuthProvider implements AuthenticationProvider {

    private SecurityUserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private UsersRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        Optional<Users> user = userRepository.findUserByLogin(username);

        if (user.isPresent()) {
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                return user.map(users -> new UsernamePasswordAuthenticationToken(username, password, List.of(users::getRole))).orElse(null);
            }
        }
        throw new AuthenticationServiceException("Wrong login or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

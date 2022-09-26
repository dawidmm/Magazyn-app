package pl.ekookna.magazynapp.dto;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import pl.ekookna.magazynapp.admin.repository.entity.Users;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Validated
public class UserDto {

    @NotNull
    @NotBlank
    private String login;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    @Pattern(regexp = "USER|ADMIN")
    private String role;

    private String warehouse;

    public UserDetails toUserDetails() {
        Users user = new Users();
        user.setPassword(password);
        user.setActive(true);
        user.setRole(role);
        user.setLogin(login);

        return user;
    }
}

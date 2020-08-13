package pl.adambalski.springbootboilerplate.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;
import pl.adambalski.springbootboilerplate.logger.Role;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class User {
    private UUID uuid;
    private String login;
    private String fullName;
    private String email;
    private String password;
    private Role role;

    public static User valueOf(SignUpUserDto signUpUserDto) {
        PasswordEncoder passwordEncoder = new pl.adambalski.springbootboilerplate.security.PasswordEncoder()
                .passwordEncoderBean();

        return new User(
                UUID.randomUUID(),
                signUpUserDto.getLogin(),
                signUpUserDto.getFullName(),
                signUpUserDto.getEmail(),
                passwordEncoder.encode(signUpUserDto
                        .getPassword1()),
                Role.USER
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUuid().equals(user.getUuid()) &&
                getLogin().equals(user.getLogin()) &&
                getFullName().equals(user.getFullName()) &&
                getEmail().equals(user.getEmail()) &&
                getPassword().equals(user.getPassword()) &&
                getRole() == user.getRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getLogin(), getFullName(), getEmail(), getPassword(), getRole());
    }

    @Override
    public String toString() {
        //noinspection StringBufferReplaceableByString
        return new StringBuilder()
                .append("User{")
                    .append("uuid=")
                        .append(uuid)
                    .append(", login='")
                        .append(login)
                    .append("', fullName='")
                        .append(fullName)
                    .append("', email='")
                        .append(email)
                    .append("', password='")
                        .append(password)
                    .append("', role=")
                        .append(role)
                .append('}').toString();
    }
}

package pl.adambalski.springbootboilerplate.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;
import pl.adambalski.springbootboilerplate.security.GrantedAuthorityImpl;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Application's User class<br><br>
 *
 * @see SignUpUserDto
 * @see Role
 * @author Adam Balski
 */
public class User {
    private UUID uuid;
    private String login;
    private String fullName;
    private String email;
    private String password;
    private Role role;

    public User(UUID uuid, String login, String fullName, String email, String password, Role role) {
        this.uuid = uuid;
        this.login = login;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static User valueOf(SignUpUserDto signUpUserDto) {
        PasswordEncoder passwordEncoder =
                new pl.adambalski.springbootboilerplate.security
                        .PasswordEncoder().passwordEncoderBean();

        return new User(
                UUID.randomUUID(),
                signUpUserDto.login(),
                signUpUserDto.fullName(),
                signUpUserDto.email(),
                passwordEncoder.encode(signUpUserDto
                        .password1()),
                Role.USER
        );
    }

    // Getters, setters, equals, hashcode, toString
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;

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

    public UserDetails toUserDetails() {
        return new org.springframework.security.core.userdetails.User(
                this.login,
                this.password,
                this.getGrantedAuthorities()
        );
    }

    public List<GrantedAuthority> getGrantedAuthorities() {
        return List.of(
                new GrantedAuthorityImpl(this.role)
        );
    }
}

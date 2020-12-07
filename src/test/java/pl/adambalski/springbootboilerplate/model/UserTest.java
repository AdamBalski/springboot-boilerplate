package pl.adambalski.springbootboilerplate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;
import pl.adambalski.springbootboilerplate.security.GrantedAuthorityImpl;
import pl.adambalski.springbootboilerplate.security.PasswordEncoderFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


class UserTest {
    private List<User> users;

    @BeforeEach
    void init() {
        // user2 is equal to user3, but user1 is not
        users = List.of(
                new User(
                        UUID.fromString("08a706cf-8ab9-4dcb-bc66-2fe9b56fa1b0"),
                        "username1",
                        "User Name 1",
                        "user1@name.com",
                        "encrypted_password1",
                        Role.USER
                ),
                new User(
                        UUID.fromString("b93c6d2c-d8d2-43bd-a855-1746af3bd7be"),
                        "username2",
                        "User Name 2",
                        "user2@name.com",
                        "encrypted_password2",
                        Role.ADMIN
                ),
                new User(
                        UUID.fromString("b93c6d2c-d8d2-43bd-a855-1746af3bd7be"),
                        "username2",
                        "User Name 2",
                        "user2@name.com",
                        "encrypted_password2",
                        Role.ADMIN
                )
        );
    }

    @Test
    void testValueOfSingUpUserDto() {
        PasswordEncoder passwordEncoder = new PasswordEncoderFactory().passwordEncoderBean();

        SignUpUserDto userDto = new SignUpUserDto(
                "username",
                "User Name",
                "user@name.com",
                "password",
                "password");
        User user = User.valueOf(userDto, passwordEncoder);

        assertEquals(UUID.class, user.getUuid().getClass());
        assertEquals("username", user.getLogin());
        assertEquals("User Name", user.getFullName());
        assertEquals("user@name.com", user.getEmail());
        Assertions.assertTrue(
                passwordEncoder.matches(
                        userDto.password1(),
                        user.getPassword())
        );
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    void testEqualsWithTheSameObject() {
        assertEquals(users.get(0), users.get(0));
    }

    @Test
    void testEqualsWithEqualUsers() {
        assertEquals(users.get(1), users.get(2));
    }

    @Test
    void testEqualsWithNotEqualUsers() {
        Assertions.assertNotEquals(users.get(0), users.get(1));
    }

    @Test
    void testEqualsWithNotUser() {
        //noinspection AssertBetweenInconvertibleTypes
        Assertions.assertNotEquals(users.get(0), new ArrayList<>());
    }

    @Test
    void testEqualsWithNull() {
        Assertions.assertNotEquals(users.get(0), null);
    }

    @Test
    void testEqualsWhenUUIDsAreNotEqual() {
        UUID uuid = UUID.fromString("d0c66a08-33ef-42f6-ad52-aeeab28d4c8a");
        users.get(2).setUuid(uuid);

        Assertions.assertNotEquals(users.get(1), users.get(2));
    }

    @Test
    void testEqualsWhenLoginsAreNotEqual() {
        String login = "username3";
        users.get(2).setLogin(login);

        Assertions.assertNotEquals(users.get(1), users.get(2));
    }

    @Test
    void testEqualsWhenFullNamesAreNotEqual() {
        String fullName = "User Name 3";
        users.get(2).setFullName(fullName);

        Assertions.assertNotEquals(users.get(1), users.get(2));
    }

    @Test
    void testEqualsWhenEmailsAreNotEqual() {
        String email = "user3@name.com";
        users.get(2).setEmail(email);

        Assertions.assertNotEquals(users.get(1), users.get(2));
    }

    @Test
    void testEqualsWhenPasswordsAreNotEqual() {
        String password = "encrypted_password3";
        users.get(2).setPassword(password);

        Assertions.assertNotEquals(users.get(1), users.get(2));
    }

    @Test
    void testEqualsWhenRolesAreNotEqual() {
        users.get(2).setRole(Role.USER);

        Assertions.assertNotEquals(users.get(1), users.get(2));
    }


    @Test
    void testHashcodeIsEqualForEqualUsers() {
        assertEquals(users.get(1).hashCode(), users.get(2).hashCode());
    }

    @Test
    void testToString() {
        String expected = "User{uuid=08a706cf-8ab9-4dcb-bc66-2fe9b56fa1b0, login='username1', " +
                "fullName='User Name 1', email='user1@name.com', password='encrypted_password1', role=USER}";

        assertEquals(expected, users.get(0).toString());
    }

    @Test
    void testGetUserDetails() {
        UserDetails expected = new org.springframework.security.core.userdetails.User(
                "username1", "encrypted_password1", List.of());
        UserDetails actual = users.get(0).toUserDetails();

        assertEquals(expected, actual);
    }

    @Test
    void testGetGrantedAuthorities() {
        GrantedAuthorityImpl grantedAuthority = new GrantedAuthorityImpl(Role.USER);

        assertEquals(
                List.of(grantedAuthority),
                users.get(0).getGrantedAuthorities()
        );
    }
}
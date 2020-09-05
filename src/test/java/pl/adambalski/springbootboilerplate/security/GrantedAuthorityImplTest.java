package pl.adambalski.springbootboilerplate.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.adambalski.springbootboilerplate.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GrantedAuthorityImplTest {
    GrantedAuthorityImpl grantedAuthorityRoleAdmin;
    GrantedAuthorityImpl grantedAuthorityRoleUser;

    @BeforeEach
    void init() {
        grantedAuthorityRoleAdmin = new GrantedAuthorityImpl(Role.ADMIN);
        grantedAuthorityRoleUser = new GrantedAuthorityImpl(Role.USER);
    }

    @Test
    void testGetAuthority() {
        assertEquals("ROLE_USER", grantedAuthorityRoleUser.getAuthority());
    }

    @Test
    void testEqualsWithTheSameGrantedAuthorities() {
        assertEquals(grantedAuthorityRoleAdmin, grantedAuthorityRoleAdmin);
    }

    @Test
    void testEqualsWithEqualGrantedAuthorityImples() {
        assertEquals(grantedAuthorityRoleAdmin, new GrantedAuthorityImpl(Role.ADMIN));
    }

    @Test
    void testEqualsWithNotEqualGrantedAuthorityImples() {
        assertNotEquals(grantedAuthorityRoleUser, grantedAuthorityRoleAdmin);
    }

    @Test
    void testEqualsWithNotGrantedAuthorityImpl() {
        //noinspection AssertBetweenInconvertibleTypes
        assertNotEquals(grantedAuthorityRoleAdmin, List.of());
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(grantedAuthorityRoleAdmin, null);
    }

    @Test
    void testHashcodeIsEqualForEqualGrantedAuthorityImples() {
        assertEquals(grantedAuthorityRoleAdmin.hashCode(), grantedAuthorityRoleAdmin.hashCode());
    }
}
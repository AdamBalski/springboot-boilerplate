package pl.adambalski.springbootboilerplate.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleTest {
    @Test
    void testGrantedAuthorityStringIfRoleIsUser() {
        Role role = Role.USER;
        assertEquals("ROLE_USER", role.grantedAuthorityString);
    }

    @Test
    void testGrantedAuthorityStringIfRoleIsAdmin() {
        Role role = Role.ADMIN;
        assertEquals("ROLE_ADMIN", role.grantedAuthorityString);
    }
}
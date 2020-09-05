package pl.adambalski.springbootboilerplate.security;

import pl.adambalski.springbootboilerplate.model.Role;

import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityImpl implements GrantedAuthority {
    private final Role role;

    public GrantedAuthorityImpl(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.role.grantedAuthorityString;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof GrantedAuthorityImpl that)) return false;

        return role == that.role;
    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }
}
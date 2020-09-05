package pl.adambalski.springbootboilerplate.model;

/**
 * Role enum for {@link User}<br><br>
 *
 * @see User
 * @author Adam Balski
 */
public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    public String grantedAuthorityString;

    Role(String grantedAuthorityString) {
        this.grantedAuthorityString = grantedAuthorityString;
    }
}

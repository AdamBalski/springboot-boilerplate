package pl.adambalski.springbootboilerplate.model;

/**
 * Enum that indicates if a {@link User} has admin privileges.<br><br>
 *
 * @see User
 * @author Adam Balski
 */
public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    public final String grantedAuthorityString;

    Role(String grantedAuthorityString) {
        this.grantedAuthorityString = grantedAuthorityString;
    }
}

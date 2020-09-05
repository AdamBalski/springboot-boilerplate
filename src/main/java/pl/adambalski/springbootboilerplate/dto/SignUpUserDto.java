package pl.adambalski.springbootboilerplate.dto;


/**
 * Used to exchange data with an api consumer while signing up.<br><br>
 *
 * @see pl.adambalski.springbootboilerplate.model.User
 * @author Adam Balski
 */

public record SignUpUserDto(String login, String fullName, String email, String password1, String password2) {}
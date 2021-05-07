package pl.adambalski.springbootboilerplate.dto;

/**
 * Used to exchange data with an api consumer while logging in.<br><br>
 *
 * @see pl.adambalski.springbootboilerplate.controller.user.AuthenticationController
 * @author Adam Balski
 */

public record LoginDto(String username, String password) {}
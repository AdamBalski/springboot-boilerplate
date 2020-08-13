package pl.adambalski.springbootboilerplate.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class SignUpUserDto {
    private String login;
    private String fullName;
    private String email;
    private String password1;
    private String password2;
}
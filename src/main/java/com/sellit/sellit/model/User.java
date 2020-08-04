package com.sellit.sellit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class User {
    UUID uuid;
    String login;
    String fullName;
    String email;
    String password;
    Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
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
}

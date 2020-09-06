package pl.adambalski.springbootboilerplate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.adambalski.springbootboilerplate.repository.UserRepository;

/**
 * Simple implementation of {@link UserDetailsService} based on {@link pl.adambalski.springbootboilerplate.model.User}s
 * <br><br>
 *
 * @see pl.adambalski.springbootboilerplate.model.User
 * @see org.springframework.security.core.userdetails.User
 * @see UserDetailsService
 * @see UserDetails
 * @author Adam Balski
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.getByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"))
                .toUserDetails();
    }
}

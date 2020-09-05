package pl.adambalski.springbootboilerplate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.Filter;

/**
 * Security configuration for the boilerplate
 *
 * @see WebSecurityConfigurerAdapter
 * @see Filter
 * @author Adam Balski
 */
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private UserDetailsService userDetailsService;

    @Autowired
    private void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        // todo
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }
}

package com.agharibi.security;

import com.agharibi.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private Environment environment;
    private UsersService usersService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity() {}

    @Autowired
    public WebSecurity(Environment environment,
                       UsersService usersService,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.environment = environment;
        this.usersService = usersService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.authorizeRequests().antMatchers("/**").hasIpAddress(environment.getProperty("gateway.ip"))
            .and()
            .addFilter(getAuthenticationFilter());
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter filter =
            new AuthenticationFilter(usersService, environment, authenticationManager());
        filter.setFilterProcessesUrl(environment.getProperty("login.url.path"));

        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);
    }
}

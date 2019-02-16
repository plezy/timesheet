package lu.plezy.timesheet.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
         * http .authorizeRequests() .antMatchers("/h2/*").permitAll()
         * .anyRequest().authenticated();
         */

        http.authorizeRequests().antMatchers("/h2/**").permitAll().anyRequest().authenticated();

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

}
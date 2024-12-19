package net.geeknotes.GeekNotes;

import net.geeknotes.GeekNotes.repos.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserRepo userRepo;

    public WebSecurityConfig(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/posts").permitAll()
                        .requestMatchers("/login", "/sign-up").permitAll()
                        .requestMatchers("/posts/{postUrl}").permitAll()
                        .requestMatchers("/users/{userLogin}/update").authenticated()
                        .requestMatchers("/posts/{postUrl}/comment").authenticated()
                        .requestMatchers("/users/{userLogin}").authenticated()
                        .requestMatchers("/posts/pre-posts/create-pre-post").hasRole("ADMIN")
                        .requestMatchers("/posts/pre-posts").hasRole("ADMIN")
                        .requestMatchers("/posts/pre-posts/{prePostUrl}").hasRole("ADMIN")
                        .requestMatchers("/posts/pre-posts/{prePostUrl}/publish").hasRole("ADMIN")
                        .requestMatchers("/posts/pre-posts/{prePostUrl}/delete").hasRole("ADMIN")
                        .requestMatchers("/posts/{postUrl}/delete").hasRole("ADMIN")
                        .requestMatchers("/posts/{postUrl}/update").hasRole("ADMIN")
                        .requestMatchers("/posts/pre-posts/{prePostUrl}/update").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
//                .exceptionHandling((exception) -> exception
//                        .accessDeniedPage("/access-denied")
//                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userLogin -> {
            net.geeknotes.GeekNotes.models.User user = userRepo.findByUserLogin(userLogin);
            if (user != null) {
                return User.withUsername(user.getUserLogin())
                        .password(user.getUserPassword())
                        .roles(user.getUserRole())
                        .build();
            }
            throw new UsernameNotFoundException("User not found");
        };
    }
}

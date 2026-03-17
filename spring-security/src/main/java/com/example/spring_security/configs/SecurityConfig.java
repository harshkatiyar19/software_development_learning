package com.example.spring_security.configs;

import com.example.spring_security.service.SecurityUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final SecurityUserService userDetailsService;

    public SecurityConfig(SecurityUserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    //Form based login -> generates forms
    //@Bean
    public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception{
        return http
        // 1️⃣ Authorization Rules
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/login","/api/auth/register").permitAll()//permits all request hitting these endpoints
                                .anyRequest().authenticated()) // rejects direct requests to any other end points and requires login

        // 2️⃣ Form Login Configuration
                .formLogin(form->
                        form.loginPage("/api/auth/login")  // Custom login page if not given spring generates ui on its own
                                .loginProcessingUrl("/api/auth/login") // POST URL to submit credentials
                                //.defaultSuccessUrl("/home",true) // Where to go after login | true → always redirect to /home.
                                .failureUrl("/api/auth/login?error=true")  // If authentication fails
                                .permitAll())/*The login page and login processing URL must be accessible to unauthenticated users.

        If you don’t permit them → Spring will block the login page itself.*/

        // 3️⃣ Logout Configuration
                /*
                🔐 What is Logout in Spring Security?

                Since Form Login is session-based, when a user logs in:
                    •	Spring creates an HTTP session
                    •	Stores authentication inside SecurityContext
                    •	Sends a cookie: JSESSIONID

                Logging out means:

                Destroy authentication + destroy session + remove session cookie.*/
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")//POST endpoint to logout| The endpoint that triggers logout.
                        .logoutSuccessUrl("/login?logout")// redirected destination
                        .invalidateHttpSession(true)// IMPORTANT as it tells spring to destroy http session.  It removes  	•	Authentication •	Stored attributes •	Session ID
                        .deleteCookies("JSESSIONID")
                        /*
                        When user logs in: Browser stores: Cookie: JSESSIONID=ABC123
                        Even if session is invalidated on server,browser still has cookie.
                        So we explicitly remove it. */
                )
        // 4️⃣ CSRF (enabled by default)
                // 5️⃣ Session Management
                .sessionManagement(session -> session
                        .maximumSessions(1) // Only 1 session per user
                )
                .build();
    }


`    // 6️⃣ AuthenticationManager Bean
    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder
    ) throws Exception {/*
    	•	@Bean → Registers this in Spring container.
        •	We inject:
        •	HttpSecurity → to access shared security objects.
        •	PasswordEncoder → to hash & verify passwords.
    */

        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        /*
        * AuthenticationManagerBuilder is a builder class used to configure:
        •	UserDetailsService
        •	PasswordEncoder
        •	AuthenticationProvider

        * */

        builder
                .userDetailsService(userDetailsService)

                .passwordEncoder(passwordEncoder);

        /*
        *
        * This means:

        When authenticating:
            •	Load user from DB using userDetailsService
            •	Compare password using passwordEncoder
            * */

        return builder.build();
    }`


//rest based filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login","/api/auth/register").permitAll()
                        .anyRequest().authenticated()
                )

                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )

                .sessionManagement(session ->
                        session.maximumSessions(1)
                )
                .build();
    }

    // 7️⃣ Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

package com.example.myecommerce.config;

import com.example.myecommerce.services.MyUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration//Hace que spring tome esto como configuration
@EnableWebSecurity//Activa la seguridad web
@EnableMethodSecurity//Habilita anotaciones @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final MyUserDetailService myUserDetailService;//Injeccion de MyUserDetail service
    private final MyAuthSuccesHandler myAuthSuccesHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csfr->csfr.disable())
                /*
                 * Manejo de sesion en http para mantener una sesion activa
                 */
                .sessionManagement(session-> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .expiredUrl("/login?logout")
                )
                /*
                 * Establecemos procesos que necesitan authenticated y que no necesitan
                 */
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/css/**","/images/**","/public/**").permitAll()
                                .requestMatchers("/login","/register").permitAll()
                                .anyRequest().authenticated()
                        )
                /*
                 * En caso de no estar logeado te redirige a "/login"
                 */
                .formLogin(login-> login
                        .loginPage("/login")
                        /*.usernameParameter(<name>)
                        * debe llevar explicitamente el mismo nombre
                        * que lleva directamente mi form en /login
                        * destinado a ser el campo de busqueda para
                        * mi user
                        * */
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(myAuthSuccesHandler)
                        .permitAll()
                )
                /*
                 * Manejo de cierre de session http
                 */
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .userDetailsService(myUserDetailService);//carga de my UserDetailService
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

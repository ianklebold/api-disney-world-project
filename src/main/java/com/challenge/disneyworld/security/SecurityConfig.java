package com.challenge.disneyworld.security;

import com.challenge.disneyworld.security.filter.CustomAuthenticationFilter;
import com.challenge.disneyworld.security.filter.CustomAuthorizationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.DELETE;


import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity //Habilita la seguridad en springboot
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final UserDetailsService userDetailsService; //Esto labura con el service de user
    private final BCryptPasswordEncoder bcryptPasswordEncoder;

    //Manager de autenticacion. Todos los intentos de autenticacion pasan por él
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bcryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/auth/login"); //EndPoint donde se da la autenticacion
        //STATELESS (Principio de REST) quiere decir que ante una peticion a la proxima no recuerdo que lo hayas hecho. 
        httpSecurity.csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                        .antMatchers("/auth/register").permitAll()
                        .antMatchers("/auth/login").permitAll()
                        .antMatchers("/auth/token/refresh").permitAll()
                        .antMatchers(GET,"/users").hasAnyAuthority("ROLE_ADMIN")
                        .antMatchers(DELETE,"/users").hasAnyAuthority("ROLE_ADMIN")
                        .antMatchers(DELETE,"/users").hasAnyAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated() //No hace falta ROL. Con Token valido puede hacer lo quiera
                        .and()
                        .addFilter(customAuthenticationFilter)  //Filtro de autenticacion
                        .addFilterBefore(new CustomAuthorizationFilter(), 
                                        UsernamePasswordAuthenticationFilter.class);
                        //Filtro de autorizacion. Aqui validamos que el token sea correcto
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

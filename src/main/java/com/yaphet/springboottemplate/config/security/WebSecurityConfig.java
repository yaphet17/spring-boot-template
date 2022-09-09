package com.yaphet.springboottemplate.config.security;

import javax.sql.DataSource;

import com.yaphet.springboottemplate.services.AppUserService;
import com.yaphet.springboottemplate.services.CustomOAuth2UserService;
import com.yaphet.springboottemplate.security.LocalLoginSuccessHandler;
import com.yaphet.springboottemplate.security.OAuth2LoginSuccessHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final AppUserService appUserService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final LocalLoginSuccessHandler localSuccessHandler;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final DataSource dataSource;
    private final int tokenValiditySeconds;

    public WebSecurityConfig(AppUserService appUserService,
                             CustomOAuth2UserService customOAuth2UserService,
                             BCryptPasswordEncoder bCryptPasswordEncoder,
                             LocalLoginSuccessHandler localSuccessHandler,
                             OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,
                             DataSource dataSource,
                             @Value("${app.spring-boot-template.remember-me.token-validity-seconds}") int tokenValiditySeconds) {
        this.appUserService = appUserService;
        this.customOAuth2UserService = customOAuth2UserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.localSuccessHandler = localSuccessHandler;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
        this.dataSource = dataSource;
        this.tokenValiditySeconds = tokenValiditySeconds;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .antMatchers("/", "/register/**", "/confirm/**", "/login/**").permitAll()
                .anyRequest()
                .authenticated().and()
                .formLogin()
                    .loginPage("/login")
                    .successHandler(localSuccessHandler)
                .and()
                .rememberMe().userDetailsService(appUserService).tokenRepository(persistentRememberMeToken()).tokenValiditySeconds(tokenValiditySeconds)
                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService)
                    .and()
                    .successHandler(oAuth2LoginSuccessHandler)
                    .failureHandler((request, response, exception) -> {
                        request.getSession().setAttribute("error.message", exception.getMessage());})
                .and()
                .logout()
                    .logoutSuccessUrl("/login")
                    .deleteCookies("JSESSIONID");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }

    @Bean
    public PersistentTokenRepository persistentRememberMeToken(){
        JdbcTokenRepositoryImpl rememberMeToken = new JdbcTokenRepositoryImpl();
        rememberMeToken.setDataSource(dataSource);
        return rememberMeToken;
    }
}

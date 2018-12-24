package kr.ac.gachon.gtg.security;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.Filter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableOAuth2Client
@Log
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    OAuth2ClientContext oAuth2ClientContext;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("security config..........");

        http.authorizeRequests()
                .antMatchers("/course/**").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/manage/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                // prevent a problem losing csrf token
                // need to get session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                // exception
                .exceptionHandling().accessDeniedPage("/accessDenied")
                // login
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/auth/login"))
                .and()
                .formLogin()
                .loginPage("/auth/login")
                .and()
                // logout
                .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .and()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(gtgPasswordEncoder());
    }

    @Bean
    public PasswordEncoder gtgPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter naverFilter =
                new OAuth2ClientAuthenticationProcessingFilter("/auth/oauth/naver");

        OAuth2RestTemplate naverTemplate = new OAuth2RestTemplate(naver(), oAuth2ClientContext);

        naverFilter.setRestTemplate(naverTemplate);

        UserInfoTokenServices tokenServices =
                new UserInfoTokenServices(naverResource().getUserInfoUri(), naver().getClientId());
        tokenServices.setRestTemplate(naverTemplate);
        tokenServices.setPrincipalExtractor(new NaverPrincipalExtractor());

        naverFilter.setTokenServices(tokenServices);
        return naverFilter;
    }

    @Bean
    public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    @Bean
    @ConfigurationProperties("naver.client")
    public AuthorizationCodeResourceDetails naver() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("naver.resource")
    public ResourceServerProperties naverResource() {
        return new ResourceServerProperties();
    }
}

package com.sao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**","js/**","fonts/**","image/**","index").permitAll()
                .antMatchers("/users/**").hasRole("ADMIN")//需要相应的角色才能访问
                .and()
                .formLogin() //基于form表单登录验证
                .loginPage("/login").failureUrl("/login-error");//自定义登录页面
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)throws Exception{
        //inMemoryAuthentication 从内存中获取
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("123@qq.com").password(new BCryptPasswordEncoder().encode("123")).roles("ADMIN");
    }
}

package com.project.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/**
 * @author Lingjun
 * @date 2023/4/25 22:04
 */

@Configuration
@EnableWebSecurity //@EnableWebSecurity is the default for turning on SpringSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


}

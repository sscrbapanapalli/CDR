package com.cmacgm.cdrserver;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableJpaAuditing
@Configuration
@EnableJpaRepositories
@EnableAspectJAutoProxy
@EnableAutoConfiguration
@ComponentScan(	    basePackages = {"com.cmacgm.*"}, includeFilters = {@ComponentScan.Filter(Aspect.class)})
public class CdrApplication  extends SpringBootServletInitializer  {

	
	   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	        return application.sources(CdrApplication.class);
	    }
	    public static void main(String[] args) throws Exception {
	        SpringApplication.run(CdrApplication.class, args);
	    }

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				applyFullCorsAllowedPolicy(registry);
			}
		};
	}

	public static void applyFullCorsAllowedPolicy(CorsRegistry registry) {
		  registry.addMapping("/**").allowCredentials(false).allowedOrigins("*").
		  allowedMethods("PUT", "POST", "GET", "OPTIONS", "DELETE").exposedHeaders("userToken");

	}
}

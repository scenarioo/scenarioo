package org.scenarioo;

import com.thetransactioncompany.cors.CORSFilter;
import org.scenarioo.rest.application.ScenariooInitializer;
import org.scenarioo.rest.base.logging.RequestLoggingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@SpringBootApplication
public class ScenariooViewerApplication extends SpringBootServletInitializer implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(ScenariooViewerApplication.class, args);
	}

	@Bean
	public ServletContextInitializer createInitializer() {
		return new ScenariooInitializer();
	}

	@Bean
	public FilterRegistrationBean<CORSFilter> corsFilter() {
		FilterRegistrationBean<CORSFilter> corsFilterBean = new FilterRegistrationBean<>(new CORSFilter());
		corsFilterBean.setUrlPatterns(Collections.singletonList("/rest/*"));
		return corsFilterBean;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RequestLoggingFilter());
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		// turn off all suffix pattern matching
		configurer.setUseSuffixPatternMatch(false);
	}
}

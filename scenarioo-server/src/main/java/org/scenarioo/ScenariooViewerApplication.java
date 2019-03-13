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
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Configuration
@SpringBootApplication
public class ScenariooViewerApplication extends SpringBootServletInitializer implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(ScenariooViewerApplication.class, args);
	}

	/**
	 * To prevent a race condition between Spring Boot and ElasticSearch both trying to configure Netty
	 * we have to disable this through a System Property.
	 * See: https://discuss.elastic.co/t/contradictory-and-sometimes-poor-advice-given-for-es-set-netty-runtime-available-processors/148014
	 */
	@PostConstruct
	private void setSystemPropertyForElasticSearch() {
		System.setProperty("es.set.netty.runtime.available.processors", Boolean.FALSE.toString());
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

	/**
	 * Needed to enable multipart upload for large ZIP file uploads.
	 */
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(-1);
		multipartResolver.setMaxUploadSizePerFile(-1);
		return multipartResolver;
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

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(false);
	}

	@Bean
	public HttpFirewall allowUrlEncodedPercentHttpFirewall() {
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowUrlEncodedPercent(true);
		return firewall;
	}
}

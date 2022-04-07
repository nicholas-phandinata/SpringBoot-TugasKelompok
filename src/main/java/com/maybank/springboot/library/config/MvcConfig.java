package com.maybank.springboot.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String brandUploadPath = "/Users/nicholasphandinata/Documents/GitHub/SpringBoot-TugasKelompok/src/main/resources/static/images";
		registry.addResourceHandler("/images/**").addResourceLocations("file://"+brandUploadPath + "/");
	}
	
}

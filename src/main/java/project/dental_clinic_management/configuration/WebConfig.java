package project.dental_clinic_management.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ánh xạ đường dẫn URL "/uploads/**" đến thư mục thực tế trên ổ đĩa "/uploads/"
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
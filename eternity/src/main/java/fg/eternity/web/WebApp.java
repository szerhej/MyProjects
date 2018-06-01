package fg.eternity.web;

import fg.eternity.bo.Config;
import fg.eternity.config.BasicConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.TimeZone;

/**
 * Starts Application
 */
@SpringBootApplication
@Import({ BasicConfig.class })
public class WebApp implements WebMvcConfigurer {

    @Bean(initMethod = "init")
    public Config config(){
        Config config = new Config();
        config.setDimX(16);
        config.setDimY(16);
        config.setPatternCount(22);
        return config;
    }

    public static void main(String[] args) {
        //Set application timezone to UTC fixed avoiding errors coming from timezone differences
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(WebApp.class, args);
    }

    /**
     * Add handler for static resources (html/css/js/etc)
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/app/**").addResourceLocations("classpath:/web/");
    }


}

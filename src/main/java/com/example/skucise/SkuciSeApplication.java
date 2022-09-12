package com.example.skucise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static com.example.skucise.security.SecurityConfiguration.JWT_CUSTOM_HTTP_HEADER;
import java.util.Arrays;

@SpringBootApplication
public class SkuciSeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkuciSeApplication.class, args);
    }

    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        //ovo je bitno, da su dozvoljeni korisnicki kredencijali
        corsConfiguration.setAllowCredentials(true);

        //definisemo sa kog izvora moze da se pristupi nasem servisu
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200")  );

        //definisemo koje headere za zahteve prihvatamo
        corsConfiguration.setAllowedHeaders(
                Arrays.asList( "Origin", "Access-Control-Allow-Origin", "Content-Type",
                        "Accept", "Authorization", "Origin, Accept", "X-Requested-With",
                        "Access-Control-Request-Method", "Access-Control-Request-Headers", JWT_CUSTOM_HTTP_HEADER )
        );

        //lista response headera koji su prihvatljivi
        corsConfiguration.setExposedHeaders(
                Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", JWT_CUSTOM_HTTP_HEADER )
        );

        //http metode koje su dozvoljene
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","PUT","POST","OPTIONS","DELETE"));

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

}

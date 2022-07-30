package com.atguigu.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/*
* 解决跨域的配置类，加了之后就不用每个controller上加@CrossOrigin注解了
*
*
*
*解决跨域问题
*注意:如果使用gateway网关的跨域解决方案，那就不需要在每个模块的Controller类上面添加@crossorigin跨域注解了，
* 如果加上就会出错，毕竟两次跨域相等于没有跨域，然后跨域配置文件是固定配置，如果需要使用的时候直接复制就可以了
* */

@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*"); // 1 允许任何域名使用
        config.addAllowedOrigin("*"); // 2 允许任何头
        config.addAllowedHeader("*"); // 3 允许任何方法（post、get等）

        //2.添加映射路径
        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        //3.返回新的CorsFilter.
        return new CorsWebFilter(source);
    }
}

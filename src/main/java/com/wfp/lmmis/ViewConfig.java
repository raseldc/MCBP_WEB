/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis;

import com.wfp.lmmis.interceptor.LoginInterceptor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.CacheControl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 *
 * @author shamiul Islam-AnunadSolution
 */
@Configuration
public class ViewConfig implements WebMvcConfigurer {

    @Bean
    public UrlBasedViewResolver urlBaseviewResolver() {
        UrlBasedViewResolver tilesViewResolver = new UrlBasedViewResolver();
        tilesViewResolver.setViewClass(TilesView.class);
        return tilesViewResolver;
    }

    /**
     *
     * @return
     */
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    /**
     *
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/resources/messages");
        //messageSource.setBasename("cl /i18n/usermsg");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
//        SessionLocaleResolver resolver = new SessionLocaleResolver();
        CookieLocaleResolver resolver = new CookieLocaleResolver();

        //  resolver.setDefaultLocale(new Locale("en", ""));
        resolver.setCookieName("myLocaleCookie");
        resolver.setCookieMaxAge(4800);
        resolver.setDefaultLocale(new Locale("bn"));
        return resolver;
    }

    @Bean
    public TilesViewResolver getTilesViewResolver() {
        TilesViewResolver tilesViewResolver = new TilesViewResolver();
        tilesViewResolver.setViewClass(TilesView.class);
        return tilesViewResolver;
    }

    /**
     *
     * @return
     */
    @Bean
    public TilesConfigurer getTilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();

        tilesConfigurer.setCheckRefresh(true);
        tilesConfigurer.setDefinitions("/WEB-INF/layouts/layouts.xml", "/WEB-INF/layouts/views.xml");
//        tilesConfigurer.setDefinitionsFactoryClass(TilesDefinitionsConfig.class);

        // Add apache tiles definitions
        TilesDefinitionsConfig.addDefinitions();

        return tilesConfigurer;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(new LoginInterceptor());

        // registry.addInterceptor(new TransactionInterceptor()).addPathPatterns("/person/save/*");
    }
    private static final int BROWSER_CACHE_CONTROL = 604800;
//
//    @Bean
//    public ResourceUrlEncodingFilter filter() {
//        return new ResourceUrlEncodingFilter();
//    }

    @Bean
    public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
        return new ResourceUrlEncodingFilter();
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
                //                .setCachePeriod(BROWSER_CACHE_CONTROL)
                .resourceChain(true);
        //addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
//        registry.addResourceHandler("/custom/**").addResourceLocations("/WEB-INF/pages/js/").setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS)).setCachePeriod(BROWSER_CACHE_CONTROL);

//        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        registry.addResourceHandler("/resources/CustomJS/**")
//                .addResourceLocations("/resources/CustomJS/")
//                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
//                .resourceChain(false)
//                .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
    }
}

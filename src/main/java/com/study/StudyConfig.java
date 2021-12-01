package com.study;

import com.study.bean.ExampleBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudyConfig {

    @Bean
    public ExampleBean exampleBeanConfig() {
        return new ExampleBean();
    }

}

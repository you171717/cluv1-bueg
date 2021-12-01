package com.study.bean;

import com.study.StudyConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ExampleBeanTest {

    @Autowired
    ExampleBean exampleBean;

    @Test
    @DisplayName("XML을 이용한 Bean 생성 및 사용 테스트")
    public void createBeanByXMLTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");

        ExampleBean bean = (ExampleBean) context.getBean("exampleBean");

        System.out.println(bean.getBeanValue());
    }

    @Test
    @DisplayName("@Configuration, @Bean을 이용한 Bean 생성 및 사용 테스트")
    public void createBeanByConfigTest() {
        ApplicationContext context = new AnnotationConfigApplicationContext(StudyConfig.class);

        ExampleBean bean = (ExampleBean) context.getBean("exampleBeanConfig");

        System.out.println(bean.getBeanValue());

    }

    @Test
    @DisplayName("@Component을 이용한 Bean 생성 및 사용 테스트")
    public void createBeanByComponentTest() {
        System.out.println(exampleBean.getBeanValue());
    }

}
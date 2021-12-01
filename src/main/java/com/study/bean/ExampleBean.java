package com.study.bean;

import org.springframework.stereotype.Component;

@Component
public class ExampleBean {

    private boolean beanValue = true;

    public boolean getBeanValue() {
        return beanValue;
    }

}

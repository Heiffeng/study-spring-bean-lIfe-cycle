package wang.ruangong.study.spring;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.DisposableBean;

public class MyBean implements BeanNameAware, BeanFactoryAware,InitializingBean, ApplicationContextAware, DisposableBean {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setBeanName(String beanName) {
        System.out.println("BeanNameAware: setBeanName called with name " + beanName);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("BeanFactoryAware: setBeanFactory called");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("ApplicationContextAware: setApplicationContext called");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("PostConstruct: afterPropertiesSet called");
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean: afterPropertiesSet called");
    }
    public void customInit() {
        System.out.println("Custom init-method: customInit called");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean: destroy called");
    }

    public void customDestroy() {
        System.out.println("Custom destroy-method: customDestroy called");
    }


}

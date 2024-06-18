# Spring Bean 生命周期详解

在 Spring 框架中，Bean 的生命周期由 Spring 容器全权管理。了解和掌握 Bean 的生命周期对于使用 Spring 开发稳定且高效的应用程序至关重要。本文将详细介绍 Spring Bean 生命周期的四个主要阶段：实例化阶段、初始化阶段、使用阶段和销毁阶段，并涵盖各个阶段的关键步骤和扩展点。

## 1. 实例化阶段（Instantiation）

实例化阶段包括以下关键步骤：

- **BeanNameAware 接口**：如果 Bean 实现了 `BeanNameAware` 接口，Spring 将调用其 `setBeanName` 方法，将 Bean 的名称传递给它。
- **BeanFactoryAware 接口**：如果 Bean 实现了 `BeanFactoryAware` 接口，Spring 将调用其 `setBeanFactory` 方法，将 `BeanFactory` 实例传递给它。

这些步骤确保 Bean 具有必要的上下文信息，可以与 Spring 容器进行交互。

```java
@Override
public void setBeanName(String beanName) {
    System.out.println("BeanNameAware: setBeanName called with name " + beanName);
}

@Override
public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    System.out.println("BeanFactoryAware: setBeanFactory called");
}
```

## 2. 初始化阶段（Initialization）

初始化阶段是 Bean 生命周期中最复杂的阶段，它包括属性设置、依赖注入和初始化方法的调用。该阶段包括以下关键步骤：

- **属性设置和依赖注入**：Spring 容器根据配置文件或注解，将配置的属性值或依赖对象注入到 Bean 实例中。
- **BeanPostProcessor 的 `postProcessBeforeInitialization` 方法**：Spring 调用所有注册的 `BeanPostProcessor` 实现的 `postProcessBeforeInitialization` 方法，以便在 Bean 初始化前对其进行处理。
- **InitializingBean 接口和自定义初始化方法**：如果 Bean 实现了 `InitializingBean` 接口，Spring 将调用其 `afterPropertiesSet` 方法。此外，如果配置了自定义的初始化方法，Spring 也会调用该方法。
- **BeanPostProcessor 的 `postProcessAfterInitialization` 方法**：Spring 调用所有注册的 `BeanPostProcessor` 实现的 `postProcessAfterInitialization` 方法，以便在 Bean 初始化后对其进行处理。

```java
@Override
public void afterPropertiesSet() throws Exception {
    System.out.println("InitializingBean: afterPropertiesSet called");
}

public void customInit() {
    System.out.println("Custom init-method: customInit called");
}
```

## 3. 使用阶段（Ready to Use）

在完成初始化后，Bean 进入使用阶段。在这个阶段，Bean 处于完全初始化状态，可以被应用程序使用。这个阶段没有特定的扩展点，但这是应用程序逻辑使用和操作 Bean 的主要时间段。

```java
public static void main(String[] args) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    MyBean myBean = context.getBean(MyBean.class);
    System.out.println("Using MyBean: " + myBean);
    context.close();
}
```

## 4. 销毁阶段（Destruction）

当 Spring 容器关闭时，Bean 进入销毁阶段。这个阶段包括以下关键步骤：

- **DisposableBean 接口和自定义销毁方法**：如果 Bean 实现了 `DisposableBean` 接口，Spring 将调用其 `destroy` 方法。此外，如果配置了自定义的销毁方法，Spring 也会调用该方法。

这些步骤确保在销毁 Bean 之前执行必要的清理操作，释放资源。

```java
@Override
public void destroy() throws Exception {
    System.out.println("DisposableBean: destroy called");
}

public void customDestroy() {
    System.out.println("Custom destroy-method: customDestroy called");
}
```

# 完整示例

为了更好地理解这些阶段，我们提供一个完整的示例，包括 Bean 类、`BeanPostProcessor` 和 Spring 配置。

## Bean 类

```java
package com.example.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.DisposableBean;

public class MyBean implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean {

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
```

## `BeanPostProcessor`

```java
package com.example.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class CustomBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanPostProcessor: postProcessBeforeInitialization called for " + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanPostProcessor: postProcessAfterInitialization called for " + beanName);
        return bean;
    }
}
```

## Spring 配置

```java
package com.example.lifecycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean(initMethod = "customInit", destroyMethod = "customDestroy")
    public MyBean myBean() {
        MyBean myBean = new MyBean();
        myBean.setName("Test Bean");
        return myBean;
    }

    @Bean
    public CustomBeanPostProcessor customBeanPostProcessor() {
        return new CustomBeanPostProcessor();
    }
}
```

# 测试 Spring 配置

```java
package com.example.lifecycle;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringLifecycleDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        
        MyBean myBean = context.getBean(MyBean.class);
        System.out.println("Using MyBean: " + myBean);

        context.close();
    }
}
```

# 参考链接

1. https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans
2. https://www.tutorialspoint.com/spring/spring_bean_life_cycle.htm
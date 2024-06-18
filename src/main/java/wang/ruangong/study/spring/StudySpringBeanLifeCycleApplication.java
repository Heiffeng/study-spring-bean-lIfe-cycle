package wang.ruangong.study.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class StudySpringBeanLifeCycleApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudySpringBeanLifeCycleApplication.class, args);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);

        MyBean myBean = context.getBean(MyBean.class);
        System.out.println("Using MyBean: " + myBean);

        context.close();
    }

}

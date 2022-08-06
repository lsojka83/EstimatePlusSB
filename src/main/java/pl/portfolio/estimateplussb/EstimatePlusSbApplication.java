package pl.portfolio.estimateplussb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import pl.portfolio.estimateplussb.service.EmailService;
import pl.portfolio.estimateplussb.service.EmailServiceImpl;
import pl.portfolio.estimateplussb.validator.PasswordValidator;

import javax.validation.Validator;

@EnableAsync
@SpringBootApplication
@ServletComponentScan
public class EstimatePlusSbApplication {

    public static void main(String[] args) {
        SpringApplication.run(EstimatePlusSbApplication.class, args);
    }


    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public PasswordValidator passwordValidator()
    {
        return new PasswordValidator();
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(20000000);
        return multipartResolver;
    }

    @Bean
    public EmailService emailService() {
        return new EmailServiceImpl();
    }

}

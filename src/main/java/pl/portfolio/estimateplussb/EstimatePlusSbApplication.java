package pl.portfolio.estimateplussb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import pl.portfolio.estimateplussb.validator.PasswordValidator;

import javax.validation.Validator;

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

}

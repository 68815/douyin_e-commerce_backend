package app;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DouyinECommerceApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
        System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
        System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

        ConfigurableApplicationContext context = SpringApplication.run(DouyinECommerceApplication.class, args);
        /*CodeGenerator codeGenerator = context.getBean(CodeGenerator.class);
        codeGenerator.generateCode();*/
    }
}

package ms.asp.appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;

@SpringBootApplication
@DependsOnDatabaseInitialization
public class Application {

    public static void main(String[] args) {
	SpringApplication.run(Application.class, args);
    }
}

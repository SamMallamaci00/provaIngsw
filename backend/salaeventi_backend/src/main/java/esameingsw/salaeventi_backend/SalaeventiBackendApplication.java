package esameingsw.salaeventi_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SalaeventiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalaeventiBackendApplication.class, args);
    }

}

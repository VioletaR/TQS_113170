package pt.ua.deti.tqs.lab06_05;

import org.springframework.boot.SpringApplication;

public class TestLab0605Application {

    public static void main(String[] args) {
        SpringApplication.from(Lab0605Application::main).with(TestcontainersConfiguration.class).run(args);
    }

}

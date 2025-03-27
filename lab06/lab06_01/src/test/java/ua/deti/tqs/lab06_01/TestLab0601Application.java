package ua.deti.tqs.lab06_01;

import org.springframework.boot.SpringApplication;

public class TestLab0601Application {

    public static void main(String[] args) {
        SpringApplication.from(Lab0601Application::main).with(TestcontainersConfiguration.class).run(args);
    }

}

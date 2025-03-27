package ua.deti.tqs.lab06_04.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "CAR")
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    private String maker;
    private String model;

    public Car(Long carId, String maker, String model) {
        this.carId = carId;
        this.maker = maker;
        this.model = model;
    }

    public Car(String maker, String model) {
        this.maker = maker;
        this.model = model;
    }
}
package ua.deti.tqc.lab02_3;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
public class Product {
    private Integer id;
    private String image;
    private String description;
    private Double price;
    private String title;
    private String category;
    private Map<String, Double> rating;

    public Product() {}

}

package ua.deti.tqs.backend.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserMealDTO {
    private Long id;
    private Long userId;
    private Long mealId;
    private Long restaurantId;
    private String code;
    private String meal;
    private Boolean isCheck = false;


}

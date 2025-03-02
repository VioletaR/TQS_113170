package ua.deti.tqs.lab03_02.unit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.deti.tqs.lab03_02.controller.CarController;
import ua.deti.tqs.lab03_02.entity.Car;
import ua.deti.tqs.lab03_02.service.CarManagerService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CarController.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private CarManagerService carManagerService;

    @Test
    void ifPostCar_thenCreateCar() throws Exception {
        Car car = new Car("maker","model");

        when( carManagerService.save(Mockito.any())).thenReturn(car);

        mockMvc.perform(
                post("/api/v1/car").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(car)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.maker", is("maker")))
        .andExpect(jsonPath("$.model", is("model")));


        verify(carManagerService, times(1)).save(Mockito.any());

    }

    @Test
    void givenManyCars_whenGetCars_thenReturnJsonArray() throws Exception {
        Car car1 = new Car("maker1", "model1");
        Car car2 = new Car("maker2", "model2");

        List<Car> allCars = Arrays.asList(car1, car2);

        when(carManagerService.getAllCars()).thenReturn(allCars);

        mockMvc.perform(get("/api/v1/car"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].maker", is(car1.getMaker())))
                .andExpect(jsonPath("$[1].maker", is(car2.getMaker())));

        verify(carManagerService, times(1)).getAllCars();
    }


    // test for getCarById HOW TO DO THIS?
//    @Test
//    void givenCarId_whenGetCarById_thenReturnJson() throws Exception {
//        Car car = new Car("maker", "model");
//        car.setId(1L);
//
//        when(carManagerService.getCarDetails(1L)).thenReturn(car);
//
//        mockMvc.perform(get("/api/v1/car/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.maker", is(car.getMaker())))
//                .andExpect(jsonPath("$.model", is(car.getModel()));
//
//        verify(carManagerService, times(1)).getCarDetails(1L);
//    }

}

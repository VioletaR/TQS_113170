package pt.ua.deti.tqs.lab3_2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
class ControllerTest {

    @Autowired
    private MockMvc mvc;    //entry point to the web framework

    @MockBean
    private CarManagerService service;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void whenPostCar_thenCreateCar() throws Exception {
        Car car = new Car("Peugeot", "3008");

        when(service.save(Mockito.any())).thenReturn(car);

        mvc.perform(
                   post("/api/cars").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(car)))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.maker", is("Peugeot")))
           .andExpect(jsonPath("$.model", is("3008")));

        verify(service, times(1)).save(Mockito.any());
    }

    @Test
    void givenManyEmployees_whenGetEmployees_thenReturnJsonArray() throws Exception {
        Car peugeot = new Car("Peugeot", "3008");
        Car citroen = new Car("Citroen", "C4");
        Car renault = new Car("Renault", "Kangoo");

        List<Car> allCars = Arrays.asList(peugeot, citroen, renault);

        when(service.getAllCars()).thenReturn(allCars);

        mvc.perform(
                   get("/api/cars").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(3)))
           .andExpect(jsonPath("$[0].maker", is(peugeot.getMaker())))
           .andExpect(jsonPath("$[1].maker", is(citroen.getMaker())))
           .andExpect(jsonPath("$[2].maker", is(renault.getMaker())));

        verify(service, times(1)).getAllCars();
    }
}

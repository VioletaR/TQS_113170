package ua.deti.tqs.backend.entities.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeatherIPMA {
    private String iUv;
    private String tempMin;
    private String tempMax;
    private String windDirection;
    private String precipitationProb;
    private int typeOfWeatherId;
    private int precipitationIntensity;
}

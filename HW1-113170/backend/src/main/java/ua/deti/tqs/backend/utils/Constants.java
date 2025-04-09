package ua.deti.tqs.backend.utils;

import lombok.Getter;

@Getter
public final class Constants {
    public static final String API_PATH = "api/";
    public static final String API_PATH_PRIVATE = API_PATH + "private/";
    public static final String API_PATH_V1 = API_PATH + "v1/";
    public static final String API_PATH_PRIVATE_V1 = API_PATH_PRIVATE + "v1/";

    public static final String BASE_URL_LOCATIONS = "https://api.ipma.pt/public-data/forecast/locations.json";
    public static final String BASE_URL_FORECAST = "https://api.ipma.pt/public-data/forecast/aggregate/";
}

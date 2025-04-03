package ua.deti.tqs.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;


public record Forecast(
        @JsonProperty("tMin") String minTemp,
        @JsonProperty("tMax") String maxTemp,
        @JsonProperty("iUv") String uvIndex,
        @JsonProperty("idTipoTempo") int weatherTypeId,
        @JsonProperty("idIntensidadePrecipita") int precipitationIntensityId,
        @JsonProperty("globalIdLocal") int globalIdLocal,
        @JsonProperty("probabilidadePrecipita") String precipitationProbability,
        @JsonProperty("dataPrev") String forecastDate,
        @JsonProperty("ddVento") String windDirection,
        @JsonProperty("idFfxVento") int windTypeId,
        @JsonProperty("dataUpdate") String updateDate,
        @JsonProperty("idPeriodo") int periodId
) {}
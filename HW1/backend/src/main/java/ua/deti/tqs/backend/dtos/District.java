package ua.deti.tqs.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;


public record District(
        @JsonProperty("idRegiao") int regionId,
        @JsonProperty("idAreaAviso") String warningAreaId,
        @JsonProperty("idConcelho") int municipalityId,
        @JsonProperty("globalIdLocal") int globalIdLocal,
        @JsonProperty("latitude") String latitude,
        @JsonProperty("idDistrito") int districtId,
        @JsonProperty("local") String local,
        @JsonProperty("longitude") String longitude
) {}
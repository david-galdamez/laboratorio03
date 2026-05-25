package com.laboratorio.laboratorio03.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSpecimenRequest {
    private String name;
    private String region;
    private Integer dangerLevel;
    private Boolean isFriendly;
}
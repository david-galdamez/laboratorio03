package com.laboratorio.laboratorio03.dto;

import com.laboratorio.laboratorio03.model.Specimen;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class SpecimenMapper {

    public Specimen toEntityCreate(CreateSpecimenRequest request) {
        return Specimen.builder()
                .name(request.getName())
                .region(request.getRegion())
                .dangerLevel(request.getDangerLevel())
                .isFriendly(request.getIsFriendly())
                .build();
    }

    public Specimen toEntityUpdate(UpdateSpecimenRequest request, UUID id) {
        return Specimen.builder()
                .id(id)
                .name(request.getName())
                .region(request.getRegion())
                .dangerLevel(request.getDangerLevel())
                .isFriendly(request.getIsFriendly())
                .build();
    }

    public SpecimenResponse toDto(Specimen specimen) {
        return SpecimenResponse.builder()
                .id(specimen.getId())
                .name(specimen.getName())
                .region(specimen.getRegion())
                .dangerLevel(specimen.getDangerLevel())
                .isFriendly(specimen.getIsFriendly())
                .build();
    }


    public PageableResponse<SpecimenResponse> toPageableResponse(
            Page<Specimen> page,
            String sortBy,
            String sortOrder) {

        List<SpecimenResponse> content = page.getContent()
                .stream()
                .map(this::toDto)
                .toList();

        return PageableResponse.<SpecimenResponse>builder()
                .content(content)
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .sortBy(sortBy)
                .sortOrder(sortOrder)
                .build();
    }
}
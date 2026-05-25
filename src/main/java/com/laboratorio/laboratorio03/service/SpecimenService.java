package com.laboratorio.laboratorio03.service;

import com.laboratorio.laboratorio03.dto.*;
import com.laboratorio.laboratorio03.exception.ResourceNotFoundException;
import com.laboratorio.laboratorio03.model.Specimen;
import com.laboratorio.laboratorio03.repository.SpecimenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpecimenService {

    private final SpecimenRepository specimenRepository;
    private final SpecimenMapper specimenMapper;

    public PageableResponse<SpecimenResponse> getAllSpecimens(
            int page,
            int size,
            String sortBy,
            String sortOrder) {

        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Specimen> specimenPage = specimenRepository.findAll(pageable);

        return specimenMapper.toPageableResponse(specimenPage, sortBy, sortOrder);
    }

    public SpecimenResponse getSpecimenById(UUID id) {
        Specimen specimen = specimenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specimen", "id", id));

        return specimenMapper.toDto(specimen);
    }

    @Transactional
    public SpecimenResponse createSpecimen(CreateSpecimenRequest requestDTO) {
        Specimen specimen = specimenMapper.toEntityCreate(requestDTO);
        Specimen saved = specimenRepository.save(specimen);
        return specimenMapper.toDto(saved);
    }

    @Transactional
    public SpecimenResponse updateSpecimen(UUID id, UpdateSpecimenRequest requestDTO) {
        Specimen existing = specimenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specimen", "id", id));

        var newSpecimen = specimenMapper.toEntityUpdate(requestDTO, existing.getId());
        Specimen updated = specimenRepository.save(newSpecimen);
        return specimenMapper.toDto(updated);
    }

    @Transactional
    public void deleteSpecimen(UUID id) {
        if (!specimenRepository.existsById(id)) {
            throw new ResourceNotFoundException("Specimen", "id", id);
        }
        specimenRepository.deleteById(id);
    }
}

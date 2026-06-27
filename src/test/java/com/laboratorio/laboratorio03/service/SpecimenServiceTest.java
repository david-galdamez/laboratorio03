package com.laboratorio.laboratorio03.service;

import com.laboratorio.laboratorio03.dto.*;
import com.laboratorio.laboratorio03.exception.ResourceNotFoundException;
import com.laboratorio.laboratorio03.model.Specimen;
import com.laboratorio.laboratorio03.repository.SpecimenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecimenServiceTest {

    @Mock
    private SpecimenRepository specimenRepository;

    @Mock
    private SpecimenMapper specimenMapper;

    @InjectMocks
    private SpecimenService specimenService;

    @Test
    void getAllSpecimens_shouldReturnPageableResponse() {
        int page = 0, size = 10;
        String sortBy = "name", sortOrder = "asc";

        Specimen specimen = Specimen.builder()
                .id(UUID.randomUUID())
                .name("Bokoblin")
                .region("Great Plateau")
                .dangerLevel(2)
                .isFriendly(false)
                .build();

        Page<Specimen> specimenPage = new PageImpl<>(List.of(specimen));
        when(specimenRepository.findAll(any(Pageable.class))).thenReturn(specimenPage);

        SpecimenResponse dto = SpecimenResponse.builder()
                .id(specimen.getId())
                .name(specimen.getName())
                .region(specimen.getRegion())
                .dangerLevel(specimen.getDangerLevel())
                .isFriendly(specimen.getIsFriendly())
                .build();

        PageableResponse<SpecimenResponse> expected = PageableResponse.<SpecimenResponse>builder()
                .content(List.of(dto))
                .currentPage(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .first(true)
                .last(true)
                .sortBy(sortBy)
                .sortOrder(sortOrder)
                .build();

        when(specimenMapper.toPageableResponse(specimenPage, sortBy, sortOrder)).thenReturn(expected);

        PageableResponse<SpecimenResponse> result = specimenService.getAllSpecimens(page, size, sortBy, sortOrder);

        assertEquals(1, result.getContent().size());
        assertEquals("Bokoblin", result.getContent().getFirst().getName());
        verify(specimenRepository).findAll(any(Pageable.class));
    }

    @Test
    void getSpecimenById_whenFound_shouldReturnSpecimen() {
        UUID id = UUID.randomUUID();
        Specimen specimen = Specimen.builder().id(id).name("Lizalfos").build();
        SpecimenResponse dto = SpecimenResponse.builder().id(id).name("Lizalfos").build();

        when(specimenRepository.findById(id)).thenReturn(Optional.of(specimen));
        when(specimenMapper.toDto(specimen)).thenReturn(dto);

        SpecimenResponse result = specimenService.getSpecimenById(id);

        assertEquals("Lizalfos", result.getName());
        verify(specimenRepository).findById(id);
    }

    @Test
    void getSpecimenById_whenNotFound_shouldThrow() {
        UUID id = UUID.randomUUID();
        when(specimenRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> specimenService.getSpecimenById(id));
    }

    @Test
    void createSpecimen_shouldSaveAndReturnDto() {
        CreateSpecimenRequest request = CreateSpecimenRequest.builder()
                .name("Wizzrobe")
                .region("Gerudo")
                .dangerLevel(4)
                .isFriendly(false)
                .build();

        Specimen entity = Specimen.builder().name("Wizzrobe").region("Gerudo").build();
        Specimen saved = Specimen.builder().id(UUID.randomUUID()).name("Wizzrobe").region("Gerudo").build();
        SpecimenResponse dto = SpecimenResponse.builder().id(saved.getId()).name("Wizzrobe").region("Gerudo").build();

        when(specimenMapper.toEntityCreate(request)).thenReturn(entity);
        when(specimenRepository.save(entity)).thenReturn(saved);
        when(specimenMapper.toDto(saved)).thenReturn(dto);

        SpecimenResponse result = specimenService.createSpecimen(request);

        assertEquals("Wizzrobe", result.getName());
        verify(specimenRepository).save(entity);
    }

    @Test
    void updateSpecimen_whenFound_shouldUpdateAndReturn() {
        UUID id = UUID.randomUUID();
        UpdateSpecimenRequest request = UpdateSpecimenRequest.builder()
                .name("Silver Bokoblin")
                .dangerLevel(4)
                .build();

        Specimen existing = Specimen.builder().id(id).name("Bokoblin").region("Hyrule").build();
        Specimen updatedEntity = Specimen.builder().id(id).name("Silver Bokoblin").dangerLevel(4).build();
        SpecimenResponse dto = SpecimenResponse.builder().id(id).name("Silver Bokoblin").dangerLevel(4).build();

        when(specimenRepository.findById(id)).thenReturn(Optional.of(existing));
        when(specimenMapper.toEntityUpdate(request, id)).thenReturn(updatedEntity);
        when(specimenRepository.save(updatedEntity)).thenReturn(updatedEntity);
        when(specimenMapper.toDto(updatedEntity)).thenReturn(dto);

        SpecimenResponse result = specimenService.updateSpecimen(id, request);

        assertEquals("Silver Bokoblin", result.getName());
        assertEquals(4, result.getDangerLevel());
        verify(specimenRepository).findById(id);
        verify(specimenRepository).save(updatedEntity);
    }

    @Test
    void updateSpecimen_whenNotFound_shouldThrow() {
        UUID id = UUID.randomUUID();
        when(specimenRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> specimenService.updateSpecimen(id, new UpdateSpecimenRequest()));
    }

    @Test
    void deleteSpecimen_whenFound_shouldDelete() {
        UUID id = UUID.randomUUID();
        when(specimenRepository.existsById(id)).thenReturn(true);

        specimenService.deleteSpecimen(id);

        verify(specimenRepository).deleteById(id);
    }

    @Test
    void deleteSpecimen_whenNotFound_shouldThrow() {
        UUID id = UUID.randomUUID();
        when(specimenRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> specimenService.deleteSpecimen(id));
        verify(specimenRepository, never()).deleteById(any());
    }
}

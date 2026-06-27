package com.laboratorio.laboratorio03.mapper;

import com.laboratorio.laboratorio03.dto.CreateSpecimenRequest;
import com.laboratorio.laboratorio03.dto.SpecimenMapper;
import com.laboratorio.laboratorio03.dto.SpecimenResponse;
import com.laboratorio.laboratorio03.dto.UpdateSpecimenRequest;
import com.laboratorio.laboratorio03.model.Specimen;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SpecimenMapperTest {

    private final SpecimenMapper mapper = new SpecimenMapper();

    @Test
    void toEntityCreate_shouldMapAllFields() {
        CreateSpecimenRequest request = CreateSpecimenRequest.builder()
                .name("Keese")
                .region("Hyrule Field")
                .dangerLevel(2)
                .isFriendly(false)
                .build();

        Specimen result = mapper.toEntityCreate(request);

        assertNull(result.getId());
        assertEquals("Keese", result.getName());
        assertEquals("Hyrule Field", result.getRegion());
        assertEquals(2, result.getDangerLevel());
        assertFalse(result.getIsFriendly());
    }

    @Test
    void toEntityUpdate_shouldMapAllFieldsAndKeepId() {
        UUID id = UUID.randomUUID();
        UpdateSpecimenRequest request = UpdateSpecimenRequest.builder()
                .name("Golden Keese")
                .region("Eldin")
                .dangerLevel(3)
                .isFriendly(false)
                .build();

        Specimen result = mapper.toEntityUpdate(request, id);

        assertEquals(id, result.getId());
        assertEquals("Golden Keese", result.getName());
        assertEquals("Eldin", result.getRegion());
        assertEquals(3, result.getDangerLevel());
        assertFalse(result.getIsFriendly());
    }

    @Test
    void toDto_shouldMapAllFields() {
        UUID id = UUID.randomUUID();
        Specimen specimen = Specimen.builder()
                .id(id)
                .name("Hinox")
                .region("Hebra")
                .dangerLevel(5)
                .isFriendly(false)
                .build();

        SpecimenResponse result = mapper.toDto(specimen);

        assertEquals(id, result.getId());
        assertEquals("Hinox", result.getName());
        assertEquals("Hebra", result.getRegion());
        assertEquals(5, result.getDangerLevel());
        assertFalse(result.getIsFriendly());
    }
}

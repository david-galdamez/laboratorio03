package com.laboratorio.laboratorio03.controller;

import com.laboratorio.laboratorio03.dto.CreateSpecimenRequest;
import com.laboratorio.laboratorio03.dto.PageableResponse;
import com.laboratorio.laboratorio03.dto.SpecimenResponse;
import com.laboratorio.laboratorio03.dto.UpdateSpecimenRequest;
import com.laboratorio.laboratorio03.exception.GlobalExceptionHandler;
import com.laboratorio.laboratorio03.exception.ResourceNotFoundException;
import com.laboratorio.laboratorio03.service.SpecimenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpecimenController.class)
@Import(GlobalExceptionHandler.class)
class SpecimenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SpecimenService specimenService;

    @Test
    void getAllSpecimens_shouldReturn200() throws Exception {
        PageableResponse<SpecimenResponse> page = PageableResponse.<SpecimenResponse>builder()
                .content(List.of())
                .currentPage(0)
                .pageSize(10)
                .totalElements(0)
                .totalPages(0)
                .first(true)
                .last(true)
                .sortBy("id")
                .sortOrder("asc")
                .build();

        when(specimenService.getAllSpecimens(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(page);

        mockMvc.perform(get("/api/specimens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Specimens traidos correctamente"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    void getSpecimenById_whenFound_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        SpecimenResponse dto = SpecimenResponse.builder()
                .id(id)
                .name("Molduga")
                .region("Gerudo Desert")
                .dangerLevel(5)
                .isFriendly(false)
                .build();

        when(specimenService.getSpecimenById(id)).thenReturn(dto);

        mockMvc.perform(get("/api/specimens/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Specimen encontrado correctamente"))
                .andExpect(jsonPath("$.data.name").value("Molduga"));
    }

    @Test
    void getSpecimenById_whenNotFound_shouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        when(specimenService.getSpecimenById(id))
                .thenThrow(new ResourceNotFoundException("Specimen", "id", id));

        mockMvc.perform(get("/api/specimens/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void createSpecimen_shouldReturn201() throws Exception {
        CreateSpecimenRequest request = CreateSpecimenRequest.builder()
                .name("Stone Talus")
                .region("Death Mountain")
                .dangerLevel(4)
                .isFriendly(false)
                .build();

        SpecimenResponse dto = SpecimenResponse.builder()
                .id(UUID.randomUUID())
                .name("Stone Talus")
                .region("Death Mountain")
                .dangerLevel(4)
                .isFriendly(false)
                .build();

        when(specimenService.createSpecimen(any())).thenReturn(dto);

        mockMvc.perform(post("/api/specimens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Specimen Creado correctamente"))
                .andExpect(jsonPath("$.data.name").value("Stone Talus"));
    }

    @Test
    void createSpecimen_withInvalidBody_shouldReturn400() throws Exception {
        CreateSpecimenRequest request = CreateSpecimenRequest.builder()
                .name("")
                .region("")
                .build();

        mockMvc.perform(post("/api/specimens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSpecimen_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateSpecimenRequest request = UpdateSpecimenRequest.builder()
                .name("Igneo Talus")
                .region("Death Mountain")
                .dangerLevel(5)
                .isFriendly(false)
                .build();

        SpecimenResponse dto = SpecimenResponse.builder()
                .id(id)
                .name("Igneo Talus")
                .region("Death Mountain")
                .dangerLevel(5)
                .isFriendly(false)
                .build();

        when(specimenService.updateSpecimen(eq(id), any())).thenReturn(dto);

        mockMvc.perform(put("/api/specimens/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Specimen actualizado correctamente"))
                .andExpect(jsonPath("$.data.name").value("Igneo Talus"));
    }

    @Test
    void updateSpecimen_whenNotFound_shouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateSpecimenRequest request = UpdateSpecimenRequest.builder()
                .name("Igneo Talus")
                .region("Death Mountain")
                .dangerLevel(5)
                .isFriendly(false)
                .build();

        when(specimenService.updateSpecimen(eq(id), any()))
                .thenThrow(new ResourceNotFoundException("Specimen", "id", id));

        mockMvc.perform(put("/api/specimens/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSpecimen_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/specimens/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Specimen eliminado correctamente"));
    }

    @Test
    void deleteSpecimen_whenNotFound_shouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new ResourceNotFoundException("Specimen", "id", id))
                .when(specimenService).deleteSpecimen(id);

        mockMvc.perform(delete("/api/specimens/{id}", id))
                .andExpect(status().isNotFound());
    }
}

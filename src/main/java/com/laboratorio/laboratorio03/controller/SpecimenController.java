package com.laboratorio.laboratorio03.controller;

import com.laboratorio.laboratorio03.dto.*;
import com.laboratorio.laboratorio03.service.SpecimenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/specimens")
@RequiredArgsConstructor
public class SpecimenController {

    private final SpecimenService specimenService;
    @GetMapping
    public ResponseEntity<GeneralResponse<PageableResponse<SpecimenResponse>>> getAllSpecimens(
            @RequestParam(defaultValue = "0")   int page,
            @RequestParam(defaultValue = "10")  int size,
            @RequestParam(defaultValue = "id")  String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            HttpServletRequest request) {

        PageableResponse<SpecimenResponse> data =
                specimenService.getAllSpecimens(page, size, sortBy, sortOrder);

        return buildResponse(
                "Specimens traidos correctamente",
                HttpStatus.OK,
                data,
                request.getRequestURI()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse<SpecimenResponse>> getSpecimenById(
            @PathVariable UUID id,
            HttpServletRequest request) {

        SpecimenResponse data = specimenService.getSpecimenById(id);

        return buildResponse(
                "Specimen encontrado correctamente",
                HttpStatus.OK,
                data,
                request.getRequestURI()
        );
    }

    @PostMapping
    public ResponseEntity<GeneralResponse<SpecimenResponse>> createSpecimen(
            @Valid @RequestBody CreateSpecimenRequest requestDTO,
            HttpServletRequest request) {

        SpecimenResponse data = specimenService.createSpecimen(requestDTO);

        return buildResponse(
                "Specimen Creado correctamente",
                HttpStatus.CREATED,
                data,
                request.getRequestURI()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse<SpecimenResponse>> updateSpecimen(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSpecimenRequest requestDTO,
            HttpServletRequest request) {

        SpecimenResponse data = specimenService.updateSpecimen(id, requestDTO);

        return buildResponse(
                "Specimen actualizado correctamente",
                HttpStatus.OK,
                data,
                request.getRequestURI()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse<Void>> deleteSpecimen(
            @PathVariable UUID id,
            HttpServletRequest request) {

        specimenService.deleteSpecimen(id);

        return buildResponse(
                "Specimen eliminado correctamente",
                HttpStatus.OK,
                null,
                request.getRequestURI()
        );
    }
    private <T> ResponseEntity<GeneralResponse<T>> buildResponse(
            String message,
            HttpStatus status,
            T data,
            String path) {

        GeneralResponse<T> response = GeneralResponse.<T>builder()
                .message(message)
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .path(path)
                .data(data)
                .build();

        return new ResponseEntity<>(response, status);
    }
}

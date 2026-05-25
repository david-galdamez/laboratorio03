package com.laboratorio.laboratorio03.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format(
                "⚠️ Sheikah Slate Error: %s not found with %s = '%s' in Sheikah Slate records",
                resourceName, fieldName, fieldValue
        ));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceName = "Recurso";
        this.fieldName = "id";
        this.fieldValue = "desconocido";
    }

}

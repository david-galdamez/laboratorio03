package com.laboratorio.laboratorio03.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponse<T> {

    private List<T> content;

    private int currentPage;

    private int pageSize;

    private long totalElements;

    private int totalPages;

    private boolean first;

    private boolean last;

    private String sortBy;

    private String sortOrder;
}

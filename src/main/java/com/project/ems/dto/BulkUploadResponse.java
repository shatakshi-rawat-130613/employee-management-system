package com.project.ems.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkUploadResponse {

    private Integer successCount;
    private Integer failureCount;
    private List<UploadError> errors;
}

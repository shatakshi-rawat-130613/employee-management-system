package com.project.ems.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadError {

    private Integer rowNumber;
    private String field;
    private String message;
}

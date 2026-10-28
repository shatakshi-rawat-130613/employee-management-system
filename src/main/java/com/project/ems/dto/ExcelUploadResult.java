package com.project.ems.dto;


import com.project.ems.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelUploadResult {

    private List<Employee> employees;
    private List<UploadError> errors;
}

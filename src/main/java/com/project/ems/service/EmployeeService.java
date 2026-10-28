package com.project.ems.service;

import com.project.ems.Enum.Role;
import com.project.ems.dto.BulkUploadResponse;
import com.project.ems.dto.EmployeeRequest;
import com.project.ems.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface EmployeeService {

    Employee createEmployee(EmployeeRequest request);

    Employee getEmployeeById(Long id);

    Employee updateEmployee(Long id, EmployeeRequest request);

    void deleteEmployee(Long id);

    List<Employee> getAllEmployees();

    List<Employee> sortEmployees(String sortBy);

   BulkUploadResponse uploadResponse(MultipartFile file);

   List<Employee> searchEmployees(String keyword);

   List<Employee> getEmployeesByDepartment(String department);

   Page<Employee> getEmployeesWithPagination(int page, int size);

    List<Employee> getEmployeesByRole(Role role);

    List<Employee> getEmployeesBySalary(Double salary);

    List<Employee> getEmployeesByName(String name);
}

package com.project.ems.controller;

import com.project.ems.Enum.Role;
import com.project.ems.dto.BulkUploadResponse;
import com.project.ems.dto.EmployeeRequest;
import com.project.ems.entity.Employee;
import com.project.ems.service.EmployeeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
// import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/employees")
public class EmployeeController{
    private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @PostMapping
    public Employee createEmployee(@RequestBody EmployeeRequest request) {
        return employeeService.createEmployee(request);
    }
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }



    @PutMapping("/{id}")
    public Employee updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeRequest request) {

        return employeeService.updateEmployee(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    @GetMapping("/sort")
    public List<Employee> sortEmployees(@RequestParam String sortBy) {
        return employeeService.sortEmployees(sortBy);
    }

    @PostMapping(value = "/upload" , consumes = "multipart/form-data")
    public ResponseEntity<BulkUploadResponse> uploadEmployees(
            @RequestParam("file") MultipartFile file) {

        BulkUploadResponse response = employeeService.uploadResponse(file);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(
            @RequestParam String keyword) {

        List<Employee> employees =
                employeeService.searchEmployees(keyword);

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/filter/department")
    public ResponseEntity<List<Employee>> filterEmployees(
            @RequestParam String department) {

        return ResponseEntity.ok(
                employeeService.getEmployeesByDepartment(department)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllEmployees(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {

        //If page and size is provided
//        if (page != null && size != null) {
//            return ResponseEntity.ok(
//                    employeeService.getEmployeesWithPagination(page, size)
//            );
//        }

        if (page != null || size != null) {

            // Default values
            if (page == null) {
                page = 0;
            }

            if (size == null) {
                size = 5;
            }

            return ResponseEntity.ok(
                    employeeService.getEmployeesWithPagination(page, size)
            );
        }



        return ResponseEntity.ok(
                employeeService.getAllEmployees()
        );
    }


//    @GetMapping("/paginated")
//    public ResponseEntity<Page<Employee>>
//    getEmployeesWithPagination(
//
//            @RequestParam(defaultValue = "0")
//            int page,
//
//            @RequestParam(defaultValue = "5")
//            int size) {
//
//        return ResponseEntity.ok(
//                employeeService.getEmployeesWithPagination(
//                        page,
//                        size
//                )
//        );
//    }

    @GetMapping("/filter/role")
    public ResponseEntity<List<Employee>>
    getEmployeesByRole(
            @RequestParam Role role) {

        return ResponseEntity.ok(
                employeeService.getEmployeesByRole(role)
        );
    }

    @GetMapping("/filter/salary")
    public ResponseEntity<List<Employee>>
    getEmployeesBySalary(
            @RequestParam Double salary) {

        return ResponseEntity.ok(
                employeeService.getEmployeesBySalary(salary)
        );
    }

    @GetMapping("/filter/name")
    public ResponseEntity<List<Employee>>
    getEmployeesByName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                employeeService.getEmployeesByName(name)
        );
    }

}

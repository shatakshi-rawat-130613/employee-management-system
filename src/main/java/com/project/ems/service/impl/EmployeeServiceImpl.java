package com.project.ems.service.impl;

import com.project.ems.Enum.Role;
import com.project.ems.dto.BulkUploadResponse;
import com.project.ems.dto.EmployeeRequest;
import com.project.ems.dto.ExcelUploadResult;
import com.project.ems.entity.Employee;
import com.project.ems.exception.EmployeeNotFoundException;
import com.project.ems.repository.EmployeeRepository;
import com.project.ems.service.EmployeeService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.project.ems.util.ExcelHelper;


import java.time.Instant;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExcelHelper excelHelper;
//
//    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
//        this.employeeRepository = employeeRepository;
//    }
    @Override
    public Employee createEmployee(@Nonnull EmployeeRequest request) {
        Employee employee = new Employee();

        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setDepartment(request.getDepartment());
        employee.setRole(request.getRole());
        employee.setSalary(request.getSalary());
        employee.setCreatedAt(Instant.now());
        employee.setUpdatedAt(Instant.now());
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException("Employee not found with id: "+ id));
    }

    @Override
    public Employee updateEmployee(Long id, @Nonnull EmployeeRequest request) {
        Employee existingEmployee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: "+ id));
        existingEmployee.setName(request.getName() == null? existingEmployee.getName() : request.getName());

        if(request.getEmail() != null && !request.getEmail().isBlank()) {
            existingEmployee.setEmail(request.getEmail());
        }
        if(request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            existingEmployee.setPhoneNumber(request.getPhoneNumber());
        }

        if(request.getPassword() != null && !request.getPassword().isBlank()) {
            existingEmployee.setPassword(request.getPassword());
        }

        if(request.getDepartment() != null && !request.getDepartment().isBlank()) {
            existingEmployee.setDepartment(request.getDepartment());
        }

        if(request.getRole() != null) {
            existingEmployee.setRole(request.getRole());
        }

        if(request.getSalary() != null) {
            existingEmployee.setSalary(request.getSalary());
        }
        return employeeRepository.save(existingEmployee);

    }

    @Override
    public void deleteEmployee(Long id) {
        if(!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee not found with id: "+ id);
        }
        employeeRepository.deleteById(id);

    }

    @Override
    public List<Employee> sortEmployees(String sortBy) {
        return employeeRepository.findAll(Sort.by(sortBy));
    }

    @Override
    public BulkUploadResponse uploadResponse(MultipartFile file) {

        ExcelUploadResult result =
                excelHelper.convertExcelToEmployees(file);

        List<Employee> employees = result.getEmployees();

        employeeRepository.saveAll(employees);


        return new BulkUploadResponse(
                employees.size(),
                result.getErrors().size(),
                result.getErrors()
        );


    }

    @Override
    public List<Employee> searchEmployees(String keyword) {
        return employeeRepository.searchEmployees(keyword);

    }

    @Override
    public List<Employee> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartmentIgnoreCase(department);
    }

//    @Override
//    public List<Employee> getAllEmployees() {
//        return employeeRepository.findAll();
//    }
//
//    @Override
//    public Page<Employee> getEmployeesWithPagination(
//            int page,
//            int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//
//        return employeeRepository.findAll(pageable);
//    }


    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Page<Employee> getEmployeesWithPagination(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return employeeRepository.findAll(pageable);
    }

    @Override
    public List<Employee> getEmployeesByRole(Role role) {
        return employeeRepository.findByRole(role);
    }

    @Override
    public List<Employee> getEmployeesBySalary(Double salary) {
        return employeeRepository
                .findBySalary(salary);
    }

    @Override
    public List<Employee> getEmployeesByName(String name) {
        return employeeRepository
                .findByNameContainingIgnoreCase(name);
    }

}



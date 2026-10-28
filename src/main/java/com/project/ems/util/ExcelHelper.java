package com.project.ems.util;

import com.project.ems.Enum.Role;
import com.project.ems.dto.ExcelUploadResult;
import com.project.ems.dto.UploadError;
import com.project.ems.entity.Employee;
import com.project.ems.repository.EmployeeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.util.*;

@Component
public class ExcelHelper {

    private final EmployeeRepository employeeRepository;

    public ExcelHelper(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public ExcelUploadResult convertExcelToEmployees(MultipartFile file) {
        List<Employee> employees = new ArrayList<>();
        Set<String> emails = new HashSet<>();
        List<UploadError> errors = new ArrayList<>();

        try (
                InputStream inputStream = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream);
                ) {
//            InputStream inputStream = file.getInputStream();
//            Workbook workbook = new XSSFWorkbook(inputStream);

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                try {

                    boolean empty = true;

                    for (Cell cell : row) {
                        if (!formatter.formatCellValue(cell).isBlank()) {
                            empty = false;
                            break;
                        }
                    }

                    if (empty) {
                        continue;
                    }

                    Employee employee = new Employee();
                    boolean rowHasError = false;

                    //EMAIL
                    String email = formatter.formatCellValue(row.getCell(1)).trim();
                    if (emails.contains(email)) {
                        errors.add(
                                new UploadError(
                                        row.getRowNum() + 1,
                                        "email",
                                        "Duplicate email in Excel"
                                )
                        );
                        rowHasError = true;
                    }
                    if (!email.matches(
                            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
                    )) {
                        errors.add(
                                new UploadError(
                                        row.getRowNum() + 1,
                                        "email",
                                        "Invalid email format"
                                )
                        );
                        rowHasError = true;
                    }

                    if (employeeRepository.existsByEmail(email)) {
                        errors.add(
                                new UploadError(
                                        row.getRowNum() + 1,
                                        "email",
                                        "Email already exists in the database"
                                )
                        );
                        rowHasError = true;
                    }
                    emails.add(email);

                    //NAME
                    String name = formatter.formatCellValue(row.getCell(0)).trim();
                    if (name.isBlank()) {
                        errors.add(
                                new UploadError(
                                        row.getRowNum() + 1,
                                        "name",
                                        "Name cannot be blank"
                                )
                        );
                        rowHasError = true;
                    }

                    //PASSWORD
                    String password = formatter.formatCellValue(row.getCell(2)).trim();
                    if (password.isBlank()) {
                        errors.add(
                                new UploadError(
                                        row.getRowNum() + 1,
                                        "password",
                                        "Password cannot be blank"
                                )
                        );
                        rowHasError = true;
                    }

                    //PHONE_NUMBER
                    String phoneNumber = formatter.formatCellValue(row.getCell(3)).trim();
                    if (phoneNumber.isBlank()) {
                        errors.add(
                                new UploadError(
                                        row.getRowNum() + 1,
                                        "phoneNumber",
                                        "Phone Number cannot be blank"
                                )
                        );
                        rowHasError = true;
                    }
                    if (!phoneNumber.matches("\\d{10}")) {
                        errors.add(
                                new UploadError(
                                        row.getRowNum() + 1,
                                        "phoneNumber",
                                        "Invalid Phone Number Format"
                                )
                        );
                        rowHasError = true;

                    }
//                double salary = Double.parseDouble(formatter.formatCellValue(row.getCell(6)));
//                if(salary <= 0){
//                    errors.add(
//                            new UploadError(
//                                    row.getRowNum() + 1,
//                                    "salary",
//                                    "Salary cannot be negative"
//                            )
//                    );
//                    continue;
//                }
                    //SALARY
                    String salaryText =
                            formatter.formatCellValue(row.getCell(6)).trim();

                    if (salaryText.isBlank()) {
                        errors.add(new UploadError(
                                row.getRowNum() + 1,
                                "salary",
                                "Salary cannot be blank"
                        ));
                        rowHasError = true;
                    }

                    double salary = 0;

                    try {
                        salary = Double.parseDouble(salaryText);
                    } catch (NumberFormatException e) {

                        errors.add(new UploadError(
                                row.getRowNum() + 1,
                                "salary",
                                "Invalid salary format"
                        ));
                        rowHasError = true;
                    }

                    if (salary <= 0) {
                        errors.add(new UploadError(
                                row.getRowNum() + 1,
                                "salary",
                                "Salary must be greater than zero"
                        ));
                        rowHasError = true;
                    }


                    //ROLE
                    String role = formatter.formatCellValue(row.getCell(5)).trim();
                    try {
                        employee.setRole(
                                Role.valueOf(role.toUpperCase())
                        );
                    } catch (IllegalArgumentException e) {
                        errors.add(
                                new UploadError(
                                        row.getRowNum() + 1,
                                        "role",
                                        "Invalid role entered"
                                )
                        );
                        rowHasError = true;
                    }


//                employee.setName(row.getCell(0).getStringCellValue());
//                employee.setEmail(row.getCell(1).getStringCellValue());
//                employee.setPassword(row.getCell(2).getStringCellValue());

                    List<String> validDepartments =
                            Arrays.asList("HR", "SALES", "IT", "FINANCE");

                    String department =
                            formatter.formatCellValue(row.getCell(4));

                    if (!validDepartments.contains(department.toUpperCase())) {
                        errors.add(
                                new UploadError(
                                        row.getRowNum() + 1,
                                        "department",
                                        "Invalid department format"
                                )
                        );
                        rowHasError = true;
                    }
                    if (rowHasError) {
                        continue;
                    }

                    //SETTING VALUES
                    employee.setName(name);
                    employee.setEmail(email);
                    employee.setPassword(password);
                    employee.setPhoneNumber(phoneNumber);
                    employee.setDepartment(department);
                    employee.setSalary(salary);

                    //ADDING VALUES
                    employees.add(employee);


                } catch (Exception e) {
                    errors.add(
                            new UploadError(
                                    row.getRowNum() + 1,
                                    "row",
                                    "Unexpected error : " + e.getMessage()
                            )
                    );
                }
            }
//            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException("Error reading Excel File", e);
        }
        employeeRepository.saveAll(employees);
        return new ExcelUploadResult(employees, errors);
            }
        }



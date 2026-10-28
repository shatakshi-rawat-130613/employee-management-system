package com.project.ems.repository;

import com.project.ems.Enum.Role;
import com.project.ems.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
        SELECT e FROM Employee e
        WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR e.phoneNumber LIKE CONCAT('%', :keyword, '%')
       """)
    List<Employee> searchEmployees(@Param("keyword") String keyword);

    List<Employee> findByDepartmentIgnoreCase(String department);

    List<Employee> findByRole(Role role);

    List<Employee> findBySalary(Double salary);

    List<Employee> findByNameContainingIgnoreCase(String name);
}

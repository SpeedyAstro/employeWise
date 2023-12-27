package in.astro.employwise.service;

import in.astro.employwise.dao.EmployeeRepository;
import in.astro.employwise.model.EmployeeDto;
import in.astro.employwise.model.EmployeeEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements IEmployeeService{
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public String addEmployee(@RequestBody EmployeeDto employeeDto) {
        // Validate and handle data
        // Generate UUID for the employee
        String employeeId = UUID.randomUUID().toString();
        employeeDto.setId(employeeId);
        String reportsToId = employeeDto.getReportsTo();
        if (reportsToId != null) {
            boolean managerExists = employeeRepository.existsById(reportsToId);  // Check if manager exists
            if (!managerExists) {
                throw new IllegalStateException("Cannot add employee: Manager with ID " + reportsToId + " does not exist.");
            }
        }
        System.out.println("Employee ID: " + employeeId);
        // Save employee to the database
        employeeRepository.save(convertDtoToEntity(employeeDto));

        return employeeId;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public String  deleteEmployee(String employeeId) {
        employeeRepository.deleteById(employeeId);
        return "Employee deleted successfully";
    }

    @Override
    public void updateEmployee(String employeeId, EmployeeDto employeeDto) {
        // Add validation and error handling
        EmployeeEntity existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        // Update employee details
        existingEmployee.setEmployeeName(employeeDto.getEmployeeName());
        existingEmployee.setPhoneNumber(employeeDto.getPhoneNumber());
        existingEmployee.setEmail(employeeDto.getEmail());
        existingEmployee.setReportsTo(employeeDto.getReportsTo());
        existingEmployee.setProfileImage(employeeDto.getProfileImage());

        // Save the updated employee
        employeeRepository.save(existingEmployee);
    }

    public EmployeeEntity convertDtoToEntity(EmployeeDto employeeDto) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setId(employeeDto.getId());
        employeeEntity.setEmployeeName(employeeDto.getEmployeeName());
        employeeEntity.setPhoneNumber(employeeDto.getPhoneNumber());
        employeeEntity.setEmail(employeeDto.getEmail());
        employeeEntity.setReportsTo(employeeDto.getReportsTo());
        employeeEntity.setProfileImage(employeeDto.getProfileImage());
        return employeeEntity;
    }

    private EmployeeDto convertEntityToDto(EmployeeEntity employeeEntity) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employeeEntity.getId());
        employeeDto.setEmployeeName(employeeEntity.getEmployeeName());
        employeeDto.setPhoneNumber(employeeEntity.getPhoneNumber());
        employeeDto.setEmail(employeeEntity.getEmail());
        employeeDto.setReportsTo(employeeEntity.getReportsTo());
        employeeDto.setProfileImage(employeeEntity.getProfileImage());
        // You may set other fields as needed

        return employeeDto;
    }

    @Override
    public EmployeeDto getNthLevelManager(String employeeId, int level) {
        // Implement logic to get nth level manager
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        return findNthLevelManager(employee, level);
    }

    public EmployeeDto findNthLevelManager(EmployeeEntity employee, int level) {
        if (level <= 0) {
            // Return the current employee as the manager if level is 0 or negative
            return convertEntityToDto(employee);
        }

        // Recursive call to find the nth level manager
        return employee.getReportsTo() != null
                ? getNthLevelManager(employee.getReportsTo(), level - 1)
                : null;
    }

    @Override
    public List<EmployeeDto> getAllEmployeesWithPaginationAndSorting(int page, int size, String sortBy) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<EmployeeEntity> employeePage = employeeRepository.findAll(pageable);

        return employeePage.getContent().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }
}

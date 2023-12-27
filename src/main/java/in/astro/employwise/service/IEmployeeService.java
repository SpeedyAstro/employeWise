package in.astro.employwise.service;

import in.astro.employwise.model.EmployeeDto;
import in.astro.employwise.model.EmployeeEntity;

import java.util.List;

public interface IEmployeeService {
    public String addEmployee(EmployeeDto employeeDto);
    public List<EmployeeDto> getAllEmployees();
    public String  deleteEmployee(String employeeId);
    public void updateEmployee(String employeeId, EmployeeDto employeeDto);
    public EmployeeDto getNthLevelManager(String employeeId, int level);
    public List<EmployeeDto> getAllEmployeesWithPaginationAndSorting(int page, int size, String sortBy);
}

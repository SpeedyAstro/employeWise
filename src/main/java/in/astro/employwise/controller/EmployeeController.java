package in.astro.employwise.controller;

// EmployeeController.java
import in.astro.employwise.model.EmployeeDto;
import in.astro.employwise.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @Value("${mail.level1manager.email}")
    private String level1ManagerEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/fetchAll")
    public List<EmployeeDto> getAllEmployees() {
        System.out.println("Inside getAllEmployees");
        return employeeService.getAllEmployees();
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee deleted successfully");
    }
    @PutMapping("/{employeeId}")
    public ResponseEntity<String> updateEmployee(@PathVariable String employeeId, @RequestBody EmployeeDto employeeDto) {
        employeeService.updateEmployee(employeeId, employeeDto);
        return ResponseEntity.ok("Employee updated successfully");
    }

    @GetMapping("/manager/{employeeId}/{level}")
    public ResponseEntity<EmployeeDto> getNthLevelManager(@PathVariable String employeeId, @PathVariable int level) {
        EmployeeDto manager = employeeService.getNthLevelManager(employeeId, level);
        return ResponseEntity.ok(manager);
    }

    @PostMapping("/add")
    public String addEmployee(@Valid @RequestBody EmployeeDto employeeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors
            return "error-page";
        }
        // Save employee to the database
        String  employeeId = employeeService.addEmployee(employeeDto);
        // Notify level 1 manager via email
        notifyLevel1Manager(employeeDto);
        return employeeId;
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDto>> getAllEmployeesWithPaginationAndSorting(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "employeeName") String sortBy) {
        List<EmployeeDto> employees = employeeService.getAllEmployeesWithPaginationAndSorting(page, size, sortBy);
        return ResponseEntity.ok(employees);
    }

    private void notifyLevel1Manager(EmployeeDto employeeDto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(level1ManagerEmail);
            message.setSubject("New Employee Addition");
            message.setText(
                    String.format(
                            "%s will now work under you. Mobile number is %s and email is %s.",
                            employeeDto.getEmployeeName(), employeeDto.getPhoneNumber(), employeeDto.getEmail()
                    )
            );
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}


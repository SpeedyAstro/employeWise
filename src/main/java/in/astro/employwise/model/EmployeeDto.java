package in.astro.employwise.model;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Email;
@Data
public class EmployeeDto {
    @NotBlank(message = "Employee ID is required")
    private String id;

    @NotBlank(message = "Employee name is required")
    private String employeeName;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    private String email;

    private String reportsTo;

    @Pattern(regexp = "https?://.+", message = "Invalid image URL format")
    private String profileImage;
}
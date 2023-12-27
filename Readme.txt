API Endpoints:

GET /fetchAll

Description: Retrieves a list of all employees.
Response: List of EmployeeDto objects.
DELETE //{employeeId}

Description: Deletes an employee with the specified ID.
Path Variable: employeeId (required, string)
Response: Success message: "Employee deleted successfully"
PUT //{employeeId}

Description: Updates an existing employee.
Path Variable: employeeId (required, string)
Request Body: EmployeeDto object with updated information.
Response: Success message: "Employee updated successfully"
GET /manager/{employeeId}/{level}

Description: Retrieves the Nth-level manager of a specified employee.
Path Variables:
employeeId (required, string)
level (required, integer)
Response: EmployeeDto object representing the manager, or an error response if not found.
POST /add

Description: Adds a new employee.
Request Body: EmployeeDto object with employee details.
Response: Generated employee ID on success, or an error response if validation fails.
GET /all

Description: Retrieves a list of employees with pagination and sorting.
Query Parameters:
page (optional, integer, default: 0)
size (optional, integer, default: 10)
sortBy (optional, string, default: "employeeName")
Response: Paginated list of EmployeeDto objects with the specified sorting.
Additional Notes:

Data Validation: The addEmployee endpoint performs validation on the incoming employee data.
Email Notification: The addEmployee endpoint sends an email notification to the Level 1 manager upon successful employee addition.
Error Handling: Consider implementing more robust error handling and informative responses for failed operations.
Security: It's recommended to implement authentication and authorization mechanisms to protect sensitive employee data.
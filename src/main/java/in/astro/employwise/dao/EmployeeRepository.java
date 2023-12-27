package in.astro.employwise.dao;

import in.astro.employwise.model.EmployeeEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

public interface EmployeeRepository extends CouchbaseRepository<EmployeeEntity, String> {
    // Custom queries can be added here if needed
}
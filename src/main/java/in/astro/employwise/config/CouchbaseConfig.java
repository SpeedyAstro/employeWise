package in.astro.employwise.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Override
    public String getConnectionString() {
        return "couchbase://localhost"; // Replace with your hostname
    }

    @Override
    public String getUserName() {
        return "yash"; // Replace with your username
    }

    @Override
    public String getPassword() {
        return "123123"; // Replace with your password
    }

    @Override
    public String getBucketName() {
        return "demo"; // Replace with your bucket name
    }
}



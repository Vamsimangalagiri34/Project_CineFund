package com.sravs;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class EmployeeRepositoryTest {
//
//	@Test
//	void contextLoads() {
//	}
//
//}
//package com.sravs.repository;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmployeeRepositoryTest {

    @Test
    public void testFindEmployeeById() throws Exception {
        // Load PostgreSQL JDBC driver (optional for newer versions)
        Class.forName("org.postgresql.Driver");

        // Connect to the database
        Connection conn = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/springboot_db", "employees_db", "Bnls6456@hcl");

        // Prepare SQL query
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employee");
        stmt.setInt(1, 1); // Example: fetch employee with ID = 1

        // Execute query
        ResultSet rs = stmt.executeQuery();

        // Process result
        while (rs.next()) {
            System.out.println("Name: " + rs.getString("name"));
            System.out.println("Department: " + rs.getString("department"));
            System.out.println("Salary: " + rs.getDouble("salary"));
        }

        // Close resources
        rs.close();
        stmt.close();
        conn.close();
    }
}


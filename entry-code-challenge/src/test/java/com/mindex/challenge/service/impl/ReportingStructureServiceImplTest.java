package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String reportingStructureIdUrl;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportingStructureIdUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testRead() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        Employee testEmployee1 = new Employee();
        testEmployee1.setFirstName("Tina");
        testEmployee1.setLastName("Huss");
        testEmployee1.setDepartment("Engineering");
        testEmployee1.setPosition("Manager");
        List<Employee> directReports = new ArrayList<>();
        directReports.add(testEmployee);
        testEmployee1.setDirectReports(directReports);

        Employee testEmployee2 = new Employee();
        testEmployee2.setFirstName("Mina");
        testEmployee2.setLastName("Russ");
        testEmployee2.setDepartment("Engineering");
        testEmployee2.setPosition("VP");
        List<Employee> directReports1 = new ArrayList<>();
        directReports1.add(testEmployee1);
        testEmployee2.setDirectReports(directReports1);

        employeeService.create(testEmployee);
        employeeService.create(testEmployee1);
        employeeService.create(testEmployee2);

        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(testEmployee2);
        reportingStructure.setNumberOfReports(2);

        ReportingStructure reportingStructure1 = new ReportingStructure();
        reportingStructure1.setEmployee(testEmployee1);
        reportingStructure1.setNumberOfReports(1);

        ReportingStructure reportingStructure2 = new ReportingStructure();
        reportingStructure2.setEmployee(testEmployee);
        reportingStructure2.setNumberOfReports(0);

        //Read check
        ReportingStructure readReportingStructure = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, testEmployee2.getEmployeeId()).getBody();
        assertEquals(reportingStructure.getNumberOfReports(), readReportingStructure.getNumberOfReports());
        assertReportingStructureEquivalence(reportingStructure, readReportingStructure);

        ReportingStructure readReportingStructure1 = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, testEmployee1.getEmployeeId()).getBody();
        assertEquals(reportingStructure1.getNumberOfReports(), readReportingStructure1.getNumberOfReports());
        assertReportingStructureEquivalence(reportingStructure1, readReportingStructure1);

        ReportingStructure readReportingStructure2 = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, testEmployee.getEmployeeId()).getBody();
        assertEquals(reportingStructure2.getNumberOfReports(), readReportingStructure2.getNumberOfReports());
        assertReportingStructureEquivalence(reportingStructure2, readReportingStructure2);
    }

    private static void assertReportingStructureEquivalence(ReportingStructure expected, ReportingStructure actual) {
        assertEquals(expected.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
        assertEquals(expected.getNumberOfReports(), actual.getNumberOfReports());
    }
}

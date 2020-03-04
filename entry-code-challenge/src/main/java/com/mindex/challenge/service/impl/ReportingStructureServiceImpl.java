package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    private Integer countNumberOfReports(Employee employee, Integer numberOfReports) {
        if (employee.getDirectReports() == null)
            return numberOfReports;
        for (Employee report: employee.getDirectReports()) {
            report = employeeRepository.findByEmployeeId(report.getEmployeeId());
            numberOfReports = countNumberOfReports(report, numberOfReports + 1);
        }
        return numberOfReports;
    }

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Creating reporting structure with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if  (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(countNumberOfReports(employee, 0));
        return reportingStructure;
    }
}

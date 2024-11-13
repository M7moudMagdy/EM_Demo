package com.em.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DepartmentVerificationService {

    @Value("#{'${valid.departments}'.split(',')}")
    private List<String> validDepartments;

    public boolean isDepartmentValid(String department) {
        log.info("validating Department: {}", department);

        return validDepartments.contains(department);
    }
}

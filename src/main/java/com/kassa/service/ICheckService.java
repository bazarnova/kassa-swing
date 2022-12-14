package com.kassa.service;

import com.kassa.entity.Check;

import java.time.LocalDate;
import java.util.List;

public interface ICheckService {
    List<Check> getAllChecks();
}

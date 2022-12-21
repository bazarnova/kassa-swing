package com.kassa.client;

import com.kassa.entity.Check;

import java.util.List;

public interface ICheckClient {

    Check addNewCheck(Check check);
    List<Check> getAllChecks();
}

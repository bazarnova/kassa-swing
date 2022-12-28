package com.kassa.client;

import com.kassa.entity.Check;

import java.util.List;

public interface ICheckClient {

    Check addCheck(Check check);
    List<Check> getAllChecks();
}

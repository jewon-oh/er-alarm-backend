package com.eralarm.eralarmbackend.base.rest_api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorDetail {
    private String field;
    private String message;
}

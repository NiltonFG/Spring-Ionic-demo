package com.example.demo.resouces.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {
    private List<FieldMessage> list = new ArrayList<>();

    public ValidationError(Integer status, String error, long timestamp, String message, String path) {
        super(status, error, timestamp, message, path);
    }

    public List<FieldMessage> getErrors() {
        return list;
    }

    public void addError(String fieldName, String message) {
        list.add(new FieldMessage(fieldName, message));
    }
}

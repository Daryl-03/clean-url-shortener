package dev.richryl.shortlink.adapaters.web.dto;

import java.util.List;

public record ValidationErrorResponse(
        int status,
        String code,
        String error,
        List<ValidationField> validationErrors
)
{
    public record ValidationField(String field, String message){
    }
}

package com.osp.bttp.common.exception;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.utils.UtilsHttp;
import com.osp.bttp.dao.model.dto.BaseErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class BaseExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private UtilsHttp utilsHttp;

    @ExceptionHandler(value = {Exception.class})
    public void commence(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
        utilsHttp.handleResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Result.SERVER_ERROR, response, exception);
    }

    @ExceptionHandler(BaseException.class)
    public void handle(HttpServletRequest request, HttpServletResponse response, BaseException exception) throws IOException {
            utilsHttp.handleResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Result.SERVER_ERROR, response, exception);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handle(HttpServletRequest request, HttpServletResponse response, ConstraintViolationException exception) throws IOException {
        utilsHttp.handleResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Result.SERVER_ERROR, response, exception);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public void commence(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        utilsHttp.handleResponse(HttpServletResponse.SC_FORBIDDEN, Result.FORBIDDEN, response, exception);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        utilsHttp.handleResponse(HttpServletResponse.SC_UNAUTHORIZED, Result.UNAUTHORIZED, response, exception);
    }

    @ExceptionHandler({
            InternalException.class
    })
    public ResponseEntity<Object> handleApplicationException(InternalException ex) {
        log.error("", ex);
        BaseErrorDto error = BaseErrorDto.builder()
                .timestamp(new Date())
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getMessage())
                .fields(ex.getFields() != null && !ex.getFields().isEmpty() ? String.join(", ", ex.getFields()) : null)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return new ResponseEntity<>(processFieldErrors(fieldErrors), HttpStatus.BAD_REQUEST);
    }

    private ApiResponseV1 processFieldErrors(List<FieldError> fieldErrors) {
        //update by sangnk: 11/09/2023. Tác dụng: Handle @Valid
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError: fieldErrors) {
            if (errors.containsKey(fieldError.getField())) {
                errors.put(fieldError.getField(), errors.get(fieldError.getField()) + ", " + fieldError.getDefaultMessage());
            } else {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }
        return new ApiResponseV1(false,10, "Invalid param", errors);
    }

}


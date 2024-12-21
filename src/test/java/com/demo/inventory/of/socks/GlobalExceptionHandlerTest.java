package com.demo.inventory.of.socks;

import com.demo.inventory.of.socks.exception.handler.ErrorResponse;
import com.demo.inventory.of.socks.exception.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleIOException() {
        IOException ioException = new IOException("Test IO exception");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleIOException(ioException);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("Произошла ошибка при обработке файла");
    }

    @Test
    void testHandleDataIntegrityViolationException() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Test data integrity violation");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleDataAccessException(exception);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("Некорректный формат данных");
    }

    @Test
    void testHandleMissingServletRequestParameterException() {
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException("param", "String");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleDataAccessException(exception);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("Некорректный формат данных");
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Test illegal argument");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("Test illegal argument");
    }

    @Test
    void testHandleGlobalException() {
        Exception exception = new Exception("Test global exception");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleGlobalException(exception);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("Произошла ошибка на сервере");
    }
}


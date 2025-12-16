package edu.dosw.rideci.unit.infrastructure.exceptions;

import edu.dosw.rideci.infrastructure.exceptions.ErrorResponse;
import edu.dosw.rideci.infrastructure.exceptions.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private WebRequest webRequest;

    @Mock
    private ServletWebRequest servletWebRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleResponseStatusException_ShouldReturnCorrectResponse() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResponseStatusException(ex, httpServletRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @Test
    void handleResponseStatusException_ShouldHandleNullReason() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.BAD_REQUEST);
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResponseStatusException(ex, httpServletRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleResponseStatusException_ShouldHandleNullRequest() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResponseStatusException(ex, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("", response.getBody().getPath());
    }

    // Nota: Los métodos handleMethodArgumentNotValid y handleHttpMessageNotReadable
    // son protected y heredados de ResponseEntityExceptionHandler.
    // Se prueban indirectamente a través de las pruebas de integración o
    // usando reflection si es necesario. Por ahora, nos enfocamos en los métodos públicos.

    @Test
    void handleConstraintViolation_ShouldReturnBadRequest() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getPropertyPath()).thenReturn(mock(jakarta.validation.Path.class));
        when(violation.getMessage()).thenReturn("Invalid value");

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);

        ConstraintViolationException ex = new ConstraintViolationException("Constraint violation", violations);
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleConstraintViolation(ex, httpServletRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Constraint violations", response.getBody().getMessage());
        assertFalse(response.getBody().getDetails().isEmpty());
    }

    @Test
    void handleNotFound_ShouldReturnNotFound() {
        NoSuchElementException ex = new NoSuchElementException("Resource not found");
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleNotFound(ex, httpServletRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("Resource not found", response.getBody().getMessage());
    }

    @Test
    void handleNotFound_ShouldHandleNullMessage() {
        NoSuchElementException ex = new NoSuchElementException();
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleNotFound(ex, httpServletRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Resource not found", response.getBody().getMessage());
    }

    @Test
    void handleAccessDenied_ShouldReturnForbidden() {
        AccessDeniedException ex = new AccessDeniedException("Access denied");
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAccessDenied(ex, httpServletRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getBody().getStatus());
        assertEquals("Access denied", response.getBody().getMessage());
        assertFalse(response.getBody().getDetails().isEmpty());
    }

    @Test
    void handleAccessDenied_ShouldHandleNullRequest() {
        AccessDeniedException ex = new AccessDeniedException("Access denied");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAccessDenied(ex, null);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("", response.getBody().getPath());
    }

    @Test
    void handleUnhandled_ShouldReturnInternalServerError() {
        Exception ex = new RuntimeException("Unexpected error");
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleUnhandled(ex, httpServletRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals("Internal server error", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @Test
    void handleUnhandled_ShouldHandleNullMessage() {
        Exception ex = new RuntimeException();
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleUnhandled(ex, httpServletRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getDetails().isEmpty());
    }

    @Test
    void handleUnhandled_ShouldHandleNullRequest() {
        Exception ex = new RuntimeException("Unexpected error");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleUnhandled(ex, null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("", response.getBody().getPath());
    }

    @Test
    void handleResponseStatusException_ShouldHandleDifferentStatusCodes() {
        ResponseStatusException ex404 = new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        ResponseStatusException ex400 = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        ResponseStatusException ex500 = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");

        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ErrorResponse> response404 = exceptionHandler.handleResponseStatusException(ex404, httpServletRequest);
        ResponseEntity<ErrorResponse> response400 = exceptionHandler.handleResponseStatusException(ex400, httpServletRequest);
        ResponseEntity<ErrorResponse> response500 = exceptionHandler.handleResponseStatusException(ex500, httpServletRequest);

        assertEquals(HttpStatus.NOT_FOUND, response404.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response400.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response500.getStatusCode());
    }

    @Test
    void handleConstraintViolation_ShouldHandleMultipleViolations() {
        ConstraintViolation<?> violation1 = mock(ConstraintViolation.class);
        ConstraintViolation<?> violation2 = mock(ConstraintViolation.class);
        when(violation1.getPropertyPath()).thenReturn(mock(jakarta.validation.Path.class));
        when(violation1.getMessage()).thenReturn("Invalid value 1");
        when(violation2.getPropertyPath()).thenReturn(mock(jakarta.validation.Path.class));
        when(violation2.getMessage()).thenReturn("Invalid value 2");

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation1);
        violations.add(violation2);

        ConstraintViolationException ex = new ConstraintViolationException("Multiple violations", violations);
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleConstraintViolation(ex, httpServletRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getDetails().size());
    }

    @Test
    void handleResponseStatusException_ShouldUseReasonWhenAvailable() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Custom reason");
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResponseStatusException(ex, httpServletRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Custom reason", response.getBody().getMessage());
    }

    @Test
    void handleResponseStatusException_ShouldUseMessageWhenReasonIsNull() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.BAD_REQUEST);
        when(httpServletRequest.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResponseStatusException(ex, httpServletRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
    }
}


package zerobase.weather.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import zerobase.weather.WeatherApplication;
import zerobase.weather.dto.ErrorResponse;

import static zerobase.weather.type.ErrorCode.INTERNAL_SERVER_ERROR;
import static zerobase.weather.type.ErrorCode.INVALID_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleAllException(Exception e) {
        logger.error("Exception is occurred.", e);

        return new ErrorResponse(INTERNAL_SERVER_ERROR,
                INTERNAL_SERVER_ERROR.getDescription());
    }

    // 날짜 형식과 다른 입력시 예외 발생
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponse handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        logger.error("MethodArgumentTypeMismatchException is occurred", e);
        return new ErrorResponse(INVALID_REQUEST,
                INVALID_REQUEST.getDescription());
    }
}

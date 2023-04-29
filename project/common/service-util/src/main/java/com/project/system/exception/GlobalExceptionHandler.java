package com.project.system.exception;

import com.project.common.result.Result;
import com.project.common.result.ResultCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author Lingjun
 * @date 2023/4/7 00:39
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    //1.Global Exceptions
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        System.out.println("Global Exceptions");
        e.printStackTrace();
        return Result.fail().message("Global exception handling is implemented");
    }

    //2.Specific Exception
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        System.out.println("Specific Exceptions");
        e.printStackTrace();
        return Result.fail().message("Specific exception handling is implemented");
    }

    //3.Custom Exception
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result error(CustomException e){
        System.out.println("Custom Exceptions");
        e.printStackTrace();
        return Result.fail().code(e.getCode()).message(e.getMessage());
    }

    /**
     * spring security
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error(AccessDeniedException e) throws AccessDeniedException {
        return Result.fail().code(ResultCodeEnum.PERMISSION.getCode()).message("No operation permission for the current function");
    }
}

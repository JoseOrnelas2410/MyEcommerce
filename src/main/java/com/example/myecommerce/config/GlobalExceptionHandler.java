package com.example.myecommerce.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {

    /*
    Por carga de recursos como imagenes algunos endpoint se llaman
    en mas de una ocasion por lo tanto generan un error silencioso
    el cual puede generar mensajes de error "falsos" se genera este
    Handler para evitar mensajes de falso positivo en error
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handlerMissingParams (MissingServletRequestParameterException e) {
        return "redirect:/";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, RedirectAttributes redirectAttributes, HttpServletRequest request)
    {
        String referer= request.getHeader("Referer");
        if (!(referer == null)) {
            String cleanRedirect = referer.split("\\?")[0];
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            return "redirect:" + cleanRedirect;
        }
        return "redirect:/";
    }
}

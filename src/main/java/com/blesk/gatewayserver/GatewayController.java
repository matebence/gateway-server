package com.blesk.gatewayserver;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@RestController
@RequestMapping(value = "/", produces = "application/json")
public class GatewayController implements ErrorController {

    @RequestMapping("/error")
    public GatewayMessageResponse handleError(HttpServletRequest request) {
        Object object = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (object != null) {
            Integer statusCode = Integer.valueOf(object.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return new GatewayMessageResponse(new Timestamp(System.currentTimeMillis()).toString(), "Je nám ľúto, ale požadovaná stránka nebola nájdená", true);
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return new GatewayMessageResponse(new Timestamp(System.currentTimeMillis()).toString(), "Ľutujeme, ale nastala chyba", true);
            }
        }
        return new GatewayMessageResponse(new Timestamp(System.currentTimeMillis()).toString(), "Nastala neočakávaná chyba", true);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
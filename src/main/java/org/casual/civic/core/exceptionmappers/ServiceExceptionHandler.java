package org.casual.civic.core.exceptionmappers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.casual.civic.core.exception.AbstractResourceNotFoundException;
import org.casual.civic.core.exception.PlatformException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(Throwable.class)
    @ResponseBody
    ResponseEntity<Object> handleControllerException(HttpServletRequest req, Throwable ex) {
		Map<String,String> errorResponse = new HashMap<>();
        if(ex instanceof AbstractResourceNotFoundException) {
        	populateErrorResponseMessage(errorResponse,(PlatformException) ex);
            return new ResponseEntity<Object>(errorResponse, HttpStatus.NOT_FOUND);
        } else if(ex instanceof DataAccessException) {
        	return new ResponseEntity<Object>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
        } else {
            return new ResponseEntity<Object>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String,String> responseBody = new HashMap<>();
        responseBody.put("path",request.getContextPath());
        responseBody.put("message","The URL you have reached is not in service at this time (404).");
        return new ResponseEntity<Object>(responseBody,HttpStatus.NOT_FOUND);
    }
    
    private void populateErrorResponseMessage(Map<String,String> errorResponse,
    		final PlatformException exception) {
    	errorResponse.put("globalisationMessageCode", exception.getGlobalisationMessageCode());
    	errorResponse.put("defaultUserMessage", exception.getDefaultUserMessage());
    	for(int i=0; i < exception.getDefaultUserMessageArgs().length; i++)
    		errorResponse.put("defaultUserMessageArgs" + i,
    				exception.getDefaultUserMessageArgs()[i].toString());
    }

}

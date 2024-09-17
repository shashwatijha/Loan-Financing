package com.oracle.financeproject.exceptionhandler;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.oracle.financeproject.exception.ApplicationException;

public class ExceptionHandler implements ExceptionMapper<ApplicationException> {
	
	public Response toResponse(ApplicationException e) {
		String jsonText="{ \"msg\":\" "+e.getMessage()+"\"}";
		return Response.status(404).entity(jsonText).build();
	}
}

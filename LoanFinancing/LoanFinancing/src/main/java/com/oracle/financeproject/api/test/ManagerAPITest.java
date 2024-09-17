package com.oracle.financeproject.api.test;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import com.oracle.financeproject.api.ManagerAPI;
import com.oracle.financeproject.exceptionhandler.ExceptionHandler;

public class ManagerAPITest extends JerseyTest {
	@Override
	protected Application configure() {
		return new ResourceConfig(ManagerAPI.class,ExceptionHandler.class);
	}
}

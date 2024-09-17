package com.oracle.financeproject.api;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.oracle.financeproject.entity.Clerk;
import com.oracle.financeproject.entity.LoanApplication;
import com.oracle.financeproject.entity.Manager;
import com.oracle.financeproject.exception.ApplicationException;
import com.oracle.financeproject.repository.iml.ClerkDAO_IMPL;
import com.oracle.financeproject.repository.iml.ManagerDAO_IMPL;
import com.oracle.financeproject.repository.iml.SmtpMail;

@Path("/Manager")
public class ManagerAPI {
	@RolesAllowed("MANAGER")
	@GET
	@Path("/{managerId}/getDetails")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getClerkInfo(@PathParam("managerId") int managerId) {
		ManagerDAO_IMPL dao=new ManagerDAO_IMPL();	
		Manager m=dao.getManagerDetails(managerId);
		if(m==null)
			throw new ApplicationException("No data found");
		return Response.status(200).entity(m).header("contentType", "application/json").build();

	}

	@RolesAllowed("MANAGER")
	@GET
	@Path("/view/allLoanApplications")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response viewApplication()
	{
		ManagerDAO_IMPL dao=new ManagerDAO_IMPL();	
		List<LoanApplication> l=dao.viewAllLoanApplications();
		if(l.isEmpty())
			throw new ApplicationException("No data found");
		return Response.status(200).entity(l).header("contentType", "application/json").build();
	}
	
	//approve loan application
	@RolesAllowed("MANAGER")
	@GET
	@Path("/view/allLoanApplications/{loanApplicationNumber}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response ApproveLoanApplication(@PathParam("loanApplicationNumber")int loanApplicationNumber, @QueryParam("approvalResponse") String approvalResponse)
	{
		ManagerDAO_IMPL dao=new ManagerDAO_IMPL();	
		Status resp =dao.ApproveLoanApplication( loanApplicationNumber , approvalResponse);
		SmtpMail email=new SmtpMail();
		System.out.println("email 1");
		email.sendMail(loanApplicationNumber);
		System.out.println("email2");
		return Response.status(resp).build();
	}
	
	@RolesAllowed("MANAGER")
	@POST
	@Path("/sendMail/{ApplicationNo}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON) 
	public Response approveLoanApplication(@PathParam("ApplicationNo")int applicationNo)
	{
		SmtpMail s= new SmtpMail();
		Response resp= s.sendMail(applicationNo);
		return resp;
		
	}
	
	
	
	
}

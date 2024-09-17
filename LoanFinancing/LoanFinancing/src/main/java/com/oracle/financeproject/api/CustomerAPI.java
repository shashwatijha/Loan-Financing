package com.oracle.financeproject.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.oracle.financeproject.entity.Customer;
import com.oracle.financeproject.entity.LoanApplication;
import com.oracle.financeproject.entity.Payment;
import com.oracle.financeproject.entity.SanctionLoan;
import com.oracle.financeproject.exception.ApplicationException;
import com.oracle.financeproject.repository.iml.ClerkDAO_IMPL;
import com.oracle.financeproject.repository.iml.CustomerDAO_IMPL;

@Path("/customer")
public class CustomerAPI {
	

	@PermitAll
	@POST
	@Path("/register")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	public int addCustomer(Customer c) {
		CustomerDAO_IMPL dao=new CustomerDAO_IMPL();
		int res=dao.setCustomerDetails(c);
		return res;
	}
	
	@PermitAll
	@GET
	@Path("/calculateEmi")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	public double CalculateCustEmi(@QueryParam("principle") double principle, @QueryParam("roi") double roi, @QueryParam("tenure") int tenure)
	{
		CustomerDAO_IMPL dao=new CustomerDAO_IMPL();	
		double emi= dao.calculateEmi(principle, roi, tenure);
		System.out.println("hello");
		return emi;
		
	}
	

	@RolesAllowed("CUSTOMER")
	@GET
	@Path("/{customerId}/details")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getCustByCustId(@PathParam("customerId") int customerId) {
		CustomerDAO_IMPL dao=new CustomerDAO_IMPL();	
		 Customer c=dao.getCustomerDetailsByCustomerId(customerId);
		if(c.getCustomerFirstName()!="0") {
			return Response.status(200).entity(c).header("contentType", "application/json").build();
		}
		else {
			//return Response.status(500);
			throw new ApplicationException("The customer "+customerId+" is not found");
		}
	}
	
	@RolesAllowed("CUSTOMER")
	@GET
	@Path("/{customerId}/viewApplication")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response viewApplicationbyCustId(@PathParam("customerId") int customerId) {
		CustomerDAO_IMPL dao=new CustomerDAO_IMPL();	
		List<LoanApplication> l=dao.viewApplicationBycustId(customerId);
		if(l.isEmpty())
			throw new ApplicationException("No data found");
		return Response.status(200).entity(l).header("contentType", "application/json").build();
		//return l;
	}
	
	@RolesAllowed("CUSTOMER")
	@GET
	@Path("/{customerId}/viewLoan")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response viewLoanbyCustId(@PathParam("customerId") int customerId) {
		CustomerDAO_IMPL dao=new CustomerDAO_IMPL();	
		List<SanctionLoan> l=dao.viewLoanBycustId(customerId);
		if(l.isEmpty())
			throw new ApplicationException("No data found");
		return Response.status(200).entity(l).header("contentType", "application/json").build();
		//return l;
	}
	
	@RolesAllowed("CUSTOMER")
	@GET
	@Path("/{customerId}/viewApplication/{loanApplicationNo}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public LoanApplication viewApplication(@PathParam("loanApplicationNo") int loanApplicationNo ) {
		CustomerDAO_IMPL dao=new CustomerDAO_IMPL();	
		LoanApplication l=dao.viewLoanApplication(loanApplicationNo);
		return l;
	}
	
	@RolesAllowed("CUSTOMER")
	@POST
	@Path("/{customerId}/loanApplication")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addLoanApplication(LoanApplication l) {
		CustomerDAO_IMPL dao=new CustomerDAO_IMPL();
		int rowNum=dao.applyForLoan(l);
		if(rowNum>0)
		   return Response.status(200).entity("Added successfully").header("contentType", "application/json").build();
		else
			return Response.status(404).entity("Application not added").header("contentType", "application/json").build();
	}
	
	@RolesAllowed("CUSTOMER")
	@DELETE
	@Path("/{custId}/deleteLoanApplication/{loanApplicationNo}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteLoanApplication(@PathParam("loanApplicationNo") int loanAppNo,@PathParam("custId")int custId) {
		System.out.println("test loan application dletion");
		CustomerDAO_IMPL dao=new CustomerDAO_IMPL();
		Status s=dao.customerdeleteApplication(custId, loanAppNo);
		return Response.status(s).build();
	}
	
	@RolesAllowed("CUSTOMER")
	@POST
	@Path("/{custId}/repayment")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response RepayLoan(@PathParam("loanAccNo") int loanAccNo,Payment p) {
		System.out.println("test loan repayment");
		CustomerDAO_IMPL dao=new CustomerDAO_IMPL();
		p.setDateOfTransaction(LocalDate.now());
		int status=dao.loanRepayment(p);
		if(status==0)
			throw new ApplicationException("Not found");
		return Response.status(200).build();
	}
	
	
	

}

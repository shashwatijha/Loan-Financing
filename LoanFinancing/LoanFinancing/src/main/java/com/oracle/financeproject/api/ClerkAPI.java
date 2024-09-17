package com.oracle.financeproject.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.oracle.financeproject.entity.Clerk;
import com.oracle.financeproject.entity.Customer;
import com.oracle.financeproject.entity.LoanApplication;
import com.oracle.financeproject.exception.ApplicationException;
import com.oracle.financeproject.repository.iml.ClerkDAO_IMPL;


@Path("/clerk")
public class ClerkAPI {
	@RolesAllowed("CLERK")
	@GET
	@Path("/{clerkId}/getDetails")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getClerkInfo(@PathParam("clerkId") int clerkId) {
		ClerkDAO_IMPL dao=new ClerkDAO_IMPL();
		Clerk c=dao.getClerkDetails(clerkId);
		if(c==null)
			throw new ApplicationException("No data found");
		return Response.status(200).entity(c).header("contentType", "application/json").build();

	}
	
	
	@RolesAllowed("CLERK")
	@GET
	@Path("/{clerkId}/searchByLoanType/{loanType}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response clerkSearchByLoanType(@PathParam("clerkId") int clerkId,@PathParam("loanType") String loanType ) {
		ClerkDAO_IMPL dao=new ClerkDAO_IMPL();
		List<LoanApplication> l=dao.searchByLoanType(clerkId,loanType);
		if(l.isEmpty())
			throw new ApplicationException("No data found");
		return Response.status(200).entity(l).header("contentType", "application/json").build();
		//return l;
	}
	
	@RolesAllowed("CLERK")
	@GET
	@Path("/{clerkId}/searchByLoanId/{loanId}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response clerkSearchByLoanId2(@PathParam("clerkId") int clerkId,@PathParam("loanId") int loanId) {
		ClerkDAO_IMPL dao=new ClerkDAO_IMPL();
		List<LoanApplication> l=dao.searchByLoanId(clerkId, loanId);
		if(l.isEmpty())
			throw new ApplicationException("No data found");
		return Response.status(200).entity(l).header("contentType", "application/json").build();
		//return alist;
	}
	
	@PermitAll
	@POST
	@Path("/newCustomer")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response clerkAddNewCustomer(Customer c) {
		ClerkDAO_IMPL dao=new ClerkDAO_IMPL();
		int id=dao.addNewCustomer(c);
		return Response.status(200).entity(id).header("contentType", "application/json").build();

	}
	
	@RolesAllowed("CLERK")
	@POST
	@Path("/{clerkId}/loanApplication")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addLoanApplicationClerk(@PathParam("clerkId") int clerkId,LoanApplication l) {
		ClerkDAO_IMPL dao=new ClerkDAO_IMPL();
		int rowNum=dao.applyForLoan(clerkId,l);
		if(rowNum>0)
		 return Response.status(200).entity("Added successfully").header("contentType", "application/json").build();
		else
			 return Response.status(200).entity("Application not added").header("contentType", "application/json").build();

	}


	@RolesAllowed("CLERK")
	@GET
	@Path("/{clerkId}/searchByDate/{applicationDate}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response clerkSearchByDate(@PathParam("clerkId") int clerkId,@PathParam("applicationDate") String applicationDate ) {
		ClerkDAO_IMPL dao=new ClerkDAO_IMPL();
		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate applicationLocalDate=LocalDate.parse(applicationDate,formatter);
		
		List<LoanApplication> l=dao.searchByDate(clerkId,applicationLocalDate);
		if(l.isEmpty())
			throw new ApplicationException("No data found");
		return Response.status(200).entity(l).header("contentType", "application/json").build();
		//return l;
	}
}

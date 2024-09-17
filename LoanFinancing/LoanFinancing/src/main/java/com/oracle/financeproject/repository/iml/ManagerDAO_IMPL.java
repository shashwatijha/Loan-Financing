package com.oracle.financeproject.repository.iml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.oracle.financeproject.entity.Manager;
import com.oracle.financeproject.entity.Clerk;
import com.oracle.financeproject.entity.LoanApplication;
import com.oracle.financeproject.entity.SanctionLoan;
import com.oracle.financeproject.exception.ApplicationException;
import com.oracle.financeproject.jdbc.DBConnection;
import com.oracle.financeproject.repository.ManagerDAO;

public class ManagerDAO_IMPL implements ManagerDAO {

	@Override
	public List<LoanApplication> viewAllLoanApplications() {
		// TODO Auto-generated method stub
		Connection con=DBConnection.getConnect();
		String sql="select * from loanapplication";
		List<LoanApplication> l = new ArrayList<LoanApplication>();
		System.out.println(sql);
		try {
			PreparedStatement pstmt=con.prepareStatement(sql);
			System.out.println("hello");
			ResultSet resultSet=pstmt.executeQuery();
			int loanApplicationNo=0,loanId=0,custId=0, clerkId=0, loanAmt=0, loanTenure=0;
			String approveStatus=null;
			LocalDate appDate=null;
			
			
			while(resultSet.next()) {
				loanApplicationNo=resultSet.getInt("loanApplicationNo");
				loanId=resultSet.getInt("loanId");
				custId=resultSet.getInt("customerId");
				clerkId=resultSet.getInt("clerkId");
				loanAmt=resultSet.getInt("loanAmount");
				loanTenure=resultSet.getInt("loanTenureInMonths");
				appDate = (resultSet.getDate("applicationDate")).toLocalDate();
				approveStatus=resultSet.getString("loanApplicationStatus");
				LoanApplication loan = new LoanApplication(loanApplicationNo,loanId,custId,clerkId,loanAmt,loanTenure,appDate,approveStatus);
				l.add(loan);
				System.out.println("added");
				
			}
			
			return l;
			}
		
		catch(SQLException e) {
			e.printStackTrace();
			return l;
			
		}		
	}


	@Override
	public List<LoanApplication> viewALlPendingLoanApplication() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status ApproveLoanApplication(int applicationId, String approvalResponse) {
		// TODO Auto-generated method stub
		Connection con=DBConnection.getConnect();
		String sql="update loanapplication set loanApplicationStatus=? where loanApplicationNo=? AND loanApplicationStatus='Pending'";
		System.out.println(approvalResponse);
		try {
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setString(1, approvalResponse);
			pstmt.setInt(2,applicationId);
			//pstmt.setString(3,"Pending");
			//System.out.println("hello");
			int check =pstmt.executeUpdate();
//			pstmt=con.prepareStatement("commit");
//			pstmt.executeUpdate();
			System.out.println(check+" no of rows affected");
			if(check==0)
			{
				return Response.Status.NOT_IMPLEMENTED;
			}
			
			if(approvalResponse.equals("Approved"))
			{
				sanctionLoan(applicationId,1234567);
				System.out.println("Loan Sanctioned");
				return Response.Status.OK;
			} 
			if(approvalResponse.equals("Rejected"))
			{
				sanctionLoan(applicationId,1234567);
				System.out.println("Loan Sanctioned");
				return Response.Status.OK;
			} 
		}
		
		catch (SQLException e) {
			e.printStackTrace();
			return Response.Status.NOT_IMPLEMENTED;
		}
		return null;

		//System.out.println(sql);
	}

	@Override
	public String deleteLoanApplication() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateChecklist() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int SendEmailUpdatingStatus(LoanApplication l) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int sanctionLoan(int applicationId, int custBankacc) {
		
Connection con=DBConnection.getConnect();
		
		String sql="select * from loanapplication natural join loan where loanapplicationno=?";
		LoanApplication l = new LoanApplication();
		System.out.println(sql);
		SanctionLoan s= new SanctionLoan();
		String sql2="insert into sanctionedLoans\r\n"
				+ "(loanAccountNo, loanApplicationNo,customerId,loanAmount,custAccountNo,monthlyEMI, loanPrinciple, loanInterest, loanRepayable) \r\n"
				+ "values (loanAccountSeq.nextval,?,?,?,?,?,?,?,?)";
		String sql3="insert into balancePayment (loanAccountNo,loanId,PrincipleBalance,InterestBalance,TotalBalance) select loanAccountNo,?,?,?,? from sanctionedloans where loanapplicationno=? ";

		System.out.println(sql2);
		try {
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, applicationId);
			System.out.println("hello");
			ResultSet resultSet=pstmt.executeQuery();
			int loanApplicationNo=0,loanId=0,custId=0, clerkId=0, loanAmt=0, loanTenure=0;
			String approveStatus=null;
			double roi=0;
			LocalDate appDate=null;
			
			while(resultSet.next()) {
				loanApplicationNo=resultSet.getInt("loanApplicationNo");
				loanId=resultSet.getInt("loanId");
				custId=resultSet.getInt("customerId");
				loanAmt=resultSet.getInt("loanAmount");
				loanTenure=resultSet.getInt("loanTenureInMonths");
				roi=resultSet.getDouble("loanInterestRate");
				System.out.println("sanction added");
			}
			
			double r=(roi/12)/100;
			System.out.println(r);
			double emi = (loanAmt * r * Math.pow(1 + r,loanTenure))/(Math.pow(1 + r,loanTenure) - 1); 
			System.out.println(emi);
			
			double loanIntrest= (emi*loanTenure)- loanAmt;
			double repayable = loanAmt+loanIntrest;
			
			pstmt=con.prepareStatement(sql2);
			//pstmt.setInt(1, 1);
			pstmt.setInt(1, loanApplicationNo);
			pstmt.setInt(2, custId);
			pstmt.setInt(3, loanAmt);
			pstmt.setInt(4, custBankacc);
			pstmt.setDouble(5, emi);
			pstmt.setDouble(6, loanAmt);
			pstmt.setDouble(7, loanIntrest);
			pstmt.setDouble(8, repayable);
			System.out.println("test 1");
			
			int check=pstmt.executeUpdate();
			System.out.println("updated ....");
			
						
			pstmt=con.prepareStatement(sql3);
			pstmt.setInt(1, loanId);
			pstmt.setDouble(2, loanAmt);
			pstmt.setDouble(3, loanIntrest);
			pstmt.setDouble(4, repayable);
			pstmt.setInt(5,applicationId);
		
			check=pstmt.executeUpdate();
			System.out.println(check+" no of rows affected");
			System.out.println("updated ....");
			
			
			
            return check;
			
		}
		catch(SQLException e){
			e.printStackTrace();
			return -1;
			
		}
		
		
	}


	@Override
	public boolean managerAuth(String uname, String password) {
		 Connection con=DBConnection.getConnect();
		 String sql="select managerId,managerPassword from manager WHERE managerId=? AND managerPassword=?";
		  try {
	        	 PreparedStatement pstmt=con.prepareStatement(sql);
	        	 pstmt.setInt(1,Integer.valueOf(uname));
	        	 pstmt.setString(2,password);
	 	         ResultSet resultSet=pstmt.executeQuery();
	 	         if(resultSet.next()) {
//	 	        	int managerId=resultSet.getInt("managerId");
//	 	        	String managerPassword=resultSet.getString("managerPassword");
	 	        	return true;
	 	         }
	        }catch(SQLException e) {
	        	e.printStackTrace();
	        	throw new ApplicationException("Invalid login");
	        }
		return false;
	}


	public Manager getManagerDetails(int managerId) {
		Connection con=DBConnection.getConnect();
		String sql="select * from manager";
		 try {
        	 PreparedStatement pstmt=con.prepareStatement(sql);
 	         ResultSet resultSet=pstmt.executeQuery();
 	         while(resultSet.next()) {
 	        	int manager_Id=resultSet.getInt("managerId");
 	        	String managerFName=resultSet.getString("managerFirstName");
 	        	String managerLName=resultSet.getString("managerLastName");
 	        	String managerPassword=resultSet.getString("managerPassword");
 	        	String branch=resultSet.getString("branch");
 	        	
 	        	return new Manager(manager_Id,managerFName,managerLName,managerPassword,branch);
 	         }
        }catch(SQLException e) {
        	e.printStackTrace();
        }
		return null;
	}

	
}

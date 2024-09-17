package com.oracle.financeproject.repository.iml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response;


import com.oracle.financeproject.entity.Customer;
import com.oracle.financeproject.entity.LoanApplication;
import com.oracle.financeproject.entity.Payment;
import com.oracle.financeproject.entity.SanctionLoan;
import com.oracle.financeproject.exception.ApplicationException;
import com.oracle.financeproject.jdbc.DBConnection;
import com.oracle.financeproject.repository.CustomerDAO;

public class CustomerDAO_IMPL implements CustomerDAO {
	public int setCustomerDetails(Customer c1) {
		Connection con=DBConnection.getConnect();
		String sql="insert into customer values(customerseq.nextval,?,?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt=con.prepareStatement(sql);
			//pstmt.setInt(1, c1.getCustomerId());
			pstmt.setString(1, c1.getCustomerFirstName());
			pstmt.setString(2, c1.getCustomerLastName());
			pstmt.setString(3, c1.getCustomerGender());
			pstmt.setString(4, c1.getCustomerMobile());
			pstmt.setString(5, c1.getCustomerAddress());
			pstmt.setString(6, c1.getCustomerPassword());
			pstmt.setString(7, c1.getCustomerEmail());
			int c=pstmt.executeUpdate();
			pstmt=con.prepareStatement("commit");
			pstmt.executeUpdate();
			
			sql="select customerId from customer where custFirstName=? AND custPassword=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, c1.getCustomerFirstName());
			pstmt.setString(2, c1.getCustomerPassword());
			ResultSet resultSet=pstmt.executeQuery();
			int custId=0;
			while(resultSet.next())
			{
				custId=resultSet.getInt("customerId");
			}
			return custId;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
		
	}
	
	
	public Customer getCustomerDetailsByCustomerId(int cust_Id) {
		Connection con=DBConnection.getConnect();
		Customer c=new Customer();
		String sql="select * from customer where customerId= ?";
		try {
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, cust_Id);
			ResultSet resultSet=pstmt.executeQuery();
			int custId=0;
			String custFname = "0",custLname ="0" ,custGender = "0",custMobile = "0",custEmail = "0",custAddress = "0",custPassword = "0";
			
			while(resultSet.next()) {
			custId=resultSet.getInt("customerId");
			custFname=resultSet.getString("custFirstName");
		    custLname=resultSet.getString("custLastName");
			custGender=resultSet.getString("custGender");
		    custMobile=resultSet.getString("custMobile");
		    custEmail=resultSet.getString("custEmail");
			custAddress=resultSet.getString("custAddress");
			custPassword=resultSet.getString("custPassword");
			}
			
			return new Customer(custId,custFname,custLname,custGender,custMobile,custEmail,custAddress,custPassword);
			 
			
		}catch(SQLException e) {
			e.printStackTrace();
			return c;
			
		}		
		
	}
	
	
	
	public void deleteCustomerByCustomerId(int customerId) {
		Connection con=DBConnection.getConnect();
		Customer c=new Customer();
		
		
		
	}



	@Override
	public int applyForLoan(LoanApplication l) {
		Connection con=DBConnection.getConnect();
		String sql="insert into loanapplication values (loanAppliseq.NEXTVAL,?,?,?,?,?,?,'Pending')";
		try {
			PreparedStatement pstmt=con.prepareStatement(sql);
			//pstmt.setInt(1, l.getLoanApplicationNo());
			pstmt.setInt(1, l.getLoanId());
			pstmt.setInt(2, l.getCustomerId());
			pstmt.setInt(3, 901);
			pstmt.setDouble(4, l.getLoanAmount());
			pstmt.setInt(5, l.getLoanTenureInMonths());
			pstmt.setDate(6, java.sql.Date.valueOf(java.time.LocalDate.now()));
			//pstmt.setString(7, "submitted");
			int r=pstmt.executeUpdate();
//			pstmt=con.prepareStatement("commit");
//			pstmt.executeUpdate();
			System.out.println(r+" no of rows affected");
			return r;
		}
		catch(SQLException e){
			e.printStackTrace();			
		}
		return -1;
	}

	

	@Override
	public LoanApplication viewLoanApplication(int LoanApplicationNo) {
		Connection con=DBConnection.getConnect();
		String sql="select * from loanapplication where loanApplicationNo= ?";
		try {
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, LoanApplicationNo);
			ResultSet resultSet=pstmt.executeQuery();
			
			int loanAppNo=0,loanId=0,custId=0,clerkId=0,loanTenure=0;
			LocalDate applicationDate = null;
			double loanamt=0;
			String status=null;
			
			while(resultSet.next()) {
			loanAppNo=resultSet.getInt("loanApplicationNo");
			loanId=resultSet.getInt("loanId");
		    custId=resultSet.getInt("customerId");
			clerkId=resultSet.getInt("clerkId");
		    loanamt=resultSet.getDouble("loanAmount");
		    loanTenure=resultSet.getInt("loanTenureInMonths");
			applicationDate=resultSet.getDate("applicationDate").toLocalDate();
			status=resultSet.getString("loanApplicationStatus");
			}
		
			return new LoanApplication(loanAppNo,loanId,custId,clerkId,loanamt,loanTenure,applicationDate,status);
			 
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}		
		return null;
	}


	@Override
	public List<LoanApplication> viewApplicationBycustId(int customerId) {
		Connection con=DBConnection.getConnect();
		String sql="select * from loanapplication where customerId= ?";
		try {
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			ResultSet resultSet=pstmt.executeQuery();
			
			int loanAppNo=0,loanId=0,custId=0,clerkId=0,loanTenure=0;
			LocalDate applicationDate = null;
			double loanamt=0;
			String status=null;
			
			List<LoanApplication> l=new ArrayList<LoanApplication>();
			
			while(resultSet.next()) {
			loanAppNo=resultSet.getInt("loanApplicationNo");
			loanId=resultSet.getInt("loanId");
		    custId=resultSet.getInt("customerId");
			clerkId=resultSet.getInt("clerkId");
		    loanamt=resultSet.getDouble("loanAmount");
		    loanTenure=resultSet.getInt("loanTenureInMonths");
			applicationDate=resultSet.getDate("applicationDate").toLocalDate();
			status=resultSet.getString("loanApplicationStatus");
			l.add(new LoanApplication(loanAppNo,loanId,custId,clerkId,loanamt,loanTenure,applicationDate,status)) ;
			}
		
			return l;
			 
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}		
		return null;
	}


	@Override
	public boolean customerAuth(String uname, String password) {
		 Connection con=DBConnection.getConnect();
		 String sql="select customerId,custPassword from customer WHERE customerId=? AND custPassword=?";
		  try {
	        	 PreparedStatement pstmt=con.prepareStatement(sql);
	        	 pstmt.setInt(1,Integer.valueOf(uname));
	        	 pstmt.setString(2,password);
	 	         ResultSet resultSet=pstmt.executeQuery();
	 	         if(resultSet.next()) {
//	 	        	int customerId=resultSet.getInt("customerId");
//	 	        	String custPassword=resultSet.getString("custPassword");
	 	        	return true;
	 	         }
	        }catch(SQLException e) {
	        	e.printStackTrace();
	        	throw new ApplicationException("Invalid Login");
	        }
		return false;
	}
	
	@Override
	public double calculateEmi(double principle, double roi, int tenure) {
		
		double r=(roi/12)/100;
		double emi = (principle * r * Math.pow(1 + r,tenure))/(Math.pow(1 + r,tenure) - 1); 
		return emi;
		
	}
	
	
	@Override
	public Status customerdeleteApplication(int custId, int applicationno) {
		// TODO Auto-generated method stub
		System.out.println(1);
		Connection con=DBConnection.getConnect();
		String sql="delete from loanapplication  where customerId=? AND loanapplicationno=? AND loanApplicationStatus='Pending'";
		
		try {
			
			
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, custId);
			pstmt.setInt(2,applicationno);
			//System.out.println("hello");
			int check =pstmt.executeUpdate();
			System.out.println(check+" no of rows affected");
			if(check==0)
			{
				throw new ApplicationException("No rows Found with specified details");
			}
			pstmt=con.prepareStatement("commit");
			pstmt.executeUpdate();
			return Response.Status.OK;
			} 
			
			
		
		catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException("Error");
		}
		
	}

	
	@Override
	public int loanRepayment(Payment p) {
		
		Connection con=DBConnection.getConnect();
		
		String sql= "select * from balancepayment natural join loan where loanaccountno=?";
				
		String sql2="insert into loanPayment\r\n"
				+ "(TransactionId,TransactionAmount,loanAccountNo, transactionMethod,dateOfTransaction,interestPaid, principlePaid) \r\n"
				+ "values (transactionseq.nextval,?,?,?,?,?,?)";
		
		String sql3="update balancepayment set\r\n"
				+ "  PrincipleBalance =?,\r\n"
				+ "  InterestBalance =?,\r\n"
				+ "  TotalBalance =? \r\n"
				+ "where loanAccountNo=?";
		
		System.out.println(sql);
		
		Payment pay=new Payment();
		
		System.out.println(sql2);
		
		System.out.println("id:"+p.getLoanAccountNo());
		
		
		try {
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, p.getLoanAccountNo());
			System.out.println("hello");
			ResultSet resultSet=pstmt.executeQuery();
			int loanAccountNo=0;
			String transactionMethod=null;
			double TransactionAmount=0;
			double loanInterestRate=0,principleBalance=0,interestBalance=0,totalBalance=0;
			
			while(resultSet.next()) {
				loanAccountNo = p.getLoanAccountNo();
			    loanInterestRate = resultSet.getDouble("loanInterestRate");
				principleBalance = resultSet.getDouble("principleBalance");
				interestBalance = resultSet.getDouble("interestBalance");
				totalBalance =resultSet.getDouble("totalBalance");
			}
			System.out.println("Balanceloan viewed");
			
			double r=(loanInterestRate/12)/100;
			System.out.println(r);
			double interestForMonth = r*principleBalance;
			double principleForMonth = p.getTransactionAmount()-interestForMonth;
			System.out.println(1);
			principleBalance -= principleForMonth;
			interestBalance-= interestForMonth;
			System.out.println(2);
			totalBalance-=p.getTransactionAmount();
			System.out.println(3);
			double amtPaid=p.getTransactionAmount();
			if(amtPaid>totalBalance) {
				throw new ApplicationException("Amount greater than balance");
			}
			pstmt=con.prepareStatement(sql2);
			System.out.println(4);
			pstmt.setDouble(1, p.getTransactionAmount());
			pstmt.setInt(2, p.getLoanAccountNo());
			pstmt.setString(3, p.getTransactionMethod());
			pstmt.setDate(4, java.sql.Date.valueOf(p.getDateOfTransaction()));
			System.out.println(5);
			pstmt.setDouble(5, interestForMonth);
			pstmt.setDouble(6, principleForMonth);
			System.out.println("test 1");
			
			int check=pstmt.executeUpdate();
			System.out.println("updated ....");
			System.out.println("loanpayment updated");
			System.out.println(check+" no of rows affected");
			
			
			pstmt=con.prepareStatement(sql3);
			pstmt.setDouble(1, principleBalance);
			pstmt.setDouble(2, interestBalance);
			pstmt.setDouble(3, totalBalance);
			pstmt.setDouble(4, p.getLoanAccountNo());
			System.out.println("test 1");
			check=pstmt.executeUpdate();
			
			System.out.println("7");
			
			System.out.println("updated ....");
			System.out.println("Balanceloan updated");
//			pstmt=con.prepareStatement("commit");
//			
//			pstmt.executeUpdate();
			
			
            return check;
			
		}
		catch(SQLException e){
			e.printStackTrace();
			return -1;
			
		}
				
	}

	@Override
	public List<SanctionLoan> viewLoanBycustId(int customerId) {
		Connection con=DBConnection.getConnect();
		String sql="select * from sanctionedLoans where customerId= ?";
		try {
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, customerId);
			ResultSet resultSet=pstmt.executeQuery();
			
			int loanAppNo=0,loanAccNo=0,custId=0,clerkId=0,loanTenure=0;
			LocalDate applicationDate = null;
			double loanamt=0;
			String status=null;
			
			List<SanctionLoan> l=new ArrayList<SanctionLoan>();
			
			while(resultSet.next()) {
			loanAccNo=resultSet.getInt("loanAccountNo");
			loanAppNo=resultSet.getInt("loanApplicationNo");
		    custId=resultSet.getInt("customerId");
		    loanamt=resultSet.getDouble("loanAmount");
		    int custBankAccNo=resultSet.getInt("custAccountNo");
		    double monthlyEMI = resultSet.getDouble("monthlyEMI");
		    double loanPrincipal = resultSet.getDouble("loanPrinciple");
		    double loanInterest = resultSet.getDouble("loanInterest");
		    double loanRepayable=resultSet.getDouble("loanRepayable");
			l.add(new SanctionLoan(loanAccNo,loanAppNo,custId,loanamt,custBankAccNo,monthlyEMI,loanPrincipal,loanInterest,loanRepayable)) ;
			}
		
			return l;
			 
			
		}catch(SQLException e) {
			e.printStackTrace();
			
		}		
		return null;
	}
	
	

//
//	@Override
//	public Customer login(int custId, String password) {
//		
//		Connection con=DBConnection.getConnect();
//		Customer c=new Customer();
//		String sql="Select * from customer where customerId=? AND custPassword=?";
//		try {
//			PreparedStatement pstmt=con.prepareStatement(sql);
//			pstmt.setInt(1, c1.getCustomerId());
//			pstmt.setString(2, c1.getCustomerFirstName());
//			int c=pstmt.executeUpdate();
//			pstmt=con.prepareStatement("commit");
//			pstmt.executeUpdate();
//			System.out.println(c+" no of rows affected");
//			return c;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return -1;
//		}
//		return null;
//	}

}

function register(){
    var fName=document.getElementById("fName").value;
    var lName=document.getElementById("lName").value;
    var gender=document.getElementById("gender").value;
    var address=document.getElementById("address").value;
    var email=document.getElementById("email").value;
    var mobile=document.getElementById("mobile").value;
    var password=document.getElementById("password").value;

    var newCust=new customer(fName,lName,gender,mobile,email,address,password);
    let url="http://localhost:8080/finance_project/onlineapi/customer/register";
     http=new XMLHttpRequest(); //used to send the req to server
     http.open("POST",url);
     http.setRequestHeader("Content-Type","application/json");
     http.setRequestHeader("Accept","application/json");
     // http.setRequestHeader("Authorization","Basic YWxiaW46MTIz");
     http.send(JSON.stringify(newCust));
     http.onreadystatechange=function (){
     if(http.readyState==4){
        let res=http.responseText;
       var id=JSON.parse(res);
       console.log(http.status);  
       alert("Successfully registered.. Your user id is"+res);
       //document.getElementById("userId").innerHTML="Your user Id is "+id;
       window.location.href="customerlogin.html";    
     }
    
    
     }


}

function customer(customerFirstName,customerLastName,customerGender,customerMobile,customerEmail,customerAddress,customerPassword)
{
    this.customerFirstName = customerFirstName;
    this.customerLastName = customerLastName;
    this.customerGender = customerGender;
    this.customerMobile = customerMobile;
    this.customerEmail = customerEmail;
    this.customerAddress = customerAddress;
    this.customerPassword = customerPassword;
       
}
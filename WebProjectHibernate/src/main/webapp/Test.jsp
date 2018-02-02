<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.springframework.context.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.sql.*" %>
<%@ page import="model.*" %>
<%@ page import="java.util.*" %>

<%
	ApplicationContext context = (ApplicationContext)application
			.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
// 	DataSource dataSource = (DataSource)context.getBean("dataSource");
	ProductDAO productDAO = (ProductDAO)context.getBean("productDAOHibernate");	
	List<ProductBean> beans1 = productDAO.select();
	out.println("<h3>beans1=" + beans1 + "</h3>");
	
	ProductService productService = (ProductService)context.getBean("productService");	
	List<ProductBean> beans2 = productService.select(beans1.get(0));
	out.println("<h3>beans2=" + beans2 + "</h3>");
	
	out.println("<hr />");
	
	CustomerDAO customerDAO = (CustomerDAO)context.getBean("customerDAOHibernate");
	CustomerBean bean1 = customerDAO.select("Alex");
	out.println("<h3>bean1=" + bean1 + "</h3>");
	
	CustomerService customerService = (CustomerService)context.getBean("customerService");
	CustomerBean bean2 = customerService.login("Alex", "AAA");
	out.println("<h3>bean2=" + bean2 + "</h3>");
	
	out.println("<hr />");
// 	Connection conn = dataSource.getConnection();	
// 	String queryString = "select * from dept";
// 	PreparedStatement stmt = conn.prepareStatement(queryString);
// 	ResultSet result = stmt.executeQuery();
// 	while(result.next()) {
// 		String id = result.getString(1);
// 		String name = result.getString(2);
// 		out.println("<h2>" +id + ":" + name + "</h2>");
// 	}
	
// 	result.close();
// 	stmt.close();
// 	conn.close();
%>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.List" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
		 <h1>OO Dashboard</h1>
    <h2>Status: <%= request.getAttribute("status") %></h2>
    <h2>Version: <%= request.getAttribute("version") %></h2>
    <h2>Content Packs:</h2>
    <ul>
        <% 
            List<String> contentPacks = (List<String>) request.getAttribute("contentPacks");
            for (String pack : contentPacks) {
                out.println("<li>" + pack + "</li>");
            }
        %>
    </ul>
    <h2>Flows:</h2>
    <ul>
        <% 
            List<String> flows = (List<String>) request.getAttribute("flows");
            for (String flow : flows) {
                out.println("<li>" + flow + "</li>");
            }
        %>
    </ul>
</body>
</html>
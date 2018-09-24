<%@page import="java.net.InetAddress" %>
<%@page import="java.util.Date" %>
<%@ page import="com.github.edwardsmatt.SessionData" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Hazelcast Session Test</title>
</head>
<body>
<span COLOR="#0000FF" style="font-size: large; ">
    Instance <%=InetAddress.getLocalHost()%> <br/><br/>
</span>

<hr/>

<span COLOR="#CC0000" style="font-size: large; ">
    <br/>
    Session Id : <%=request.getSession().getId()%> <br/>
    Is new session : <%=request.getSession().isNew()%><br/>
    Session Creation Date : <%=new Date(request.getSession().getCreationTime())%><br/>
    Session Access Date : <%=new Date(request.getSession().getLastAccessedTime())%><br/><br/>
    <%
        if (request.getSession().isNew()) {
            request.getSession().setAttribute("sessionData", new SessionData());
        }
    %>
    Has Session Data Set? <%=request.getSession().getAttribute("sessionData") != null%><br/><br/>
</span>
</body>
</html>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html>
<head>
<title>getalladmins.jsp</title>

<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="this is my page">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<!--<link rel="stylesheet" type="text/css" href="./styles.css">-->

</head>

<body>
	get all admins!
	
	<ul>
		<c:forEach var="o" items="${UsersList}">
			<li>${o.adminid} &nbsp; ${o.admin_name} &nbsp; ${o.admin_pwd} &nbsp; ${o.admin_realname}</li>
		</c:forEach>
	</ul>
	
</body>
</html>

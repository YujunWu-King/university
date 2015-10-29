<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html>
<head>
<title>getall.jsp</title>

<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="this is my page">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<!--<link rel="stylesheet" type="text/css" href="./styles.css">-->

</head>

<body>
	get all users!
	
	<ul>
		<c:forEach var="o" items="${UsersList}">
			<li>${o.id} &nbsp; ${o.nickname} &nbsp; ${o.state}</li>
		</c:forEach>
	</ul>
	
</body>
</html>

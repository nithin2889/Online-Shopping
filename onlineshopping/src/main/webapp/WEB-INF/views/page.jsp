<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="contextRoot" value="${pageContext.request.contextPath}" />

<spring:url var="css" value="/resources/css" />
<spring:url var="js" value="/resources/js" />
<spring:url var="images" value="/resources/images" />

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">

<title>Online Shopping - ${title}</title>

<script>
	window.menu = '${title}';
	window.contextRoot = '${contextRoot}';
</script>

<!-- Bootstrap Core CSS -->
<link href="${css}/bootstrap.min.css" rel="stylesheet">
<!-- Bootstrap Flatly Theme -->
<%-- <link href="${css}/bootstrap-flatly-theme.css" rel="stylesheet"> --%>

<!-- Data Table bootstrap -->
<link href="${css}/dataTables.bootstrap.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="${css}/myapp.css" rel="stylesheet">

</head>

<body>
	<div class="wrapper">
		<!-- Navigation -->
		<%@ include file="./shared/navbar.jsp"%>

		<!-- Page Content -->
		<!-- Load when user clicks home -->
		<div class="content">
			<c:if test="${userClickHome == true}">
				<%@ include file="home.jsp"%>
			</c:if>

			<!-- Load when user clicks about -->
			<c:if test="${userClickAbout == true}">
				<%@ include file="about.jsp"%>
			</c:if>

			<!-- Load when user clicks contact -->
			<c:if test="${userClickContact == true}">
				<%@ include file="contact.jsp"%>
			</c:if>

			<!-- Load when user clicks view products -->
			<c:if test="${userClickAllProducts == true or userClickCategoryProducts == true}">
				<%@ include file="listProducts.jsp"%>
			</c:if>

			<!-- Load when user clicks show product -->
			<c:if test="${userClickShowProduct == true}">
				<%@ include file="singleProduct.jsp"%>
			</c:if>

			<!-- Load when user clicks manage products -->
			<c:if test="${userClickManageProducts == true}">
				<%@ include file="manageProducts.jsp"%>
			</c:if>
			
			<!-- Load when user clicks show cart -->
			<c:if test="${userClickShowCart == true}">
				<%@ include file="cart.jsp"%>
			</c:if>
		</div>

		<!-- Footer -->
		<%@ include file="./shared/footer.jsp"%>

		<!-- Bootstrap core JavaScript -->
		<script src="${js}/jquery.min.js"></script>

		<!-- jQuery Validator -->
		<script src="${js}/jquery.validate.js"></script>

		<script src="${js}/bootstrap.min.js"></script>

		<!-- DataTable plugin -->
		<script src="${js}/jquery.dataTables.js"></script>

		<!-- DataTable bootstrap Script -->
		<script src="${js}/dataTables.bootstrap.js"></script>

		<!-- DataTable bootbox Script -->
		<script src="${js}/bootbox.min.js"></script>

		<!-- Self coded javascript file -->
		<script src="${js}/myapp.js"></script>
	</div>
</body>

</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
	<head>
	<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Index</title>
	<% pageContext.setAttribute("APP_PATH", request.getContextPath()); %>
	<script type="text/javascript" src="${ APP_PATH }/static/js/jquery-3.3.1.min.js"></script>
	<script type="text/javascript" src="${ APP_PATH }/static/js/popper.min.js"></script>
	<link rel="stylesheet" href="${ APP_PATH }/static/bootstrap-4.0.0-dist/css/bootstrap.min.css">
	<script type="text/javascript" src="${ APP_PATH }/static/bootstrap-4.0.0-dist/js/bootstrap.min.js"></script>
	<script>
		$(function() {
			$("#list").click(function() {
				location.assign("${ APP_PATH }/empls")
			});
		});
	</script>
	</head>
	<body>

	<button id="list" class="btn btn-success">list</button>

	</body>
</html>
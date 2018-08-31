<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>

<html>
	<head>
	<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>员工列表</title>
	<% pageContext.setAttribute("APP_PATH", request.getContextPath()); %>
	<script type="text/javascript" src="${ APP_PATH }/static/js/jquery-3.3.1.min.js"></script>
	<script type="text/javascript" src="${ APP_PATH }/static/js/popper.min.js"></script>
	<script type="text/javascript" src="${ APP_PATH }/static/js/vue.js"></script>
	<link rel="stylesheet" href="${ APP_PATH }/static/bootstrap-4.0.0-dist/css/bootstrap.min.css">
	<script type="text/javascript" src="${ APP_PATH }/static/bootstrap-4.0.0-dist/js/bootstrap.min.js"></script>
	<script>
		$(function() {
			console.log("enter list page ...")
		});
	</script>
	
	</head>
	<body>
		<div class="container" id="app">
			<div class="row">
				<div class="col-md-12">
					<h1>SSM-CRUD</h1>
				</div>
			</div>
			<div class="row">
				<div class="col-md-4 offset-md-8">
					<button class="btn btn-primary">新增</button>
					<button class="btn btn-danger">删除</button>
				</div>
			</div>
			<div class="row" style="height: 400px;">
				<div class="col-md-12">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>#</th>
								<th>Name</th>
								<th>Gender</th>
								<th>Email</th>
								<th>Department</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${pageInfo.list }" var="empl">
								<tr>
									<td>${empl.emplId }</td>
									<td>${empl.emplName }</td>
									<td>${empl.emplGender == "M" ? "男" : "女" }</td>
									<td>${empl.emplEmail }</td>
									<td>${empl.department.deptName }</td>
									<td>
										<button class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-pencil"></span>修改</button>
										<button class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-trash"></span>删除</button>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">共${pageInfo.pages }页,第${pageInfo.pageNum }页,共${pageInfo.total }条记录</div>
				<div class="col-md-6">
					<nav aria-label="Page navigation example">
					  <ul class="pagination">
					  	<li class="page-item"><a class="page-link"  href="${APP_PATH }/empls?pageNum=1">首页</a></li>
					    <c:choose>
					    	<c:when test="${pageInfo.hasPreviousPage }">
							    <li class="page-item">
							      <a class="page-link" href="${APP_PATH }/empls?pageNum=${pageInfo.pageNum - 1 }" aria-label="Previous">
							        <span aria-hidden="true">&laquo;</span>
							        <span class="sr-only">Previous</span>
							      </a>
							    </li>
					    	</c:when>
						    <c:otherwise>
							    <li class="page-item disabled">
							      <a class="page-link" href="#" aria-label="Previous">
							        <span aria-hidden="true">&laquo;</span>
							        <span class="sr-only">Previous</span>
							      </a>
							    </li>
						    </c:otherwise>
					    </c:choose>
					    <c:forEach items="${pageInfo.navigatepageNums }" var="pageNum">
					    	<c:choose>
					    		<c:when test="${pageNum == pageInfo.pageNum }">
					    			<li class="page-item active"><a class="page-link"  href="${APP_PATH }/empls?pageNum=${pageNum}">${pageNum}</a></li>
					    		</c:when>
					    		<c:otherwise>
					    			<li class="page-item"><a class="page-link"  href="${APP_PATH }/empls?pageNum=${pageNum}">${pageNum}</a></li>
					    		</c:otherwise>
					    	</c:choose>
					    </c:forEach>
					    <c:choose>
					    	<c:when test="${pageInfo.hasNextPage }">
					    		<li class="page-item">
							      <a class="page-link" href="${APP_PATH }/empls?pageNum=${pageInfo.pageNum + 1 }" aria-label="Next">
							        <span aria-hidden="true">&raquo;</span>
							        <span class="sr-only">Next</span>
							      </a>
							    </li>
					    	</c:when>
						    <c:otherwise>
						    	<li class="page-item disabled">
							      <a class="page-link" href="#" aria-label="Next">
							        <span aria-hidden="true">&raquo;</span>
							        <span class="sr-only">Next</span>
							      </a>
							    </li>
						    </c:otherwise>
					    </c:choose>
					  	<li class="page-item"><a class="page-link"  href="${APP_PATH }/empls?pageNum=${pageInfo.pages}">末页</a></li>
					  </ul>
					</nav>
				</div>
			</div>
		</div>
	</body>
</html>
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
	<script src="https://cdn.bootcss.com/vue/2.4.2/vue.min.js"></script>
	<script type="text/javascript" src="${ APP_PATH }/static/js/vue.js"></script>
	<link rel="stylesheet" href="${ APP_PATH }/static/bootstrap-4.0.0-dist/css/bootstrap.min.css">
	<script type="text/javascript" src="${ APP_PATH }/static/bootstrap-4.0.0-dist/js/bootstrap.min.js"></script>
	<script>
		var vm
		$(function() {
			console.log("enter list page ...")
			
			vm = new Vue({
				el: '#app',
				data: {
					list: [],
					page: {
						pageNum: '',
						pages: '',
						total: '',
						hasPreviousPage: '',
						hasNextPage: '',
						navigatepageNums: []
					},
					depts: []
				},
				methods: {
					getEmpls: function (pageNum) {
						$.ajax({
							url: '${ APP_PATH }/empls2json?pageNum=' + pageNum,
							method: 'GET',
							success:  (result) => {
								var pageInfo = result && result.data && result.data && result.data.pageInfo
								this.list = pageInfo && pageInfo.list
								this.page = {
									pageNum: pageInfo.pageNum,
									pages: pageInfo.pages,
									total: pageInfo.total,
									hasPreviousPage: pageInfo.hasPreviousPage,
									hasNextPage: pageInfo.hasNextPage,
									navigatepageNums: pageInfo.navigatepageNums
								}
							}
						})
					},
					getDepts: function () {
						$.ajax({
							url: '${ APP_PATH }/depts2json',
							method: 'GET',
							success:  (result) => {
								this.depts = depts = result && result.data && result.data && result.data.depts
							}
						})
					},
					insert: function (data) {
						$.ajax({
							url: '${ APP_PATH }/epml',
							method: 'POST',
							success:  (result) => {
								// 1. 是否插入成功
								
								// 2. 失败 - 显示失败信息
								
								// 3. 成功 - 关闭模态框
							}
						})
					}
				},
				mounted: function () {
					this.getEmpls(1)
				}
			})
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
					<button class="btn btn-primary" data-toggle="modal" data-target="#modal_panel" @click="getDepts">新增</button>
					<button class="btn btn-danger">删除</button>
				</div>
			</div>
			<div class="modal fade" id="modal_panel" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  				<div class="modal-dialog" role="document">
  					<modalpanel title="新增用户" opt="保存" :depts="depts" @insert="insert"></modalpanel>
  				</div>
  			</div>
			<div class="row" style="height: 400px;">
				<table_com :list="list"></table_com>
			</div>
			<pagination @pageto=getEmpls :page="page"></pagination>
		</div>
	</body>
</html>
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
	<link rel="stylesheet" href="${ APP_PATH }/static/bootstrap-3.3.7-dist/css/bootstrap.min.css">
	<script type="text/javascript" src="${ APP_PATH }/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
	<script>
		var vm
		$(function() {
			console.log("enter list page ...")
			vm = new Vue({
				el: '#app',
				data: {
					list: [],
					chooseList: [],
					page: {
						pageNum: '',
						pages: '',
						total: '',
						hasPreviousPage: '',
						hasNextPage: '',
						navigatepageNums: []
					},
					modal: {
						depts: [],
						name_status: {
							status: false,
							desc: ''
						},
						email_status: {
							status: false,
							desc: ''
						},
						modal_data: {
							emplId: '',
							emplName: '',
							emplEmail: '',
							emplGender: '',
							deptId: '',
						}
					}
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
								this.modal.depts = result && result.data && result.data.depts
							}
						})
					},
					getEmplById: function (id) {
						$.ajax({
							url: '${ APP_PATH }/empl/' + id,
							method: 'GET',
							success:  (result) => {
								this.modal.modal_data = result && result.data && result.data.empl
							}
						})
					},
					enter_add_panel: function () {
						this.modal.modal_data = {
							emplId: '',
							emplName: '',
							emplEmail: '',
							emplGender: '',
							deptId: '',
						}
						this.getDepts()
					},
					validate_name: function (name) {
						var reg = /(^[\u2E80-\u9FFF0-9a-zA-Z]{3,10}$)/ 
						if (reg.test(name)){
							$.ajax({
								url: '${ APP_PATH }/validateName',
								method: 'POST',
								data: {name: name},
								success:  (result) => {
									if (result && result.code === "100") {
										// 用户名可用
										this.modal.name_status = {
											status: true,
											desc: result.desc
										}
									} else {
										// 用户名不可用
										this.modal.name_status = {
											status: false,
											desc: result.desc
										}
									}
								}
							})
						} else {
							// 用户名5到16位英文或3到6位中文
							this.modal.name_status = {
								status: false,
								desc: '用户名3到10字符'
							}
						}
						
					},
					validate_email: function(email) {
						var reg = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/
						if (reg.test(email)){
							this.modal.email_status = {
								status: true,
								desc: '邮箱可用'
							}
						} else {
							this.modal.email_status = {
								status: false,
								desc: '邮箱格式不正确'
							}
						}
					},
					insert: function (data) {
						$.ajax({
							url: '${ APP_PATH }/empl',
							method: 'POST',
							data: data,
							success:  (result) => {
								// 1. 是否插入成功
								if (result.code === "200") {
									// 2. 失败 - 显示失败信息
									var errors = result && result.data && result.data.errors
									if (errors.emplEmail) {
										this.modal.email_status = {
											status: false,
											desc: errors.emplEmail
										}
									}
									if (errors.emplName) {
										this.modal.name_status = {
											status: false,
											desc: errors.emplName
										}
									}
								} else {
									// 3. 显示最后一页
									this.getEmpls(this.page.total)
									// 4. 成功 - 关闭模态框
									$('#modal_panel').modal('hide')
									// 5. 清空模态框
									this.modal.modal_data = {
										emplName: '',
										emplEmail: '',
										emplGender: '',
										deptId: '',
									}
								}
							}
						})
					},
					showupdatepanel: function (data) {
//						this.modal.modal_data = data
						this.getEmplById(data)
						this.getDepts()
						$('#modal_panel_update').modal('show')
					},
					update: function (data) {
					//	data._method = "PUT"
						$.ajax({
							url: '${ APP_PATH }/empl/' + data.emplId,
							method: 'PUT',
							data: data,
							success:  (result) => {
								// 1. 是否插入成功
								if (result.code === "200") {
									// 2. 失败 - 显示失败信息
									var errors = result && result.data && result.data.errors
									if (errors.emplEmail) {
										this.modal.email_status = {
											status: false,
											desc: errors.emplEmail
										}
									}
									if (errors.emplName) {
										this.modal.name_status = {
											status: false,
											desc: errors.emplName
										}
									}
								} else {
									// 3. 显示当前页
									this.getEmpls(this.page.pageNum)
									// 4. 成功 - 关闭模态框
									$('#modal_panel_update').modal('hide')
									// 5. 清空模态框
									this.modal.modal_data = {
										emplName: '',
										emplEmail: '',
										emplGender: '',
										deptId: '',
									}
								}
							}
						})
					},
					deletelist: function (list) {
						this.chooseList = list
					},
					deleteEmpl: function (list) {
						if (!(list instanceof Array)) {
							list = this.chooseList
						}
						if (list.length <= 0) return
						if (!confirm('是否确定[ ' + list + ' ]? ')) return
						$.ajax({
							url: '${ APP_PATH }/empl/' + list.join('-'),
							method: 'DELETE',
							success:  (result) => {
								if (result && result.code == "100") {
									alert("删除成功 !!")
									this.getEmpls(this.page.pageNum)
								} else {
									alert("删除失败 !!")
								}
							}
						})
					}
				},
				mounted: function () {
					this.getEmpls(1)
				}
			})
			$('[data-toggle="tooltip"]').tooltip()
			$('[data-toggle="popover"]').popover()
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
				<div class="col-md-4 col-md-offset-8">
					<button class="btn btn-primary" data-toggle="modal" data-target="#modal_panel" @click="enter_add_panel">新增</button>
					<button class="btn btn-danger" @click="deleteEmpl">删除</button>
				</div>
			</div>
			
			<div class="modal fade" id="modal_panel" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  				<div class="modal-dialog" role="document">
  					<modalpanel 
  						title="新增用户" 
  						opt="保存" 
  						:depts="modal.depts" 
 						:name_status="modal.name_status" 
 						:email_status="modal.email_status"
 						:data="modal.modal_data"
 						@validate_name="validate_name"
 						@validate_email="validate_email"
 						@insert="insert">
 					</modalpanel>
  				</div>
  			</div>
  			
  			
			<div class="modal fade" id="modal_panel_update" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  				<div class="modal-dialog" role="document">
  					<modalpanel 
  						title="更新用户" 
  						opt="更新" 
  						:depts="modal.depts" 
 						:name_status="modal.name_status" 
 						:email_status="modal.email_status"
 						:data="modal.modal_data"
 						@validate_name="validate_name"
 						@validate_email="validate_email"
 						@insert="update">
 					</modalpanel>
  				</div>
  			</div>
  			
			<div class="row" style="height: 400px;">
				<table_com :list="list" @showupdatepanel="showupdatepanel" @deletelist="deletelist" @deleteempl="deleteEmpl"></table_com>
			</div>
			<pagination @pageto="getEmpls" :page="page"></pagination>
		</div>
	</body>
</html>
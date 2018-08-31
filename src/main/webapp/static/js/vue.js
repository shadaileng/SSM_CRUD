if (typeof(Vue)!=='undefined') {
	Vue.component('table_com', {
		template: '<div class="col-md-12">' +
					'<table class="table table-hover">\
						<thead>\
							<tr>\
								<th>#</th><th>Name</th><th>Gender</th><th>Email</th><th>Department</th><th>操作</th>\
							</tr>\
						</thead>\
						<tbody>\
							<tr v-for="item in list">\
								<td>{{ item.emplId }}</td>\
								<td>{{ item.emplName }}</td>\
								<td>{{ item.emplGender === "M" ? "男" : "女" }}</td>\
								<td>{{ item.emplEmail }}</td>\
								<td>{{ item.department && item.department.deptName }}</td>\
								<td>\
									<button class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-pencil"></span>修改</button>\
									<button class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-trash"></span>删除</button>\
								</td>\
							</tr>\
						</tbody>\
					</table>\
				</div>',
		props: ['list'],
	})
	
	Vue.component('pagination', {
		template: '\
			<div class="row">\
				<div class="col-md-6 text-center">共 {{ page.pages }} 页,第{{ page.pageNum }}页,共{{ page.total }}条记录</div>\
				<div class="col-md-6">\
					<nav aria-label="Page navigation example">\
					  <ul class="pagination">\
						<li class="page-item"><a class="page-link" :nav="1" @click="pageTo">首页</a></li>\
					    <li class="page-item"><a class="page-link" :class="{disabled : !page.hasPreviousPage}" :nav="page.pageNum - 1" @click="pageTo">&laquo;</a></li>\
					    <li v-for="nav in page.navigatepageNums" class="page-item" :class="{active : page.pageNum === nav}"><a class="page-link" :nav="nav" @click="pageTo">{{ nav }}</a></li>\
					    <li class="page-item"><a class="page-link" :class="{disabled : !page.hasNextPage}" :nav="page.pageNum + 1" @click="pageTo">	&raquo;</a></li>\
						<li class="page-item"><a class="page-link" :nav="page.total" @click="pageTo">末页</a></li>\
					  </ul>\
					</nav>\
				</div>\
			</div>',
		props: ['page'],
		methods: {
			pageTo: function (event) {
				var event = event || window.event
				event.preventDefault()
				var nav = event.currentTarget.attributes["nav"].value
				this.$emit('pageto', nav)
				return false;
			}
		}
	})
	
	Vue.component('modalpanel', {
		template: '\
				<div class="modal-content">\
			      <div class="modal-header">\
			        <h5 class="modal-title" id="exampleModalLabel">{{ title }}</h5>\
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">\
			          <span aria-hidden="true">&times;</span>\
			        </button>\
			      </div>\
			      <div class="modal-body">\
					<form>\
						<div class="form-group row">\
							<label for="emplName" class="col-md-2 col-form-label">姓名</label>\
							<div class="col-md-10">\
								<input type="text" class="form-control" aria-describedby="emailHelp" placeholder="5到16位英文或3到6位中文" v-model="emplName">\
							</div>\
						</div>\
						<div class="form-group row">\
							<label for="emplEmail" class="col-md-2 col-form-label">邮箱</label>\
							<div class="col-md-10">\
								<input type="email" class="form-control" aria-describedby="emailHelp" placeholder="yourname@example.com" v-model="emplEmail">\
							</div>\
						</div>\
							<div class="form-group row">\
							<label for="emplGender" class="col-sm-2 col-form-label">性别</label>\
							<div class="col-md-10">\
								<div class="form-check form-check-inline">\
								  <input class="form-check-input" type="radio" name="emplGender" value="M" checked v-model="emplGender">\
								  <label class="form-check-label" for="inlineRadio1">男</label>\
								</div>\
								<div class="form-check form-check-inline">\
								  <input class="form-check-input" type="radio" name="emplGender" value="F" v-model="emplGender">\
								  <label class="form-check-label" for="inlineRadio2">女</label>\
								</div>\
							</div>\
						</div>\
						<div class="form-group row">\
							<label for="deptId" class="col-md-2 col-form-label">部门</label>\
							<div class="col-md-4">\
						      <select id="inputState" class="form-control"  v-model="deptId">\
						        <option v-for="(dept, i) in depts" :key="i" :value="dept.deptId" >{{ dept.deptName }}</option>\
						      </select>\
							</div>\
						</div>\
					</form>\
			      </div>\
			      <div class="modal-footer">\
			        <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>\
			        <button type="button" class="btn btn-primary" @click="insert">{{ opt }}</button>\
			      </div>\
			    </div>\
			',
		props: ['title', 'opt', 'depts'],
		data: function () {
			return {
				emplName: '',
				emplEmail: '',
				emplGender: '',
				deptId: ''
			}
		},
		methods: {
			insert: function () {
				
				// 1. 数据校验
				// 2. 封装数据
				var data = this.$data
				// 3. 触发事件
				this.$emit('insert', data)
			}
		},
		computed: {
			burl: function() {
				return this.depts
			}
		}
	})
	
}
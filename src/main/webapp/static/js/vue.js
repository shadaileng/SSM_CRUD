if (typeof(Vue)!=='undefined') {
	Vue.component('table_com', {
		template: '<div class="col-md-12">' +
					'<table class="table table-hover">\
						<thead>\
							<tr>\
								<th><input type="checkbox" value="" v-model="chengkAll"></th><th>#</th><th>Name</th><th>Gender</th><th>Email</th><th>Department</th><th>操作</th>\
							</tr>\
						</thead>\
						<tbody>\
							<tr v-for="item in list">\
								<td><input type="checkbox" :value="item.emplId" v-model="chooselist"></td>\
								<td>{{ item.emplId }}</td>\
								<td>{{ item.emplName }}</td>\
								<td>{{ item.emplGender === "M" ? "男" : "女" }}</td>\
								<td>{{ item.emplEmail }}</td>\
								<td>{{ item.department && item.department.deptName }}</td>\
								<td>\
									<button class="btn btn-primary btn-sm" :mdata="item.emplId" @click="enter_update_panel"><span class="glyphicon glyphicon-pencil"></span>修改</button>\
									<button class="btn btn-danger btn-sm" :mdata="item.emplId" @click="deleteEmpl"><span class="glyphicon glyphicon-trash"></span>删除</button>\
								</td>\
							</tr>\
						</tbody>\
					</table>\
				</div>',
		props: ['list'],
		data: function () {
			return  {
				chengkAll: false,
				chooselist: []
			}
		},
		methods: {
			enter_update_panel: function (event) {
				var event = event || window.event
				var mdata = event.currentTarget.attributes["mdata"].value
				this.$emit('showupdatepanel', mdata)
			},
			deleteEmpl: function (event) {
				var event = event || window.event
				var mdata = event.currentTarget.attributes["mdata"].value
				this.chengkAll = false
				this.chooselist = []
				this.chooselist.push(Number(mdata))
				this.$emit('deleteempl', this.chooselist)
			}
		},
		watch: {
			chooselist: function () {
				this.$emit('deletelist', this.chooselist)
			},
			chengkAll: function () {
				if (this.chengkAll) {
					this.chooselist = []
					this.list.forEach((e) => {
						this.chooselist.push(e.emplId)
					})
				} else {
					this.chooselist = []
				}
			},
			list: function () {
				this.chooselist = []
				this.chengkAll = false
			}
		}
	})
	
	Vue.component('pagination', {
		template: '\
			<div class="row">\
				<div class="col-md-6 text-center">共 {{ page.pages }} 页,第{{ page.pageNum }}页,共{{ page.total }}条记录</div>\
				<div class="col-md-6">\
					<nav aria-label="Page navigation">\
						<ul class="pagination">\
							<li><a href="#" :nav="1" @click="pageTo">首页</a></li>\
							<li>\
								<a href="#" :class="{disabled : !page.hasPreviousPage}" :nav="page.pageNum - 1" @click="pageTo"aria-label="Previous">\
									<span aria-hidden="true">&laquo;</span>\
								</a>\
						    </li>\
							<li v-for="nav in page.navigatepageNums" :class="{active : page.pageNum === nav}"><a href="#" :nav="nav" @click="pageTo">{{ nav }}</a></li>\
							<li>\
								<a href="#" aria-label="Next"  :nav="page.pageNum + 1" @click="pageTo">\
									<span aria-hidden="true">&raquo;</span>\
								</a>\
							</li>\
							<li><a href="#" :nav="page.total" @click="pageTo">末页</a></li>\
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
						<div class="form-group row" >\
							<label for="emplName" class="col-md-2 control-label text-center">姓名</label>\
							<div class="col-md-8" :class="[name_status?(name_status.status?`has-success`:`has-error`):``]">\
								<p v-if="opt===`更新`" class="form-control-static" v-text="emplName"></p>\
								<input v-if="opt===`保存`" type="text" class="form-control" placeholder="5到16位英文或3到6位中文" v-model="emplName" name="emplName" \
								@keyup="validateName" @blur="hide_tips"\
								data-toggle="popover" data-placement="top" \
								data-trigger="manual" data-delay="{show: 500, hide: 100}">\
							</div>\
						</div>\
						<div class="form-group row">\
							<label for="emplEmail" class="col-md-2 control-label text-center">邮箱</label>\
							<div class="col-md-8" :class="[email_status.status?`has-success`:`has-error`]">\
								<input type="email" class="form-control" placeholder="yourname@example.com" v-model="emplEmail" name="emplEmail" \
								@keyup="validateEmail" @blur="hide_tips"\
								data-toggle="popover" data-placement="top" \
								data-trigger="manual" data-delay="{show: 500, hide: 100}">\
							</div>\
						</div>\
						<div class="form-group row">\
							<label for="emplGender" class="col-md-2 control-label text-center">性别</label>\
							<div class="col-md-8">\
								<div class="form-check form-check-inline col-md-2">\
								  <input class="form-check-input" type="radio" name="emplGender" value="M" checked v-model="emplGender">\
								  <label class="form-check-label" for="inlineRadio1">男</label>\
								</div>\
								<div class="form-check form-check-inline col-md-2">\
								  <input class="form-check-input" type="radio" name="emplGender" value="F" v-model="emplGender">\
								  <label class="form-check-label" for="inlineRadio2">女</label>\
								</div>\
							</div>\
						</div>\
						<div class="form-group row">\
							<label for="deptId" class="col-md-2 control-label text-center">部门</label>\
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
		props: ['title', 'opt', 'depts', 'name_status', 'email_status', 'data'],
		data: function () {
			return {
				emplId: '',
				emplName: '',
				emplEmail: '',
				emplGender: '',
				deptId: '',
			}
		},
		methods: {
			hide_tips: function (e) {
				$(e.currentTarget).popover('hide')
			},
			validateName: function () {
				this.$emit('validate_name', this.$data.emplName)
			},
			validateEmail: function () {
				this.$emit('validate_email', this.$data.emplEmail)
			},
			insert: function () {
				if (this.opt === '更新') {
					this.name_status.status = true
				}
				// 1. 数据校验
				if (!this.name_status.status || !this.email_status.status){
					$('[name="emplName"]').attr('data-content', this.name_status.desc)
					$('[name="emplName"]').popover('show')
					$('[name="emplEmail"]').attr('data-content', this.email_status.desc)
					$('[name="emplEmail"]').popover('show')
					return
				}
				// 2. 封装数据
				var data = {
					emplId: this.$data.emplId,
					emplName: this.$data.emplName,
					emplEmail: this.$data.emplEmail,
					emplGender: this.$data.emplGender,
					deptId: this.$data.deptId
				}
				// 3. 触发事件
				this.$emit('insert', data)
			}
		},
		watch: {
			name_status: function() {
				$('[name="emplName"]').attr('data-content', this.name_status.desc)
				$('[name="emplName"]').popover('show')
			},
			email_status: function() {
				$('[name="emplEmail"]').attr('data-content', this.email_status.desc)
				$('[name="emplEmail"]').popover('show')
			},
			data: function () {
				this.emplId = this.data.emplId
				this.emplName = this.data.emplName
				this.emplEmail = this.data.emplEmail
				this.emplGender = this.data.emplGender
				this.deptId = this.data.deptId
			}
		}
	})
	
}
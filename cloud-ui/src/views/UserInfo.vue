<template>
  <page-header title="User Info" tip="Your infomations."></page-header>
	<div class="info">
		<div class="avatar">
			<el-upload class="avatar-uploader" action="/api/user-info/uploadAvatar" name="avatarFile"
				:headers="{'Authorization': `Bearer ${$store.state.accessToken}`}" :show-file-list="false"
				:on-success="uploadAvatarSuccess" :before-upload="beforeUploadAvatar"
			>
				<img src="../assets/icon/user_info/avatar_upload.svg" alt="avatar_upload">
			</el-upload>
			<img class="avatar-img" :src="userInfo.avatarUrl" alt="avatar">
		</div>
		<el-descriptions
			class="margin-top"
			:column="4"
			size="large"
			border
		>
			<el-descriptions-item :min-width="100">
				<template #label>
					<div class="cell-item">
						Nickname
					</div>
				</template>
				{{ userInfo.nickname }}
			</el-descriptions-item>
			<el-descriptions-item :min-width="100">
				<template #label>
					<div class="cell-item">
						E-mail
					</div>
				</template>
				{{ userInfo.username }}
			</el-descriptions-item>
			<el-descriptions-item :min-width="100">
				<template #label>
					<div class="cell-item">
						Stored Num
					</div>
				</template>
				{{ userInfo.storedNum }}
			</el-descriptions-item>
			<el-descriptions-item :min-width="100">
				<template #label>
					<div class="cell-item">
						Stored
					</div>
				</template>
				{{ storedSize }}
			</el-descriptions-item>
			<el-descriptions-item :min-width="100">
				<template #label>
					<div class="cell-item">
						CreateTime
					</div>
				</template>
				{{ userInfo.createTime }}
			</el-descriptions-item>
		</el-descriptions>
	</div>
	<div class="card-list">
		<div class="card">
			<div class="card-header">Secondary path</div>
			<div class="card-body">
				<el-tree
					:data="userInfo.secondaryPathList"
					node-key="id"
					default-expand-all
				>
					<!-- <template #default="{ node, data }">
						<span class="tree-node">
							<span>/{{ node.label }}</span>
							<span>
								<a @click="alert(data)"> Append </a>
								<a @click="alert(node, data)"> Delete </a>
							</span>
						</span>
					</template> -->
					<template #default="{ node }">
						<span class="tree-node">
							<span>/{{ node.label }}</span>
						</span>
					</template>
				</el-tree>
			</div>
		</div>
		<div class="card" style="max-height: 500px;">
			<div class="card-header">Modify information</div>
			<div class="card-body">
				<el-tabs v-model="updateForm.form">
					<el-tab-pane label="Information" name="basicInfo">
						<div class="form-item">
							<div class="label">Nickname</div>
							<el-input v-model="updateForm.nickname" placeholder="Enter a nickname you like." clearable />
						</div>
						<div class="form-item">
							<div></div>
							<div>
								<el-button type="primary" @click="modifyInfo">Save</el-button>
								<el-button @click="resetInfoForm">Reset</el-button>
							</div>
						</div>
					</el-tab-pane>
					<el-tab-pane label="Password" name="changePassword">
						<div class="form-item">
							<div class="label">Old Password</div>
							<el-input v-model="updateForm.oldPassword" type="password" placeholder="Please enter the old password." clearable />
						</div>
						<div class="form-item">
							<div class="label">New Password</div>
							<el-input v-model="updateForm.newPassword" type="password" placeholder="Please enter the new password." clearable />
						</div>
						<div class="form-item">
							<div class="label">Confirm Password</div>
							<el-input v-model="updateForm.confirmPassword" type="password" placeholder="Please confirm your password." clearable />
						</div>
						<div class="form-item" v-if="updateForm.error">
							<div></div>
							<div class="error">
								{{ updateForm.error }}
							</div>
						</div>
						<div class="form-item">
							<div></div>
							<div>
								<el-button type="primary" @click="modifyPwd">Save</el-button>
								<el-button @click="resetPwdForm">Reset</el-button>
							</div>
						</div>
					</el-tab-pane>
				</el-tabs>
			</div>
		</div>
	</div>
</template>

<script>
import { reactive, computed } from 'vue';
import { useStore } from 'vuex'
import server from '@/util/request';
import md5 from 'js-md5'
import '@/assets/css/el-input-medium.css'
import '@/assets/css/el-upload-avatar.css'
import PageHeader from '@/components/PageHeader'
import { ElMessage } from 'element-plus';

export default {
  name: 'UserInfo',
  components: {
    'page-header': PageHeader,
  },
	setup() {
		const store = useStore()

		const userInfo = reactive({
			username: store.state.userInfo.username,
      nickname: store.state.userInfo.nickname,
      avatarUrl: store.state.userInfo.avatarUrl,
			storedNum: store.state.userInfo.storedNum,
      secondaryPathList: [{id: 0, label: '', children: pathListToTree(store.state.userInfo.secondaryPathList)}],
      createTime: store.state.userInfo.createTime,
		})

		// 已存储大小
    const storedSize = computed(()=>{
      if (store.state.userInfo.storedSize < 1024)
        return `${store.state.userInfo.storedSize} B`
      else if (store.state.userInfo.storedSize < 1024*1024)
        return `${(store.state.userInfo.storedSize / 1024).toFixed(2)} KB`
      else if (store.state.userInfo.storedSize < 1024*1024*1024)
        return `${(store.state.userInfo.storedSize / 1024/1024).toFixed(2)} MB`
      else
        return `${(store.state.userInfo.storedSize / 1024/1024/1024).toFixed(2)} GB`
    })

		// 次级路径数组转树结构
		function pathListToTree(pathList) {
			// 1.记录根位置
			let tree = []
			let id = 1
			pathList.forEach(path => {
				// 2.待匹配项
				let pathItemList = path.split('/')
				// 3.将移动指针重置顶层，确保每次从根检索匹配（必须！！！）
				let levelList = tree
				// 4.遍历待询节点
				for (let label of pathItemList) {
					// 5.同层同名节点查找匹配
					let obj = levelList.find(item => item.label == label)
					// 6.若不存在则建立该节点
					if (!obj) {
						obj = { id: id++, label, children: [] }
						levelList.push(obj)
						// 7.若当前被增节点是叶子节点，则裁剪该节点子节点属性
						// if (label == pathItemList[pathItemList.length - 1]) {
						// 	delete obj.children
						// }
					}
					// 8.已有则进入下一层，继续寻找
					levelList = obj.children
				}
			})
			return tree
		}

		// 更新用户头像
		function beforeUploadAvatar(file) {
			// 检查文件大小
      if (file.size > 1*1024*1024) {
        ElMessage.warning('The file size cannot exceed 1MB!')
        return
      }
		}
		function uploadAvatarSuccess(res) {
			if (res.code === 200) {
				store.state.userInfo.avatarUrl = res.avatarUrl
				userInfo.avatarUrl = res.avatarUrl
				ElMessage.success('The avatar was updated successfully.')
			} else {
				ElMessage.error(res.msg)
			}
		}

		// 用户信息更新表单
		const updateForm = reactive({
			form: 'basicInfo',
			nickname: store.state.userInfo.nickname,
			oldPassword: '',
			newPassword: '',
			confirmPassword: '',
			error: '',
		})

		// 更新用户基本信息
		function modifyInfo() {
			let params = {
				nickname: updateForm.nickname
			}
			server.get('/user-info/updateUserInfo', {params}).then(res=>{
        userInfo.nickname = updateForm.nickname
        store.commit('updateUserInfo', {nickname: updateForm.nickname})
				ElMessage.success(res.msg)
			})
		}
		function resetInfoForm() {
			updateForm.nickname = store.state.userInfo.nickname
			updateForm.error = ''
		}

		// 更新用户密码
		function modifyPwd() {
			if (updateForm.newPassword.length <6 || updateForm.newPassword.length >30) {
				updateForm.error = 'Passwords must be between 6 to 30 characters.'
				return
			}
			if (updateForm.newPassword !== updateForm.confirmPassword) {
				updateForm.error = 'Two password don\'t match!'
				return
			}
			updateForm.error = ''
			let params = {
				oldPassword: md5(updateForm.oldPassword+'#'+updateForm.oldPassword),
				newPassword: md5(updateForm.newPassword+'#'+updateForm.newPassword),
			}
			server.get('/user/updatePassword', {params}).then(res=>{
				ElMessage.success(res.msg)
			})
		}
		function resetPwdForm() {
			updateForm.oldPassword = ''
			updateForm.newPassword = ''
			updateForm.confirmPassword = ''
			updateForm.error = ''
		}

		return {
			userInfo, storedSize,
			beforeUploadAvatar, uploadAvatarSuccess,
			updateForm, modifyInfo, resetInfoForm, modifyPwd, resetPwdForm
		}
	}
}
</script>

<style scoped>
.info {
	margin: 1.2rem 1rem;
	display: -webkit-flex;
	display: flex;
	align-items: center;
}
.avatar {
	--avatar-w: 97px;
	--avatar-h: 97px;
	width: var(--avatar-w);
	height: var(--avatar-h);
	margin-right: 20px;
}
.avatar .avatar-img {
	width: var(--avatar-w);
	height: var(--avatar-h);
	border-radius: 10px;
	position: relative;
	top: 0;
	z-index: 1;
}
.avatar:hover .avatar-img {
	top: -97px;
}
.avatar .avatar-uploader {
	width: var(--avatar-w);
	height: var(--avatar-h);
	border-radius: 10px;
	position: relative;
	z-index: 2;
	display: none;
}
.avatar .avatar-uploader img {
	width: 36px;
	height: 36px;
	position: relative;
	top: 50%;
	transform: translateY(-50%);
}
.avatar:hover .avatar-uploader {
	display: block;
}
.card-list {
	margin: 1rem;
	display: grid;
	grid-template-columns: 1fr 2fr;
	gap: 1rem;
}
.card {
	border: 1px solid #e4e7ed;
	border-radius: 4px;
	color: #303133;
	box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.card .card-header {
	padding: 14px 14px 7px;
	border-bottom: 1px solid #e4e7ed;
}
.card .card-body {
	padding: 10px;
}
.tree-node {
	width: 100%;
	background-color: #00000005;
}
.form-item {
	margin-bottom: 20px;
	display: grid;
	grid-template-columns: 100px auto;
	gap: 12px;
	align-items: center;
}
#pane-changePassword .form-item {
	grid-template-columns: 150px auto;
}
.form-item .label {
	justify-self: end;
}
.form-item .label::before {
	content: "*";
	color: #ff4949;
	margin-right: 3px;
}
.form-item .error {
	font-size: 14px;
	color: #f56c6c;
}
</style>
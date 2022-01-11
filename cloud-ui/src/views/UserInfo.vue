<template>
  <page-header title="User Info" tip="Your infomations."></page-header>
	<p>已创建的次级路径：</p>
	<p v-for="(path, i) in $store.state.userInfo.secondaryPathList" :key="i">{{ i+1 }}. /{{ path }}</p>
</template>

<script>
import { onMounted, reactive } from 'vue';
// import { useStore } from 'vuex'
// import server from '@/util/request';
import PageHeader from '@/components/PageHeader'

export default {
  name: 'UserInfo',
  components: {
    'page-header': PageHeader,
  },
	setup() {
		// const store = useStore()
		onMounted(() => {
			
    });
		const userInfo = reactive({
			secondaryPathList: [],
		})

		return { userInfo }
	}
}
</script>

<style scoped>

</style>
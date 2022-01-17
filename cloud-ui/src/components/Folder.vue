<template>
	<div class="folder" @click="$emit('toNextPath', folderName)">
		<div class="icon"><img src="../assets/icon/folder.svg"></div>
		<div class="name">{{ folderName }}</div>
	</div>
</template>

<script>
export default {
	name: 'Folder',
  props: {
		folderName: {
			type: String,
			required: true,
		}
	},
	setup() {

	}
}
</script>

<style scoped>
.folder {
  width: 226px;
  height: 226px;
  cursor: pointer;
}
.folder:hover {
  background-color: #409eff33;
}
.folder .icon {
  box-sizing: border-box;
  width: 226px;
  height: 200px;
  padding: 0 13px;
}
.folder .icon img {
  width: 200px;
  height: 200px;
}
.folder .name {
  width: 226px;
  height: 26px;
  text-align: center;
}
</style>
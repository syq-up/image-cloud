<template>
	<div class="daily-list-item">
    <div class="img-menu">
      <div class="copy">
        <el-tooltip content="Copy [URL] link" effect="light" placement="right">
          <img @click="copy('url')" class="item hvr-grow" src="../assets/icon/image/copy_url.svg">
        </el-tooltip>
        <el-tooltip content="Copy [HTML] tag" effect="light" placement="right">
          <img @click="copy('html')" class="item hvr-grow" src="../assets/icon/image/copy_html.svg">
        </el-tooltip>
        <el-tooltip content="Copy [MD] tag" effect="light" placement="right">
          <img @click="copy('md')" class="item hvr-grow" src="../assets/icon/image/copy_md.svg">
        </el-tooltip>
      </div>
      <el-tooltip content="View" effect="light" placement="right">
        <img @click="$emit('lookBigImage', imageObj.id)" class="look hvr-grow" src="../assets/icon/image/look.svg">
      </el-tooltip>
      <el-tooltip v-if="!deleted" content="Delete" effect="light" placement="left">
        <img @click="deleteById" class="delete hvr-grow" src="../assets/icon/image/delete.svg">
      </el-tooltip>
      <el-tooltip v-if="deleted" content="Restore" effect="light" placement="left">
        <img @click="restoreById" class="restore hvr-grow" src="../assets/icon/image/restore.svg">
      </el-tooltip>
    </div>
    <img class="img-div" :src="imageObj.url" alt="">
  </div>
</template>

<script>
import server from '@/util/request';
import { ElMessage } from 'element-plus'

export default {
  name: 'Image',
  props: {
    deleted: {
      type: Boolean,
      required: true,
    },
    imageObj: {
      type: Object,
      required: true,
    },
  },
  setup(props, {emit}) {
    function copy(category) {
      switch(category) {
        case 'url':
          navigator.clipboard.writeText(props.imageObj.url)
          .then(()=>{ ElMessage({message: 'The <b>URL</b> has been copied to the clipboard.', type: 'success', dangerouslyUseHTMLString: true,}) })
          .catch(err=>{ ElMessage.error(err) })
          break;
        case 'html':
          navigator.clipboard.writeText(`<img src="${props.imageObj.url}" alt="">`)
          .then(()=>{ ElMessage({message: 'The <b>H5 Tag</b> has been copied to the clipboard.', type: 'success', dangerouslyUseHTMLString: true,}) })
          .catch(err=>{ ElMessage.error(err) })
          break;
        case 'md':
          navigator.clipboard.writeText(`![](${props.imageObj.url})`)
          .then(()=>{ ElMessage({message: 'The <b>MD Tag</b> has been copied to the clipboard.', type: 'success', dangerouslyUseHTMLString: true,}) })
          .catch(err=>{ ElMessage.error(err) })
          break;
        default:
          ElMessage.error('error')
          break;
      }
    }
    function deleteById() {
      server.get('/image/deleteImageById/'+props.imageObj.id).then(res=>{
        if (res.code === 200) {
          emit('deleteByIndex', props.imageObj.id)
          ElMessage({message: 'The image is deleted successfully.', type: 'success',})
        }
      })
    }
    function restoreById() {
      server.get('/image/restoreImageById/'+props.imageObj.id).then(res=>{
        if (res.code === 200) {
          emit('restoreByIndex', props.imageObj.id)
          ElMessage({message: 'The image is restored successfully.', type: 'success',})
        }
      })
    }
    return { copy, deleteById, restoreById }
  }
}
</script>

<style scoped>
.daily-list-item {
  box-sizing: border-box;
  width: 226px;
  height: 226px;
}
.daily-list-item .img-div {
  box-sizing: border-box;
  width: 226px;
  height: 226px;
  object-fit: cover;
	border: 1px solid #999;
  position: relative;
  /* top: -226px; */
  /* z-index: 1; */
}
.daily-list-item .img-menu {
  width: 226px;
  height: 226px;
  display: -webkit-flex;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  background-image: radial-gradient(#99999910, #00000060);
  position: relative;
  z-index: 2;
  display: none;
}
.daily-list-item:hover .img-div {
  top: -226px;
}
.daily-list-item:hover .img-menu {
  display: -webkit-flex;
  display: flex;
}
.daily-list-item .img-menu .copy {
  width: 32px;
  display: -webkit-flex;
  display: flex;
  flex-direction: column;
}
.daily-list-item .img-menu .copy .item {
  padding: 1px 0;
  background-color: #fff;
}
.daily-list-item .img-menu .copy .item:first-child {
  padding-top: 0;
}
.daily-list-item .img-menu .look {
  width: 32px;
  align-self: center;
}
.daily-list-item .img-menu .delete {
  width: 32px;
  background-color: red;
}
.daily-list-item .img-menu .restore {
  width: 32px;
  background-color: #138dff;
}
</style>
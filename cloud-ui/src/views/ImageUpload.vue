<template>
  <page-header title="Image Upload" tip="Single image less than 5 MB, and less than 20 images can be selected at a time.">
    <template #menu>
      <el-autocomplete v-model="upload.secondaryPath" placeholder="Secondary path" :style="upload.secondaryPath===''?'':'border: 2px solid #409eff;'"
        clearable :fetch-suggestions="querySecondaryPath" @blur="checkSecondaryPath">
        <template #prepend>/</template>
      </el-autocomplete>
      <el-input v-model="upload.webImageUrl" placeholder="Upload web image here." clearable>
        <template #append>
          <el-button @click="uploadWebImage"><img src="../assets/icon/upload/input_upload.svg"></el-button>
        </template>
      </el-input>
      <span v-if="!$store.state.settings.uploadDirectly">该功能暂时无效 </span>
      <el-checkbox v-model="$store.state.settings.uploadDirectly" label="Upload directly" border></el-checkbox>
    </template>
  </page-header>
  <div style="padding-right: 0px;">
    <div class="file-preview">
      <el-upload class="file-drop-zone" name="file" :action="upload.action" :data="upload.data" :headers="upload.headers" :accept="upload.accept"
        drag multiple :show-file-list="false" :before-upload="beforeUpload" :on-success="uploadOnSuccess" :on-error="uploadOnError"
        ><!-- :http-request="handleUpload" -->
        <div class="file-drop-zone-title">
          <img src="../assets/icon/left_menu/upload.svg">
          <div class="el-upload__text">
            Drop file here or <em>click to upload</em>
          </div>
        </div>
      </el-upload>
    </div>
    <div class="file-menu-group">
      <span class="count">Selected: <b>{{ upload.selected }}</b> <span>images</span>, Total: <b>{{ getTotalSize }}</b> .</span>
      <el-button-group v-if="!$store.state.settings.uploadDirectly">
        <el-button type="info" size="large"><img class="icon" src="../assets/icon/upload/clear.svg">Clear</el-button>
        <el-button type="info" size="large"><img class="icon" src="../assets/icon/upload/upload.svg">Upload</el-button>
        <el-button type="primary" size="large"><img class="icon" src="../assets/icon/upload/image_list.svg">Select...</el-button>
      </el-button-group>
    </div>
    <div class="uploaded-list">
      <el-space wrap :size="14">
        <yq-image v-for="(item, i) in upload.list" :key="item.id" 
          :deleted="false" :imageObj="item" :index="i" @lookBigImage="lookBigImage"
        ></yq-image>
      </el-space>
    </div>
  </div>
</template>

<script>
import { onMounted, reactive, computed } from 'vue';
import { useStore } from 'vuex'
import server from '@/util/request';
import '@/assets/css/el-upload.css'
import PageHeader from '@/components/PageHeader'
import Image from '@/components/Image'
import { ElMessage } from 'element-plus';

export default {
  name: 'ImageUpload',
  components: {
    'page-header': PageHeader,
    'yq-image': Image,
  },
  setup() {
    const store = useStore()

    onMounted(() => {
      // 查询已创建的次级路径
      const temp = store.state.userInfo.secondaryPathList
      temp ? temp.forEach(val => { upload.secondaryPathList.push({value: '/'+val}) }) : ''
    })

    // 给出次级路径建议（来源于已创建的次级路径）
    const querySecondaryPath = (queryString, callback) => {
      const results = queryString ? upload.secondaryPathList.filter(secondaryPathFilter(queryString)) : upload.secondaryPathList
      callback(results)
    }
    const secondaryPathFilter = (queryString) => {
      return secondaryPathItem => {
        return (secondaryPathItem.value.indexOf(queryString) === 0)
      }
    }
    // 检查次级路径的格式
    function checkSecondaryPath() {
      upload.secondaryPath.replace('\\', '/')
      upload.secondaryPath.replace('//', '/')
      if (upload.secondaryPath[0]!=='/') {
        upload.secondaryPath = '/' + upload.secondaryPath
      }
      if(upload.secondaryPath[upload.secondaryPath.length-1]==='/') {
        upload.secondaryPath = upload.secondaryPath.substr(0, upload.secondaryPath.length-1)
      }
    }

    // 上传图像
    const upload = reactive({
      secondaryPathList: [],  // 已创建的次级路径，用于提示建议，元素为对象{value: ''}
      secondaryPath: '',  // 次级路径，用于本次上传文件的存储
      accept: 'image/*',  // 仅允许上传图片文件
      action: '/api/image/upload', // https://imagecloud.shiyq.top/api/image/upload
      headers: {'Authorization': `Bearer ${store.state.accessToken}`},  // 请求头需携带访问令牌
      data: {secondaryPath: '', webImageUrl: ''}, // 上传图像时携带的信息
      list: [], // 上传图像列表
      // index: 0, // 当前上传文件下标
      webImageUrl: '', // 上传网络图片
      selected: 0,  // 已选择图像总数
      totalSize: 0, // 已选择图像总大小（字节）
      error: 0, // 上传失败的图像总数
    })
    function beforeUpload(file) {
      // 检查文件大小
      if (file.size > 5*1024*1024) {
        ElMessage.warning('File too large!')
        return
      }
      // 上传前更新携带的信息data
      upload.data.secondaryPath = upload.secondaryPath.substring(1)
      // 上传成功前暂时使用文件uid区别不同图像
      upload.list.push({id: file.uid+'', url: require('../assets/icon/image/loading.svg')})
      upload.selected++
      upload.totalSize += file.size
    }
    function uploadOnSuccess(response, file) {
      if (response.code === 200) {
        // 更新图像路径，显示图像
        for (let i=0; i<upload.list.length; i++) {
          if (upload.list[i].id === file.uid+'') {
            upload.list[i].id = response.data.id
            upload.list[i].url = response.data.url
            break
          }
        }
        store.commit('increaseStoredSize', file.size)
        updateSecondaryPath()
      } else {
        for (let i=0; i<upload.list.length; i++) {
          if (upload.list[i].id === file.uid+'') {
            upload.list[i].url = ''
            break
          }
        }
        upload.error++
      }
    }
    function uploadOnError(err, file) {
      console.log(err, file)
    }

    // 上传网络图像
    function uploadWebImage() {
      upload.selected++
      const i = upload.list.length
      // 添加到上传显示列表
      upload.list.push({id: upload.webImageUrl.substring(upload.webImageUrl.lastIndexOf('/')), url: require('../assets/icon/image/loading.svg')})
      // 上传时携带的信息
      upload.data.userId = 1
      upload.data.secondaryPath = upload.secondaryPath.substring(1)
      upload.data.webImageUrl = upload.webImageUrl
      // 上传
      server.post('/image/uploadWebImage', upload.data).then(res=>{
        if (res.code === 200) {
          upload.list[i].id = res.data.id
          upload.list[i].url = res.data.url
          store.commit('increaseStoredSize', res.data.size)
          updateSecondaryPath()
        } else {
          upload.error++
        }
      })
      upload.data.webImageUrl = ''
    }

    // 计算已选择上传图像总大小
    const getTotalSize = computed(() => {
      if (upload.totalSize < 1024)
        return `${upload.totalSize} B`
      else if (upload.totalSize < 1024*1024)
        return `${(upload.totalSize / 1024).toFixed(2)} KB`
      else
        return `${(upload.totalSize / 1024 / 1024).toFixed(2)} MB`
    })
    // 检查次级路径，把新创建的次级路径记录到数据库 TODO 这里应该在后端检查…
    function updateSecondaryPath() {
      if (upload.secondaryPath.length > 0 && !upload.secondaryPathList.some(obj=>obj.value===upload.secondaryPath)) {
        store.commit('pushSecondaryPathList', upload.secondaryPath.substring(1))
      }
    }
    
    // 全屏查看图片
    function lookBigImage(index) {
      store.commit('changeShade', true)
      store.commit('changeImageList', upload.list)
      store.commit('changeImageListIndex', index)
      document.getElementsByTagName("body")[0].className="ban-scroll"
    }

    return {
      querySecondaryPath, checkSecondaryPath
      ,upload, beforeUpload, uploadOnSuccess, uploadOnError, uploadWebImage, getTotalSize
      ,lookBigImage,
    };
  },
};
</script>

<style scoped>
.file-preview {
  padding: 10px;
  margin: 0 1rem 0.5rem 1rem;
  border-radius: 5px;
  box-shadow: 0 0 15px 0 rgb(0 0 0 / 13%);
}
.file-drop-zone {
  margin: 10px;
}
.file-drop-zone-title {
  padding: 85px 0;
  font-size: 1.2rem;
}
.file-drop-zone-title img {
  width: 80px;
  height: 80px;
}
.file-menu-group {
  height: 40px;
  margin: 0 1rem;
  display: -webkit-flex;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 5px;
  box-shadow: 0 0 15px 0 rgb(0 0 0 / 13%);
}
.file-menu-group  {
  font-size: 0.8rem;
  padding-left: 10px;
  color: #757575;
}
.file-menu-group .count b,
.file-menu-group .count span {
  color: #409eff;
}
.el-button--large {
  padding: 8px 14px;
  font-size: 17px;
}
.el-button--large .icon {
  margin-right: 5px;
}
.uploaded-list {
  margin: 1rem 0 1rem 1rem;
  display: -webkit-flex;
  display: flex;
  flex-wrap: wrap;
}
</style>

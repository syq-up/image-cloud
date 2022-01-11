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

    <!-- 该部分暂时舍弃
    <div class="success-info">
      <div class="mdui-tab mdui-tab-scrollable">
        <a href="javascript:" @click="showCode('code_url')"
          :class="'mdui-ripple '+ (code.code_url ? 'mdui-tab-active' : '')">URL</a>
        <a href="javascript:" @click="showCode('code_html')"
          :class="'mdui-ripple '+ (code.code_html ? 'mdui-tab-active' : '')">HTML</a>
        <a href="javascript:" @click="showCode('code_markdown')"
          :class="'mdui-ripple '+ (code.code_markdown ? 'mdui-tab-active' : '')">Markdown</a>
        <div class="mdui-tab-indicator" :style="'left:'+code.indicator*160+'px;width:160px;'"></div>
      </div>
      <ul v-show="code.code_url">
        <li data-index="0">https://pic.iqy.ink/2021/12/30/2bcaf9b71f36c.png<i class="copy iconfont icon-copy"></i></li>
        <li data-index="1">https://pic.iqy.ink/2021/12/30/743a11bb9a371.png<i class="copy iconfont icon-copy"></i></li>
      </ul>
      <ul v-show="code.code_html">
        <li data-index="0">&lt;img src="https://pic.iqy.ink/2021/12/30/2bcaf9b71f36c.png" alt="add.png" title="add.png" /&gt;<i class="copy iconfont icon-copy"></i></li>
        <li data-index="1">&lt;img src="https://pic.iqy.ink/2021/12/30/743a11bb9a371.png" alt="add-chapter.png" title="add-chapter.png" /&gt;<i class="copy iconfont icon-copy"></i></li>
      </ul>
      <ul v-show="code.code_markdown">
        <li data-index="0">![add.png](https://pic.iqy.ink/2021/12/30/2bcaf9b71f36c.png)<i class="copy iconfont icon-copy"></i></li>
        <li data-index="1">![add-chapter.png](https://pic.iqy.ink/2021/12/30/743a11bb9a371.png)<i class="copy iconfont icon-copy"></i></li>
      </ul>
    </div> -->
  </div>
</template>

<script>
import { onMounted, reactive, computed } from 'vue';
import { useStore } from 'vuex'
import server from '@/util/request';
import '@/assets/css/element.css'
import PageHeader from '@/components/PageHeader'
import Image from '@/components/Image'

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
      if (file.size > 5*1024*1024)
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
          // TODO 网络图片暂时只在后端计算大小
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

    // 该部分暂时舍弃
    // // 上传成功后，回显每张图片的链接，有3种类型，url、html标签、md标签，每次仅显示一类
    // const code = reactive({
    //   code_url: true,
    //   code_html: false,
    //   code_markdown: false,
    //   indicator: 0,
    // });
    // function showCode(category) {
    //   switch (category) {
    //     case 'code_url':
    //       code.code_url = true;
    //       code.code_html = false;
    //       code.code_markdown = false;
    //       code.indicator = 0;
    //       break;
    //     case 'code_html':
    //       code.code_url = false;
    //       code.code_html = true;
    //       code.code_markdown = false;
    //       code.indicator = 1;
    //       break;
    //     case 'code_markdown':
    //       code.code_url = false;
    //       code.code_html = false;
    //       code.code_markdown = true;
    //       code.indicator = 2;
    //       break;
    //     default:
    //       code.code_url = true;
    //       code.code_html = false;
    //       code.code_markdown = false;
    //       code.indicator = 0;
    //       break;
    //   }
    // }
    // 
    
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
/* .success-info {
  margin: 2rem 1rem 0 1rem;
}
.success-info .mdui-tab-scrollable {
  padding-left: 0;
}
.success-info .mdui-tab-scrollable {
  padding-left: 56px;
}
.success-info .mdui-tab {
  position: relative;
  display: flex;
  height: 48px;
  padding: 0;
  margin: 0 auto;
  overflow-x: auto;
  overflow-y: hidden;
  -webkit-overflow-scrolling: touch;
  white-space: nowrap;
}
.mdui-tab-indicator {
  position: absolute;
  bottom: 0;
  height: 2px;
  background-color: #3f51b5;
  -webkit-transition: all .35s cubic-bezier(.4,0,.2,1);
  transition: all .35s cubic-bezier(.4,0,.2,1);
  will-change: left,width;
}
.success-info .mdui-tab a {
  height: 48px;
  width: 160px;
  line-height: 48px;
  text-transform: inherit;
  text-align:center;
  align-self: center;
  opacity: 0.8;
}
.success-info .mdui-tab .mdui-tab-active {
  color: #3f51b5;
  opacity: 1;
}
.success-info ul {
  list-style: none;
  margin: 0;
  padding-left: 0;
}
.success-info ul li {
  position: relative;
  margin-top: 0.5rem;
  padding: 0.8rem;
  border: 1px solid #dadada;
  background-color: #f7f7f7;
  font-size: 14px;
  color: #555;
  white-space: pre-wrap;
  word-break: break-all;
  word-wrap: break-word;
  border-radius: 0;
} */
</style>

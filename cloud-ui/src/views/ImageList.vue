<template>
  <page-header title="Image List" tip="This contains all the images you uploaded.">
    <template #menu>
      <span v-if="settings.multipleChoice">该功能暂时无效 </span>
      <el-checkbox v-model="settings.multipleChoice" label="Multiple choice" border></el-checkbox>
      <el-checkbox v-model="$store.state.settings.showDateInList" label="Show Date" border
        v-if="!$store.state.settings.folderStyleInList"></el-checkbox>
      <el-checkbox v-model="$store.state.settings.showDividerInList" label="Show Divider" border
        v-if="$store.state.settings.folderStyleInList"></el-checkbox>
      <el-checkbox v-model="$store.state.settings.folderStyleInList" label="Folder Style" border></el-checkbox>
    </template>
  </page-header>
  <div v-infinite-scroll="loadImages" :infinite-scroll-disabled="page.disabled">
    <template v-if="!$store.state.settings.folderStyleInList && $store.state.settings.showDateInList">
      <div v-for="(dateItem, index) in dateList" :key="dateItem">
        <el-divider content-position="left">{{ dateItem }}</el-divider>
        <div class="daily-list">
          <el-space wrap :size="14">
            <yq-image v-for="(item, i) in dailyImgList[index]" :key="item.id" 
              :deleted="false" :imageObj="item" :index="i"
              @lookBigImage="lookBigImage" @deleteByIndex="deleteByIndex"
            ></yq-image>
          </el-space>
        </div>
      </div>
    </template>
    <template v-if="!$store.state.settings.folderStyleInList && !$store.state.settings.showDateInList">
      <div class="daily-list">
        <el-space wrap :size="14">
          <yq-image v-for="item in imageList" :key="item.id" 
            :deleted="false" :imageObj="item" @lookBigImage="lookBigImage" @deleteByIndex="deleteByIndex"
          ></yq-image>
        </el-space>
      </div>
    </template>
    <template v-if="$store.state.settings.folderStyleInList && !$store.state.settings.showDividerInList">
      <div class="daily-list">
        <el-space wrap :size="14">
          <yq-folder v-for="folder in page.folderInCurrentPath" :key="'folder'+folder.id"
            :folderName="folder.name" @toNextPath="toNextPath"></yq-folder>
          <yq-image v-for="image in imageList" :key="image.id"
            :deleted="false" :imageObj="image" @lookBigImage="lookBigImage" @deleteByIndex="deleteByIndex"
          ></yq-image>
        </el-space>
      </div>
    </template>
    <template v-if="$store.state.settings.folderStyleInList && $store.state.settings.showDividerInList">
      <div class="daily-list">
        <el-space wrap :size="14">
          <yq-folder v-for="folder in page.folderInCurrentPath" :key="'folder'+folder.id"
            :folderName="folder.name" @toNextPath="toNextPath"></yq-folder>
        </el-space>
      </div>
      <el-divider content-position="left"><span style="color: #222; font-size: 16px; line-height: 18px; height: 18px;">images</span></el-divider>
      <div class="daily-list">
        <el-space wrap :size="14">
          <yq-image v-for="item in imageList" :key="item.id"
            :deleted="false" :imageObj="item" @lookBigImage="lookBigImage" @deleteByIndex="deleteByIndex"
          ></yq-image>
        </el-space>
      </div>
    </template>
    <loading-heart v-show="page.loading"></loading-heart>
  </div>
  <page-footer v-if="page.disabled"></page-footer>
</template>

<script>
import { reactive, watch } from 'vue';
import { useStore } from 'vuex'
import server from '@/util/request';
import PageHeader from '@/components/PageHeader'
import Folder from '@/components/Folder'
import Image from '@/components/Image'
import LoadingHeart from '@/components/LoadingHeart'
import PageFooter from '@/components/PageFooter'

export default {
  name: 'List',
  components: {
    'page-header': PageHeader,
    'yq-folder': Folder,
    'yq-image': Image,
    'loading-heart': LoadingHeart,
    'page-footer': PageFooter,
  },
  setup() {
    const store = useStore()
    // 渲染图片列表
    const page = reactive({
      loading: true,
      disabled: false,
      currentPage: 0,
      currentPath: [],
      folderInCurrentPath: [],
    })
    const dateList = reactive([])
    const dailyImgList = reactive([])
    const imageList = reactive([])
    // 加载下一页图片
    function loadImages() {
      page.loading = true
      // 参数
      const params = {
        path: store.state.settings.folderStyleInList ? page.currentPath.join('/') : null,
				pageNum: ++page.currentPage
			}
      server.get('/image/getImageList', {params}).then(res => {
        // records.length!==0：当前页非空页，可能存在下一页，对当前页图像数据进行下一步处理
        // records.length===0：当前页为空页，不存在下一页，置disabled=true，不再请求下一页
        if (res.data.records.length!==0) {
					imageList.push(...res.data.records) // 将图像数据插入到imageList，以渲染UI
          // 非文件夹样式时，图像按时间分组显示
          if (!store.state.settings.folderStyleInList) {
            // 逐一对图像进行处理
            for (let img of res.data.records) {
              const len = dateList.length // dateList数组长度
              // len === 0：第一个图像
              // len !== 0：后续图像
              if (len === 0) {
                dateList.push(img.createTime) // 时间插入到dateList
                dailyImgList.push([img])  // 图像先插入到一维数组，再插入到dailyImgList
              } else {
                // 当前图像与前x张上传时间在同一天，则不需要再插入dateList，仅插入dailyImgList中对应的一维数组中
                // 否则，两个数组均需要插入
                if (dateList[len-1] === img.createTime) {
                  dailyImgList[len-1].push(img)
                } else {
                  dateList.push(img.createTime)
                  dailyImgList.push([img])
                }
              }
            }
          }
          // 文件样式时，得到当前路径下的子路径（文件夹）
          else {
            let tempList = pathListToTree(store.state.userInfo.secondaryPathList)
            page.currentPath.forEach(currentPathItem => {
              for (let i=0; i<tempList.length; i++) {
                if (tempList[i].name === currentPathItem) {
                  tempList = tempList[i].children
                  break
                }
              }
            })
            page.folderInCurrentPath = tempList
          }
				} else {
					page.disabled = true
				}
				page.loading = false
      })
    }

    // 点击文件夹进入路径
    function toNextPath(path) {
      // 更新当前路径和当前页数
      page.currentPath.push(path)
      page.currentPage = 0
      // 清空上一级路径的数据
      page.folderInCurrentPath.length = 0
      imageList.length = 0
      // 解除禁用加载
      page.disabled = false
      // 重新加载数据
      loadImages()
    }

    // 监听文件夹样式的切换
    watch(()=>store.state.settings.folderStyleInList, ()=>{
      // 置当前页为0
      page.currentPage = 0
      // 清空图像列表
      dateList.length = 0
      dailyImgList.length = 0
      imageList.length = 0
      page.currentPath.length = 0
      // 解除禁用加载
      page.disabled = false
      // 执行loadImages方法，加载数据
      loadImages()
    })

    // 移除已删除的图像
    function deleteByIndex(id) {
      // 移除imageList中存储的图像
      for (let i=0; i<imageList.length; i++) {
        if (id === imageList[i].id) {
          imageList.splice(i, 1)
          break
        }
      }
      // 移除dailyImgList中存储的图像
      for (let i=0; i<dailyImgList.length; i++) {
        for (let j=0; j<dailyImgList[i].length; j++) {
          if (id === dailyImgList[i][j].id) {
            dailyImgList[i].splice(j, 1)
            // 如果移除后这一天已经没有图像，则需移除这一天
            if (dailyImgList[i].length === 0) {
              dailyImgList.splice(i, 1)
              dateList.splice(i, 1)
            }
            break
          }
        }
      }
    }

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
				for (let name of pathItemList) {
					// 5.同层同名节点查找匹配
					let obj = levelList.find(item => item.name == name)
					// 6.若不存在则建立该节点
					if (!obj) {
						obj = { id: id++, name, children: [] }
						levelList.push(obj)
            // TODO 这里有bug！
						// 7.若当前被增节点是叶子节点，则裁剪该节点子节点属性
						// if (name == pathItemList[pathItemList.length - 1]) {
						// 	delete obj.children
						// }
					}
					// 8.已有则进入下一层，继续寻找
					levelList = obj.children
				}
			})
			return tree
		}

    // Upload Settings
    const settings = reactive({
      multipleChoice: false,
    })

    // 全屏查看图片
    function lookBigImage(id) {
      // 根据查看的图片id找出其在imageList中的下标
      let index = 0
      for (let i=0; i<imageList.length; i++) {
        if (id === imageList[i].id) {
          index = i
          break
        }
      }
      // 打开遮罩层，全屏展示图片
      store.commit('changeShade', true)
      store.commit('changeImageList', imageList)
      store.commit('changeImageListIndex', index)
      // 禁止页面滚动
      document.getElementsByTagName("body")[0].className="ban-scroll"
    }

    return { page, dateList, dailyImgList, imageList, loadImages, toNextPath, deleteByIndex, settings, lookBigImage };
  },
};
</script>

<style scoped>
.current-path span {
  margin-right: 8px;
  height: 18px;
  font-size: 16px;
  line-height: 18px;
  color: #222;
  cursor: pointer;
}
.current-path span:hover {
  color: #409eff;
}
.current-path span::before {
  content: '/';
  margin-right: 8px;
}
.daily-list {
  margin: 0 0 0 1rem;
  display: -webkit-flex;
  display: flex;
  flex-wrap: wrap;
}
</style>

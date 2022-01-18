<template>
	<page-header title="Image Recycle" tip="The deleted images will be kept here for a period of time.">
		<template #menu>
      <span v-if="settings.multipleChoice">该功能暂时无效 </span>
      <el-checkbox v-model="settings.multipleChoice" label="Multiple choice" border></el-checkbox>
      <el-checkbox v-model="$store.state.settings.showDateInRecycle" label="Show Date" border
        v-if="!$store.state.settings.folderStyleInRecycle"></el-checkbox>
      <el-checkbox v-model="$store.state.settings.folderStyleInRecycle" label="Folder Style" border></el-checkbox>
    </template>
	</page-header>
	<div v-infinite-scroll="loadImages" :infinite-scroll-disabled="page.disabled">
    <template v-if="!$store.state.settings.folderStyleInRecycle && $store.state.settings.showDateInRecycle">
      <div v-for="(dateItem, index) in dateList" :key="dateItem">
        <el-divider content-position="left">{{ dateItem }}</el-divider>
        <div class="daily-list">
          <el-space wrap :size="14">
            <yq-image v-for="(item, i) in dailyImgList[index]" :key="item.id" 
              :deleted="true" :imageObj="item" :index="i"
              @lookBigImage="lookBigImage" @restoreByIndex="restoreByIndex"
            ></yq-image>
          </el-space>
        </div>
      </div>
    </template>
    <template v-if="!$store.state.settings.folderStyleInRecycle && !$store.state.settings.showDateInRecycle">
      <div class="daily-list">
        <el-space wrap :size="14">
          <yq-image v-for="item in imageList" :key="item.id" 
            :deleted="true" :imageObj="item" @lookBigImage="lookBigImage" @restoreByIndex="restoreByIndex"
          ></yq-image>
        </el-space>
      </div>
    </template>
    <template v-if="$store.state.settings.folderStyleInRecycle">
      <div class="daily-list">
        <el-space wrap :size="14">
          <yq-folder v-for="folder in page.folderInCurrentPath" :key="'folder'+folder.id"
            :folderName="folder.name" @toNextPath="toNextPath"></yq-folder>
          <yq-image v-for="image in imageList" :key="image.id"
            :deleted="true" :imageObj="image" @lookBigImage="lookBigImage" @restoreByIndex="restoreByIndex"
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
    const store = useStore();
		// Upload Settings
    const settings = reactive({
      multipleChoice: false,
    })

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
    const secondaryPathTree = reactive(pathListToTree(store.state.userInfo.secondaryPathList))  // 次级路径列表（树结构）
    // 加载下一页图片
		function loadImages() {
      page.loading = true
      // 参数
      const params = {
        path: store.state.settings.folderStyleInList ? page.currentPath.join('/') : null,
				pageNum: ++page.currentPage
			}
      server.get('/image/getRecycleList', {params}).then(res => {
        // records.length!==0：当前页非空页，可能存在下一页，对当前页图像数据进行下一步处理
        // records.length===0：当前页为空页，不存在下一页，置disabled=true，不再请求下一页
        if (res.data.records.length!==0) {
					imageList.push(...res.data.records) // 将图像数据插入到imageList，以渲染UI
          // 非文件夹样式时，图像按时间分组显示
          if (!store.state.settings.folderStyleInRecycle) {
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
            let tempList = secondaryPathTree
            page.currentPath.forEach(currentPathItem => {
              for (let i=0; i<tempList.length; i++) {
                if (tempList[i].name === currentPathItem) {
                  tempList = tempList.children
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
      // 重新加载数据
      loadImages()
    }

    // 监听文件夹样式的切换
    watch(()=>store.state.settings.folderStyleInRecycle, (newValue)=>{
      // 置当前页为0
      page.currentPage = 0
      // 清空图像列表
      dateList.length = 0
      dailyImgList.length = 0
      imageList.length = 0
      page.currentPath.length = 0
      secondaryPathTree.length = 0
      // 切换到文件夹样式时，将次级路径列表转化为数结构
      if (newValue) {
        secondaryPathTree.push(...pathListToTree(store.state.userInfo.secondaryPathList))
      }
      // 执行loadImages方法，加载数据
      loadImages()
    })

		// 移除已恢复的图像
		function restoreByIndex(id) {
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

    return { page, dateList, dailyImgList, imageList, loadImages, toNextPath, restoreByIndex, settings, lookBigImage };
  },
};
</script>

<style scoped>
.daily-list {
  margin: 0 0 0 1rem;
  display: -webkit-flex;
  display: flex;
  flex-wrap: wrap;
}
</style>

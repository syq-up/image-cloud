<template>
	<page-header title="Image Recycle" tip="The deleted images will be kept here for a period of time.">
		<template #menu>
      <span v-if="settings.multipleChoice">该功能暂时无效 </span>
      <el-checkbox v-model="settings.multipleChoice" label="Multiple choice" border></el-checkbox>
      <el-checkbox v-model="$store.state.settings.showDateInRecycle" label="Show date tag" border></el-checkbox>
    </template>
	</page-header>
	<div v-infinite-scroll="loadImages" :infinite-scroll-disabled="page.disabled">
    <template v-if="$store.state.settings.showDateInRecycle">
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
    <template v-if="!$store.state.settings.showDateInRecycle">
      <div class="daily-list">
        <el-space wrap :size="14">
          <yq-image v-for="(item, i) in imageList" :key="item.id" 
            :deleted="false" :imageObj="item" :index="i" 
            @lookBigImage="lookBigImage" @deleteByIndex="deleteByIndex"
          ></yq-image>
        </el-space>
      </div>
    </template>
    <loading-heart v-show="page.loading"></loading-heart>
	</div>
	<page-footer v-if="page.disabled"></page-footer>
</template>

<script>
import { reactive } from 'vue';
import { useStore } from 'vuex'
import server from '@/util/request';
import PageHeader from '@/components/PageHeader'
import Image from '@/components/Image'
import LoadingHeart from '@/components/LoadingHeart'
import PageFooter from '@/components/PageFooter'

export default {
  name: 'List',
	components: {
    'page-header': PageHeader,
    'yq-image': Image,
    'loading-heart': LoadingHeart,
    'page-footer': PageFooter,
  },
  setup() {
		// Upload Settings
    const settings = reactive({
      multipleChoice: false,
    })

    // 渲染图片列表
    const page = reactive({
      loading: true,
      disabled: false,
      current: 0,
    })
    const dateList = reactive([])
    const dailyImgList = reactive([])
    const imageList = reactive([])
		function loadImages() {
      server.get('/image/getRecycleList/' + (++page.current)).then(res => {
        // records.length!==0：当前页非空页，可能存在下一页，对当前页图像数据进行下一步处理
        // records.length===0：当前页为空页，不存在下一页，置disabled=true，不再请求下一页
        if (res.data.records.length!==0) {
					imageList.push(...res.data.records) // 将图像数据插入到imageList，以渲染UI
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
				} else {
					page.disabled = true
				}
				page.loading = false
      })
    }
		// 移除已恢复的图像
		function restoreByIndex(index) {
			// 移除imageList中存储的图像
      imageList.splice(index, 1)
      // 移除dailyImgList中存储的图像
      for (let i=0; i<dailyImgList.length; i++) {
        index -= dailyImgList[i].length
        if (index < 0) {
          dailyImgList[i].splice(index + dailyImgList[i].length, 1)
          // 如果移除后这一天已经没有图像，则需移除这一天
          if (dailyImgList[i].length === 0) {
            dailyImgList.splice(i, 1)
            dateList.splice(i, 1)
          }
          break
        }
      }
		}

    // 全屏查看图片
    const store = useStore();
    function lookBigImage(index) {
      store.commit('changeShade', true);
      store.commit('changeImageList', imageList);
      store.commit('changeImageListIndex', index);
      document.getElementsByTagName("body")[0].className="ban-scroll";
    }
    return { page, dateList, dailyImgList, imageList, loadImages, restoreByIndex, settings, lookBigImage };
  },
};
</script>

<style scoped>
.daily-list {
  margin: 0 1rem;
  display: -webkit-flex;
  display: flex;
  flex-wrap: wrap;
}
.el-checkbox {
  margin-right: 15px;
}
</style>

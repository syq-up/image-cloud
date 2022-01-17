<template>
	<div class="title">
    <div class="left">
      <span>{{ title }}</span>
      <span>[ {{ tip }} ]</span>
    </div>
    <div class="right">
      <el-space :size="15">
        <slot name="menu"></slot>
      </el-space>
    </div>
  </div>
</template>

<script>
import '@/assets/css/el-space.css'

export default {
  name: 'PageHeader',
  props: {
    title: {
      type: String,
      required: true,
    },
    tip: {
      type: String,
      default: '……',
    },
  },
  setup() {
    
    return { }
  }
};
</script>

<style scoped>
.title {
  margin: 1rem;
  display: -webkit-flex;
  display: flex;
  justify-content: space-between;
}
.title .left span:first-child {
  font-weight: 300;
  font-size: 2rem;
  text-shadow: -5px 5px 0 rgb(0 0 0 / 10%);
  margin-right: 0.5rem;
}
.title .left span:last-child {
  margin-top: 1rem;
  font-size: 0.8rem;
  color: #999;
}
.title .right {
  align-self: center;
  display: -webkit-flex;
  display: flex;
}
</style>
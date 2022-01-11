<template>
  <router-view/>
</template>

<style>
html, body {
  margin: 0;
  padding: 0;
}
a, a:focus, a:hover {
    cursor: pointer;
    color: inherit;
    text-decoration: none;
}
.ban-scroll {
  overflow-x: hidden;
  overflow-y: hidden;
}
/* 鼠标触碰时动画 */
/* Grow */
.hvr-grow {
  display: inline-block;
  vertical-align: middle;
  -webkit-transform: perspective(1px) translateZ(0);
  transform: perspective(1px) translateZ(0);
  box-shadow: 0 0 1px rgba(0, 0, 0, 0);
  -webkit-transition-duration: 0.3s;
  transition-duration: 0.3s;
  -webkit-transition-property: transform;
  transition-property: transform;
  cursor: pointer;
}
.hvr-grow:hover, .hvr-grow:focus, .hvr-grow:active {
  -webkit-transform: scale(1.2);
  transform: scale(1.2);
}
</style>

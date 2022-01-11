<template>
	<div class="loading">
    <div class="split-gap"></div>
    <div class="heart">
      <span></span><span></span><span></span>
      <span></span><span></span><span></span>
      <span></span><span></span><span></span>
    </div>
    <div class="split-gap"></div>
  </div>
</template>

<script>
export default {
  name: 'LoadingHeart',
}
</script>

<style scoped>
.loading {
  margin: 2rem 1rem;
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  grid-template-areas: '. heart .';
  grid-auto-flow: column;
  align-items: center;
  justify-content: center;
}
.split-gap {
  box-sizing: border-box;
  border-bottom: 1px solid #e6e6e6;
}
.heart {
  grid-column-start: heart-start;
  grid-column-end: heart-end;
  grid-row-start: heart-start;
  grid-row-end: heart-end;
  width: 210px;
  height: 165px;
  display: -webkit-flex;
  display: flex;
  justify-content: space-between;
  transform: scale(0.5);
}
.heart span {
  --c: plum;
  --h: 50%;
  --t: 25%;
  background-color: var(--c);
  width: 15px;
  height: var(--h);
  border-radius: 8px;
  position: relative;
  top: var(--t);
  animation: beating 1s infinite;
}
.heart span:nth-child(1),
.heart span:nth-child(9) {
  --c: lightcoral;
  --h: 45px;
  --t: 33px;
}
.heart span:nth-child(2),
.heart span:nth-child(8) {
  --c: lightskyblue;
  --h: 90px;
  --t: 9px;
}
.heart span:nth-child(3),
.heart span:nth-child(7) {
  --c: lightgreen;
  --h: 120px;
  --t: 0;
}
.heart span:nth-child(4),
.heart span:nth-child(6) {
  --c: gold;
  --h: 135px;
  --t: 12px;
}
.heart span:nth-child(5) {
  --c: plum;
  --h: 141px;
  --t: 24px;
}
@keyframes beating {
  0%,30% {
    height: var(--h);
    top: var(--t);
    background-color: var(--c);
    filter: blur(0);
  }
  60%,70% {
    height: 50%;
    top: 25%;
    background-color: plum;
    filter: blur(3px);
  }
}
</style>
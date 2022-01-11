<template>
	<div class="heart">
    <ul>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'LoadingHeart2',
}
</script>

<style scoped>
.heart {
  display: -webkit-flex;
  display: flex;
  height: 100vh;
  align-items: center;
  justify-content: center;
  background-color: #232e6db0;
}
.heart * {
  margin: 0;
  padding: 0;
  list-style-type: none;
}
.heart ul>li {
  width: 20px;
  height: 20px;
  background-color: coral;
  border-radius: 20px;
  float: left;
  margin-left: 20px;
}
.heart ul>li:nth-child(1) {
  background-color: #f62e74;
  animation: heart1 2.5s linear infinite;
  animation-delay: 0s;
}
.heart ul>li:nth-child(2) {
  background-color: #f45330;
  animation: heart2 2.5s linear infinite;
  animation-delay: 0.06s;
}
.heart ul>li:nth-child(3) {
  background-color: #ffc883;
  animation: heart3 2.5s linear infinite;
  animation-delay: 0.12s;
}
.heart ul>li:nth-child(4) {
  background-color: #30d268;
  animation: heart4 2.5s linear infinite;
  animation-delay: 0.18s;
}
.heart ul>li:nth-child(5) {
  background-color: #006cb4;
  animation: heart5 2.5s linear infinite;
  animation-delay: 0.24s;
}
.heart ul>li:nth-child(6) {
  background-color: #30d268;
  animation: heart4 2.5s linear infinite;
  animation-delay: 0.30s;
}
.heart ul>li:nth-child(7) {
  background-color: #ffc883;
  animation: heart3 2.5s linear infinite;
  animation-delay: 0.36s;
}
.heart ul>li:nth-child(8) {
  background-color: #f45330;
  animation: heart2 2.5s linear infinite;
  animation-delay: 0.42s;
}
.heart ul>li:nth-child(9) {
  background-color: #f62e74;
  animation: heart1 2.5s linear infinite;
  animation-delay: 0.48s;
}
@keyframes heart1 {
  35%, 50% {
    height: 60px;
    transform: translateY(-30px);
  }
  80%, 100% {
    height: 20px;
    transform: translateY(0);
  }
}
@keyframes heart2 {
  35%, 50% {
    height: 120px;
    transform: translateY(-60px);
  }
  80%, 100% {
    height: 20px;
    transform: translateY(0);
  }
}
@keyframes heart3 {
  35%, 50% {
    height: 160px;
    transform: translateY(-75px);
  }
  80%, 100% {
    height: 20px;
    transform: translateY(0);
  }
}
@keyframes heart4 {
  35%, 50% {
    height: 180px;
    transform: translateY(-60px);
  }
  80%, 100% {
    height: 20px;
    transform: translateY(0);
  }
}
@keyframes heart5 {
  35%, 50% {
    height: 190px;
    transform: translateY(-45px);
  }
  80%, 100% {
    height: 20px;
    transform: translateY(0);
  }
}
</style>
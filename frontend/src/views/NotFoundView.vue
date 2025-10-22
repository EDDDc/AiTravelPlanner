<template>
  <div class="not-found">
    <h1>404</h1>
    <p>抱歉，你访问的页面不存在或已经被移动。</p>
    <RouterLink to="/" class="home-link">返回首页</RouterLink>
  </div>
</template>

<style scoped>
.not-found {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  padding: 80px 0;
  color: #374151;
}

h1 {
  font-size: 72px;
  margin: 0;
  font-weight: 700;
  color: #5271ff;
}

.home-link {
  color: #5271ff;
  font-weight: 600;
}
</style>

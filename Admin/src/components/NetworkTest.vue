<template>
  <div class="network-test">
    <h2>网络测试</h2>
    <div class="test-controls">
      <el-button type="primary" @click="testConnection">测试API连接</el-button>
      <el-button type="info" @click="testWithToken">测试带Token的请求</el-button>
    </div>
    <div class="test-results" v-if="testResult">
      <el-alert
        :title="testResult.success ? '连接成功' : '连接失败'"
        :type="testResult.success ? 'success' : 'error'"
        :description="testResult.message"
        show-icon
      ></el-alert>
      <pre v-if="testResult.data">{{ JSON.stringify(testResult.data, null, 2) }}</pre>
    </div>
  </div>
</template>

<script>
import { getCurrentInstance, ref } from 'vue'

export default {
  name: 'NetworkTest',
  setup() {
    const { proxy } = getCurrentInstance()
    const testResult = ref(null)

    const testConnection = async () => {
      testResult.value = null
      try {
        const response = await proxy.$http.get('/api/test')
        testResult.value = {
          success: true,
          message: 'API连接成功!',
          data: response.data
        }
      } catch (error) {
        console.error('API连接测试失败:', error)
        testResult.value = {
          success: false,
          message: `API连接失败: ${error.message}`,
          data: error.response?.data
        }
      }
    }

    const testWithToken = async () => {
      testResult.value = null
      const token = window.sessionStorage.getItem('token')
      if (!token) {
        testResult.value = {
          success: false,
          message: '没有找到有效的token，请先登录'
        }
        return
      }

      try {
        // 使用当前token发起请求
        const response = await proxy.$http.get('/api/protected-endpoint')
        testResult.value = {
          success: true,
          message: '带Token的API请求成功!',
          data: response.data
        }
      } catch (error) {
        console.error('带Token的API请求失败:', error)
        testResult.value = {
          success: false,
          message: `带Token的API请求失败: ${error.message}`,
          data: error.response?.data
        }
      }
    }

    return {
      testResult,
      testConnection,
      testWithToken
    }
  }
}
</script>

<style scoped>
.network-test {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.test-controls {
  margin: 20px 0;
}

.test-controls button {
  margin-right: 10px;
}

.test-results {
  margin-top: 20px;
}

pre {
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 5px;
  overflow: auto;
  margin-top: 10px;
}
</style> 
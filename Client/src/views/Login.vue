<template>
    <div class="login_container">
        <div class="login_box">
            <!-- 头像区域 -->
            <div class="avatar_box">
                <img src="../assets/user.png" alt="">
            </div>

            <!-- 登录表单区域 -->
            <el-form ref="loginFormRef" class="login_form" :rules="loginFormRules" :model="loginForm">
                <!-- 用户名 -->
                <el-form-item prop="username">
                    <el-input v-model="loginForm.username" placeholder="请输入用户名">
                        <template #prefix>
                            <i class="iconfont icon-user"></i>
                        </template>
                    </el-input>
                </el-form-item>
                <!-- 密码 -->
                <el-form-item prop="password">
                    <el-input v-model="loginForm.password" type="password" placeholder="请输入密码">
                        <template #prefix>
                            <i class="iconfont icon-3702mima"></i>
                        </template>
                    </el-input>
                </el-form-item>
                <!-- 登录按钮 -->
                <el-form-item class="btns">
                    <el-button type="primary" @click="login">登录</el-button>
                    <el-button type="info" @click="resetLoginForm">重置</el-button>
                </el-form-item>
                <!-- 注册链接 -->
                <div class="register-link">
                    <a @click="goToRegister">没有账号？立即注册</a>
                </div>
            </el-form>
        </div>
    </div>
</template>

<script>
import { getCurrentInstance } from 'vue'
import { useRouter } from 'vue-router'

export default {
    setup() {
        const router = useRouter()
        const { proxy } = getCurrentInstance()
        
        return {
            router,
            proxy
        }
    },
    data () {
        return {
            // 登陆表单的数据绑定对象
            loginForm: {
                username: '',
                password: ''
            },
            // 这是表单的验证规则对象
            loginFormRules: {
                // 验证用户名是否合法
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
                ],
                // 验证密码是否合法
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 5, max: 16, message: '密码长度在 5 到 16 个字符', trigger: 'blur' }
                ]
            }
        }
    },
    methods: {
        // 点击重置按钮，重置登录表单
        resetLoginForm () {
            this.$refs.loginFormRef.resetFields()
        },
        login () {
            this.$refs.loginFormRef.validate(async valid => {
                if (!valid) return
                try {
                    console.log("Attempting client login...")
                    const response = await this.proxy.$http.post('/client/login', this.loginForm)
                    console.log('Login response:', response.data) // 调试输出
                    
                    const res = response.data
                    if (res.code !== 100) {
                        this.proxy.$message.error('登录失败，用户名或密码错误！')
                        return
                    }
                    
                    // 只有在响应码为100时才显示登录成功
                    if (!res.extend || !res.extend.token) {
                        this.proxy.$message.error('登录响应中未包含有效令牌！')
                        console.error('Token未找到，响应数据:', res)
                        return
                    }
                    
                    // 如果验证通过并且有token，才显示登录成功
                    this.proxy.$message.success('登录成功')
                    
                    // 保存token
                    window.sessionStorage.setItem('token', res.extend.token)
                    
                    // 跳转到主页
                    this.router.push('/home')
                } catch (error) {
                    console.error('登录请求错误:', error)
                    this.proxy.$message.error('登录请求失败，请检查网络连接或服务器状态')
                }
            })
        },
        goToRegister() {
            this.router.push('/register')
        }
    }
}
</script>

<style lang="less" scoped>
.login_container{
    background-color: #2b4b6b;
    height: 100%;
}

.login_box{
    width: 450px;
    height: 300px;
    background-color: #fff;
    border-radius: 3px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);

    .avatar_box{
        height: 130px;
        width: 130px;
        background-color: #fff;
        border: 1px solid #eee;
        border-radius: 50%;
        padding: 10px;
        position: absolute;
        transform: translate(-50%, -50%);
        left: 50%;
        img{
            width: 100%;
            height: 100%;
            background-color: #eee;
            border-radius: 50%;
        }
    }
}

.login_form{
    position: absolute;
    bottom: 0;
    width: 100%;
    padding: 0 20px;
    box-sizing: border-box;
}

.btns{
    display: flex;
    justify-content: flex-end;
}

.register-link {
    text-align: center;
    margin-top: -15px;
    margin-bottom: 10px;
    
    a {
        color: #409EFF;
        cursor: pointer;
        text-decoration: none;
        
        &:hover {
            text-decoration: underline;
        }
    }
}
</style>

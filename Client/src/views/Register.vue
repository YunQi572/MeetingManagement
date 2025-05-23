<template>
    <div class="register_container">
        <div class="register_box">
            <div class="title">
                <h2>用户注册</h2>
            </div>

            <!-- 注册表单区域 -->
            <el-form ref="registerFormRef" class="register_form" :model="registerForm" :rules="registerFormRules">
                <!-- 用户名 -->
                <el-form-item prop="username">
                    <el-input v-model="registerForm.username" placeholder="请输入用户名">
                        <template #prefix>
                            <i class="iconfont icon-user"></i>
                        </template>
                    </el-input>
                </el-form-item>
                <!-- 密码 -->
                <el-form-item prop="password">
                    <el-input v-model="registerForm.password" type="password" placeholder="请输入密码">
                        <template #prefix>
                            <i class="iconfont icon-3702mima"></i>
                        </template>
                    </el-input>
                </el-form-item>
                <!-- 确认密码 -->
                <el-form-item prop="confirmPassword">
                    <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请确认密码">
                        <template #prefix>
                            <i class="iconfont icon-3702mima"></i>
                        </template>
                    </el-input>
                </el-form-item>
                <!-- 邮箱 -->
                <el-form-item prop="email">
                    <el-input v-model="registerForm.email" placeholder="请输入邮箱">
                        <template #prefix>
                            <i class="iconfont icon-email"></i>
                        </template>
                    </el-input>
                </el-form-item>
                <!-- 手机号 -->
                <el-form-item prop="phone">
                    <el-input v-model="registerForm.phone" placeholder="请输入手机号">
                        <template #prefix>
                            <i class="iconfont icon-phone"></i>
                        </template>
                    </el-input>
                </el-form-item>
                <!-- 按钮 -->
                <el-form-item class="btns">
                    <el-button type="primary" @click="register">注册</el-button>
                    <el-button type="info" @click="resetRegisterForm">重置</el-button>
                </el-form-item>
                <!-- 登录链接 -->
                <div class="login-link">
                    <a @click="goToLogin">已有账号？立即登录</a>
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
    data() {
        // 自定义验证规则
        const validateConfirmPassword = (rule, value, callback) => {
            if (value !== this.registerForm.password) {
                callback(new Error('两次输入的密码不一致!'))
            } else {
                callback()
            }
        }
        
        return {
            // 注册表单的数据绑定对象
            registerForm: {
                username: '',
                password: '',
                confirmPassword: '',
                email: '',
                phone: ''
            },
            // 注册表单的验证规则对象
            registerFormRules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 5, max: 16, message: '密码长度在 5 到 16 个字符', trigger: 'blur' }
                ],
                confirmPassword: [
                    { required: true, message: '请确认密码', trigger: 'blur' },
                    { validator: validateConfirmPassword, trigger: 'blur' }
                ],
                email: [
                    { required: true, message: '请输入邮箱', trigger: 'blur' },
                    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
                ],
                phone: [
                    { required: true, message: '请输入手机号', trigger: 'blur' },
                    { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' }
                ]
            }
        }
    },
    methods: {
        // 点击重置按钮，重置注册表单
        resetRegisterForm() {
            this.$refs.registerFormRef.resetFields()
        },
        // 点击注册按钮
        register() {
            this.$refs.registerFormRef.validate(async valid => {
                if (!valid) return
                
                // 准备要提交的数据，移除确认密码字段
                const registerData = {
                    username: this.registerForm.username,
                    password: this.registerForm.password,
                    email: this.registerForm.email,
                    phone: this.registerForm.phone
                }
                
                try {
                    const response = await this.proxy.$http.post('/client/register', registerData)
                    const res = response.data
                    
                    if (res.code === 100) {
                        this.proxy.$message.success('注册成功，请登录')
                        this.router.push('/login')
                    } else {
                        this.proxy.$message.error(res.message || '注册失败，请稍后重试')
                    }
                } catch (error) {
                    console.error('注册请求错误:', error)
                    this.proxy.$message.error('注册失败，请检查网络连接或服务器状态')
                }
            })
        },
        goToLogin() {
            this.router.push('/login')
        }
    }
}
</script>

<style lang="less" scoped>
.register_container {
    background-color: #2b4b6b;
    height: 100%;
}

.register_box {
    width: 500px;
    background-color: #fff;
    border-radius: 3px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    padding: 20px;
    
    .title {
        text-align: center;
        margin-bottom: 20px;
        
        h2 {
            color: #303133;
            font-weight: 500;
        }
    }
}

.register_form {
    width: 100%;
    padding: 0 20px;
    box-sizing: border-box;
}

.btns {
    display: flex;
    justify-content: flex-end;
}

.login-link {
    text-align: center;
    margin-top: 10px;
    
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
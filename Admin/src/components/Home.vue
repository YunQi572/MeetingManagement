<template>
  <el-container class="home-container">
    <!-- 头部区域 -->
    <el-header>
      <div>
        <img src="../assets/meeting.png" alt />
        <span>会议室后台管理系统</span>
      </div>
      <el-button type="info" @click="logout">退出</el-button>
    </el-header>
    <!-- 页面主体区域 -->
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <div class="toggle-button" @click="toggleCollapse">|||</div>
        <!-- 侧边栏菜单区域 -->
        <el-menu :default-active="activePath" :router="true" :collapse-transition="false" :collapse="isCollapse" :unique-opened="true" background-color="#333744" text-color="#fff" active-text-color="#409eff">
          <!-- 一级菜单 -->
          <el-sub-menu :index="item.id + ''" v-for="item in menulist" :key="item.id">
            <!-- 一级菜单的模板 -->
            <template #title>
              <!-- 图标 -->
              <el-icon>
                <component :is="iconsObj[item.id]" />
              </el-icon>
              <!-- 文本 -->
              <span>{{ item.authName }}</span>
            </template>
            <!-- 二级菜单 -->
            <el-menu-item @click="saveNavState('/' + subItem.path)" :index="'/' + subItem.path" v-for="subItem in item.children" :key="subItem.id">
                <template #title>
                    <!-- 图标 -->
                    <el-icon>
                      <component :is="iconsObj[subItem.id]" />
                    </el-icon>
                    <!-- 文本 -->
                    <span>{{ subItem.authName }}</span>
                </template>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <!-- 右侧内容 -->
      <el-main>
        <!-- 路由占位符 -->
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { useRouter } from 'vue-router'
import { 
  OfficeBuilding, 
  User, 
  Check, 
  Document, 
  Timer,  
  Date as DateIcon, 
  Folder, 
  FolderChecked 
} from '@element-plus/icons-vue'

export default {
    setup() {
        const router = useRouter()
        return { 
          router,
          OfficeBuilding,
          User,
          Check,
          Document,
          Timer,
          DateIcon,
          Folder,
          FolderChecked
        }
    },
    data () {
        return {
            // 左侧菜单数据
            menulist: [{
                id: 101,
                authName: '会议室管理',
                path: 'null',
                children: [
                    { id: 1011, authName: '会议室信息', path: 'meetingRooms' },
                    { id: 1012, authName: '会议室历史会议', path: 'meetingRoomHistory' }
                ]
                },
                {
                id: 102,
                authName: '部门管理',
                path: 'null',
                children: [
                    { id: 1021, authName: '部门信息', path: 'department' },
                    { id: 1022, authName: '部门历史会议', path: 'depHistory' }
                ]
                },
                {
                id: 103,
                authName: '审批申请',
                path: 'null',
                children: [
                    { id: 1031, authName: '已审批', path: 'approved' },
                    { id: 1032, authName: '未审批', path: 'notApproved' }
                ]
                }],
            iconsObj: {
                101: OfficeBuilding,
                102: User,
                103: Check,
                1011: Document,
                1012: Timer,
                1021: User,
                1022: DateIcon,
                1032: Folder,
                1031: FolderChecked
            },
            isCollapse: false,
            activePath: ''
        }
    },
    created () {
        this.activePath = window.sessionStorage.getItem('activePath')
    },
    methods: {
        logout () {
            window.sessionStorage.clear()
            this.router.push('/login')
        },
        // 点击按钮,切换菜单的折叠与展开
        toggleCollapse () {
            this.isCollapse = !this.isCollapse
        },
        // 保存链接的激活状态
        saveNavState (activePath) {
          window.sessionStorage.setItem('activePath', activePath)
          this.activePath = activePath
        }
    }
}
</script>

<style lang="less" scoped>
.home-container {
  height: 100%;
  background-color: var(--background-color);
}

.el-header {
  background: linear-gradient(135deg, #1e88e5 0%, #1565c0 100%);
  display: flex;
  justify-content: space-between;
  padding: 0 20px;
  align-items: center;
  color: #fff;
  font-size: 20px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
  transition: all 0.3s;
  > div {
    display: flex;
    align-items: center;
    height: 100%;
    img {
      height: 40px;
      margin-right: 15px;
      transition: all 0.3s;
      &:hover {
        transform: scale(1.1);
      }
    }
    span {
      margin-left: 15px;
      font-weight: 600;
      letter-spacing: 1px;
    }
  }
  .el-button {
    background-color: rgba(255, 255, 255, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.2);
    color: #fff;
    transition: all 0.3s;
    &:hover {
      background-color: rgba(255, 255, 255, 0.2);
      transform: translateY(-2px);
    }
  }
}

.el-aside {
  background-color: #fff;
  box-shadow: 2px 0 8px 0 rgba(29,35,41,.05);
  transition: all 0.3s;
  .toggle-button {
    background-color: #f5f7fa;
    cursor: pointer;
    color: var(--text-primary);
    font-size: 12px;
    line-height: 24px;
    text-align: center;
    letter-spacing: 0.2em;
    transition: all 0.3s;
    &:hover {
      background-color: #e6e8eb;
    }
  }
}

.el-menu {
  border-right: none;
  .el-submenu__title {
    &:hover {
      background-color: #f5f7fa !important;
    }
  }
  .el-menu-item {
    &:hover {
      background-color: #f5f7fa !important;
    }
    &.is-active {
      background-color: #ecf5ff !important;
      color: var(--primary-color) !important;
      font-weight: 600;
    }
  }
}

.el-main {
  padding: 20px;
  background-color: var(--background-color);
  .router-view {
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
    padding: 20px;
    min-height: calc(100vh - 140px);
  }
}

.iconfont {
  margin-right: 10px;
  font-size: 18px;
  vertical-align: middle;
}

// 添加响应式布局
@media screen and (max-width: 1366px) {
  .el-header {
    padding: 0 15px;
    > div {
      img {
        height: 35px;
      }
      span {
        font-size: 18px;
      }
    }
  }
  .el-main {
    padding: 15px;
  }
}
</style>

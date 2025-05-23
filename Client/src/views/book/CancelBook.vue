<template>
    <div>
        <!-- 面包屑导航区 -->
        <el-breadcrumb :separator-icon="ArrowRight">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>申请会议室</el-breadcrumb-item>
            <el-breadcrumb-item>取消预约</el-breadcrumb-item>
        </el-breadcrumb>

        <!-- 卡片视图 -->
        <el-card>
            <el-table :data="applicationList" border stripe>
                <el-table-column type="index"></el-table-column>
                <el-table-column label="申请部门" prop="deptName"></el-table-column>
                <el-table-column label="会议室" prop="roomNo"></el-table-column>
                <el-table-column label="会议主题" prop="meetingTheme"></el-table-column>
                <el-table-column label="开会日期">
                    <template #default="scope">
                        <div>
                            {{ moment(scope.row.applyDate).format('YYYY/MM/DD') }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="开会时间">
                    <template #default="scope">
                        <div v-if="scope.row.applySlot == 1">08:00-09:00</div>
                        <div v-if="scope.row.applySlot == 2">09:00-10:00</div>
                        <div v-if="scope.row.applySlot == 3">10:00-11:00</div>
                        <div v-if="scope.row.applySlot == 4">11:00-12:00</div>
                        <div v-if="scope.row.applySlot == 5">12:00-13:00</div>
                        <div v-if="scope.row.applySlot == 6">13:00-14:00</div>
                        <div v-if="scope.row.applySlot == 7">14:00-15:00</div>
                        <div v-if="scope.row.applySlot == 8">15:00-16:00</div>
                        <div v-if="scope.row.applySlot == 9">16:00-17:00</div>
                        <div v-if="scope.row.applySlot == 10">17:00-18:00</div>
                        <div v-if="scope.row.applySlot == 11">18:00-19:00</div>
                        <div v-if="scope.row.applySlot == 12">19:00-20:00</div>
                        <div v-if="scope.row.applySlot == 13">20:00-21:00</div>
                        <div v-if="scope.row.applySlot == 14">21:00-22:00</div>
                    </template>
                </el-table-column>
                <el-table-column label="申请日期">
                    <template #default="scope">
                        <div>
                            {{ moment(scope.row.applyTime).format('YYYY/MM/DD') }}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template #default="scope">
                        <!-- 删除按钮 -->
                        <el-tooltip class="item" effect="dark" content="取消预约" placement="top" :enterable="false">
                            <el-button type="danger" size="small" @click="remove(scope.row.applicationId)">
                                <el-icon><Delete /></el-icon>
                            </el-button>
                        </el-tooltip>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页区域 -->
            <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="queryInfo.currentPage"
            :page-sizes="[2, 4, 8, 10]"
            :page-size="queryInfo.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
            </el-pagination>
        </el-card>
    </div>
</template>

<script setup>
import { ArrowRight, Delete } from '@element-plus/icons-vue'
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'
import moment from 'moment'

const applicationList = ref([])
const total = ref(0)

const queryInfo = reactive({
    pageSize: 4,
    currentPage: 1,
    auditStatus: 0
})

const getList = async () => {
    const { data: res } = await axios.get('/api/meeting/history', { params: queryInfo })
    if (res.code !== 100) {
        return ElMessage.error('获取申请列表失败！')
    }
    applicationList.value = res.extend.result.list
    total.value = res.extend.result.total
    console.log(res)
}

const handleSizeChange = (newSize) => {
    queryInfo.pageSize = newSize
    getList()
}

const handleCurrentChange = (newPage) => {
    queryInfo.currentPage = newPage
    getList()
}

const remove = async (id) => {
    try {
        const confirmResult = await ElMessageBox.confirm(
            '此操作将删除本次预约, 是否继续?',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }
        )
        
        if (confirmResult !== 'confirm') {
            return ElMessage.info('已取消删除')
        }
        
        const { data: res } = await axios.delete('/api/apply/revoke', { data: { applicationId: id } })
        if (res.code !== 100) {
            return ElMessage.error('取消预约失败！')
        }
        ElMessage.success('取消预约成功！')
        getList()
    } catch (error) {
        ElMessage.info('已取消删除')
    }
}

// Initialize data on component mount
onMounted(() => {
    getList()
})
</script>

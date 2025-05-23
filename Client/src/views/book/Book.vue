<template>
    <div>
        <!-- 面包屑导航区 -->
        <el-breadcrumb :separator-icon="ArrowRight">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>申请会议室</el-breadcrumb-item>
            <el-breadcrumb-item>进行预约</el-breadcrumb-item>
        </el-breadcrumb>

        <!-- 卡片视图 -->
        <el-card>
            <div class="resRoomPluginBox">
                <div class="table-header">
                    <h3>会议室预约表</h3>
                </div>
                <div class="resHead">
                    <div class="identifyLst">
                        <li class="identify" v-for="(item, index) in identifyLst" :key="index">
                            <i :class="{ 'nooccupy':index == 1, 'select':index == 2 }"></i><span>{{ item }}</span>
                        </li>
                    </div>
                </div>
                
                <!-- 日期选择器 -->
                <div class="date-selector">
                    <div class="date-selector-header">焦点日期选择:</div>
                    <div class="date-cells-container">
                        <div class="date-cell-item" 
                            v-for="(date, key) in dateLst" 
                            :class="{'active':date.active}" 
                            @click="changeDate(key)" 
                            :key="key">
                            <div class="date-content">
                                <div class="date-week">{{date.week}}</div>
                                <div class="date-day">{{date.displayDate}}</div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- 会议室日程表区域 -->
                <div v-if="roomList.length > 0" class="rooms-schedule-area">
                    <div v-for="room_item in roomList" :key="room_item.roomId" class="room-schedule-container">
                        <h4>{{ room_item.roomNo }} ({{ room_item.roomSize }}人)</h4>
                        <table class="room-calendar-grid">
                            <thead>
                                <tr>
                                    <th class="time-col-header">时间</th>
                                    <th v-for="date_col_item in dateLst" :key="date_col_item.date"
                                        :class="{ 'active-date-column': date_col_item.date === queryInfo.applyDate }"
                                        class="date-col-header-cell">
                                        {{ date_col_item.displayDate }} <br/> {{ date_col_item.week.substring(2) }}
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="(hour_item, hour_idx) in hourList" :key="hour_item.full">
                                    <td class="time-row-header-cell">{{ hour_item.full }}</td>
                                    <td v-for="(date_col_item, date_idx) in dateLst" :key="date_col_item.date"
                                        class="schedule-grid-cell"
                                        :class="getCellClassForRoom(room_item, hour_idx, date_col_item)"
                                        @click="selectSlotInGrid(room_item, hour_idx, date_col_item)">
                                        <!-- 状态可以通过背景色和伪元素指示，或留空 -->
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <el-empty v-else description="暂无会议室数据，请尝试选择其他日期或调整分页。" class="empty-rooms"></el-empty>
                
                <!-- 分页区域 -->
                <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="queryInfo.currentPage"
                    :page-sizes="[1, 2, 3, 5, 10]"
                    :page-size="queryInfo.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
                    class="pagination-bar">
                </el-pagination>
            </div>

            <el-form ref="formRef" :rules="selectRule" :model="selectedInfo" label-width="100px" class="booking-form">
                <el-form-item label="会议室：" size="mini">
                    <el-input v-model="selectedInfo.roomNo" disabled></el-input>
                </el-form-item>
                <el-form-item label="会议室设备：" size="mini">
                    <div class="equipment-status">
                        <p class="status-info">（以下为会议室设备状态显示，不可交互操作）</p>
                        <div class="tag">
                            <el-tag type="success" v-if="selectedInfo.air === true">空调可用</el-tag>
                            <el-tag type="danger" v-else-if="selectedInfo.air === false">空调不可用</el-tag>
                            <el-tag type="info" v-else>空调状态</el-tag>
                        </div>
                        <div class="tag">
                            <el-tag type="success" v-if="selectedInfo.projector === true">投影仪可用</el-tag>
                            <el-tag type="danger" v-else-if="selectedInfo.projector === false">投影仪不可用</el-tag>
                            <el-tag type="info" v-else>投影仪状态</el-tag>
                        </div>
                    </div>
                </el-form-item>
                <el-form-item label="开会日期：" size="mini">
                    <el-input v-model="selectedInfo.applyDate" disabled></el-input>
                </el-form-item>
                <el-form-item label="开会时间：" size="mini">
                    <el-input v-model="selectedInfo.displaySlot" disabled></el-input>
                </el-form-item>
                <el-form-item label="会议主题：" prop="meetingTheme">
                    <el-input type="textarea" placeholder="请输入会议主题，不超过15个字符。" v-model="selectedInfo.meetingTheme"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submit()">提交申请</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { ArrowRight } from '@element-plus/icons-vue'
import axios from 'axios'
import moment from 'moment'

const formRef = ref(null)

const selectedInfo = reactive({
    applyDate: '',
    meetingTheme: '',
    roomNo: '',
    applySlot: null, // Use null for no selection
    roomId: '',
    displaySlot: '',
    roomSize: '',
    air: '',
    projector: ''
})

const queryInfo = reactive({
    pageSize: 3, // Show fewer rooms if each has a large grid
    currentPage: 1,
    applyDate: ''
})

const total = ref(0)
// const selectedId = ref('') // Replaced by selectedInfo.roomId
// const selectedSlot = ref('') // Replaced by selectedInfo.applySlot

const identifyLst = ref(['被占用', '空闲', '选择中'])
const dateLst = ref([])
const hourList = ref([
    '08:00-09:00', '09:00-10:00', '10:00-11:00', '11:00-12:00', 
    '12:00-13:00', '13:00-14:00', '14:00-15:00', '15:00-16:00', 
    '16:00-17:00', '17:00-18:00', '18:00-19:00', '19:00-20:00', 
    '20:00-21:00', '21:00-22:00'
].map(time => {
    const [start, end] = time.split('-');
    return { full: time, start, end };
}))

const selectRule = {
    meetingTheme: [
        { required: true, message: '请输入会议主题', trigger: 'blur' },
        { max: 15, message: '会议主题不超过15个字符！', trigger: 'blur' }
    ]
}

const roomList = ref([])

const getRoomList = async () => {
    try {
        const { data: res } = await axios.get('/api/apply/free', { params: queryInfo })
        if (res.code !== 100) {
            ElMessage.error(res.message || '获取会议室列表失败！')
            roomList.value = []
            total.value = 0
        } else {
            if (res.extend && res.extend.result) {
                total.value = res.extend.result.total
                roomList.value = res.extend.result.list.map(room => {
                    const adaptedRoom = { ...room };
                    if (room.one !== undefined && !room.slot1) { // Basic adaptation
                        adaptedRoom.slot1 = true; adaptedRoom.slot2 = true;
                        adaptedRoom.slot3 = room.one; adaptedRoom.slot4 = room.two;
                        adaptedRoom.slot5 = true; adaptedRoom.slot6 = room.three;
                        adaptedRoom.slot7 = room.three; adaptedRoom.slot8 = room.four;
                        adaptedRoom.slot9 = room.four; adaptedRoom.slot10 = room.five;
                        adaptedRoom.slot11 = room.five; adaptedRoom.slot12 = true;
                        adaptedRoom.slot13 = true; adaptedRoom.slot14 = true;
                    }
                    return adaptedRoom;
                });
            } else {
                roomList.value = []
                total.value = 0
                ElMessage.info('当前条件下无可用会议室数据。')
            }
        }
    } catch (error) {
        console.error("Error fetching room list:", error);
        ElMessage.error('请求会议室列表时发生错误。')
        roomList.value = []
        total.value = 0
    }
}

const getDateLst = () => {
    const newDateLst = [];
    for (let i = 0; i < 7; i++) {
        let active = false
        const momentDate = moment().add(i, 'days')
        const fullDate = momentDate.format('YYYY-MM-DD')
        const displayDate = momentDate.format('MM-DD')
        const weekLst = ['日', '一', '二', '三', '四', '五', '六']
        const week = '星期' + weekLst[momentDate.day()]
        if (queryInfo.applyDate) { // Check if queryInfo.applyDate is initialized
          active = (fullDate === queryInfo.applyDate);
        } else if (i === 0) { // Default to today if not initialized
          active = true;
        }
        newDateLst.push({ week, date: fullDate, displayDate, active });
    }
    dateLst.value = newDateLst;
}

const changeDate = (key) => {
    dateLst.value.forEach((item, index) => {
        item.active = (index === key);
    });
    queryInfo.applyDate = dateLst.value[key].date;
    // Reset selection when date changes, as slot availability is per date
    selectedInfo.roomId = '';
    selectedInfo.applySlot = null;
    selectedInfo.displaySlot = '';
    selectedInfo.roomNo = '';
    // selectedInfo.applyDate will be set upon slot selection
    getRoomList();
}

// New function to get cell class
const getCellClassForRoom = (room, hour_idx, date_col_item) => {
    if (date_col_item.date !== queryInfo.applyDate) {
        return 'disabled-date-cell'; // Data for other dates not loaded for this room's slots
    }
    const slotKey = 'slot' + (hour_idx + 1);
    const isSlotFree = room[slotKey];

    if (!isSlotFree) {
        return 'occupied-cell';
    }
    if (room.roomId === selectedInfo.roomId && 
        (hour_idx + 1) === selectedInfo.applySlot &&
        date_col_item.date === selectedInfo.applyDate) {
        return 'selected-cell';
    }
    return 'free-cell';
};

// New function to select slot
const selectSlotInGrid = (room, hour_idx, date_col_item) => {
    if (date_col_item.date !== queryInfo.applyDate) {
        ElMessage.info('请先点击上方日期条选择 '+ date_col_item.displayDate + ' 作为焦点日期，再进行预约。');
        return;
    }

    const slotKey = 'slot' + (hour_idx + 1);
    const isSlotFree = room[slotKey];

    if (!isSlotFree) {
        ElMessage.warning('该时间段已被占用，请选择其他时间段。');
        return;
    }

    selectedInfo.roomId = room.roomId;
    selectedInfo.roomNo = room.roomNo;
    selectedInfo.applySlot = hour_idx + 1;
    selectedInfo.displaySlot = hourList.value[hour_idx].full;
    selectedInfo.applyDate = date_col_item.date; // This is queryInfo.applyDate
    selectedInfo.roomSize = room.roomSize;
    selectedInfo.air = room.air;
    selectedInfo.projector = room.projector;
};


const handleSizeChange = (newSize) => {
    queryInfo.pageSize = newSize;
    getRoomList();
}

const handleCurrentChange = (newPage) => {
    queryInfo.currentPage = newPage;
    getRoomList();
}

const submit = () => {
    if (!selectedInfo.roomId || selectedInfo.applySlot === null) {
        ElMessage.warning('请先从上方表格中选择一个可用的会议室和时间段。');
        return;
    }
    formRef.value.validate(async valid => {
        if (!valid) return;
        // Construct payload for POST, ensure only necessary fields from selectedInfo
        const bookingPayload = {
            applyDate: selectedInfo.applyDate,
            meetingTheme: selectedInfo.meetingTheme,
            roomNo: selectedInfo.roomNo, // May not be needed if roomId is primary
            applySlot: selectedInfo.applySlot,
            roomId: selectedInfo.roomId
        };
        const { data: res } = await axios.post('/api/apply', bookingPayload);
        if (res.code !== 100) {
            return ElMessage.error(res.message || '申请失败！');
        } else {
            ElMessage.success('申请成功，请等待管理员审核！');
            // Clear selection and form after successful submission
            selectedInfo.meetingTheme = '';
            selectedInfo.roomNo = '';
            selectedInfo.displaySlot = '';
            selectedInfo.roomId = '';
            selectedInfo.applySlot = null;
            selectedInfo.applyDate = queryInfo.applyDate; // Keep form date aligned with active date
            
            // Refresh room list to show updated availability (important!)
            getRoomList(); 
        }
    })
}

onMounted(() => {
    queryInfo.applyDate = moment().format('YYYY-MM-DD'); // Initialize with today
    getDateLst(); // Initialize dateLst based on today
    getRoomList();
})

</script>

<style lang="less" scoped>
    @disabledColor: #f0f0f0; 
    @occupiedCellBG: #fde2e2;
    @freeCellBG: #e8f4ff;
    @selectedCellBG: #409eff;
    @borderColor: #e0e0e0;
    @headerBG: #f8f9fa;
    @sumW: 980px;

    .resRoomPluginBox {
        width: @sumW;
        margin: 0 auto;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.08);
        border-radius: 4px;
        border: 1px solid #ebeef5;
        margin-bottom: 20px;
        padding: 15px;
    }

    .table-header {
        padding: 10px 0px;
        margin-bottom:10px;
        border-bottom: 1px solid #e4e7ed;
        h3 {
            margin: 0;
            font-size: 18px;
            font-weight: 550;
            color: #303133;
        }
    }

    .resHead {
        display: flex;
        padding: 8px 0;
        margin-bottom:10px;
        .identifyLst {
            display: flex;
            justify-content:flex-end;
            flex: 1;
            padding-right: 5px;
            .identify {
                margin-left: 15px;
                display: flex;
                align-items: center;
                i {
                    width: 14px; height: 14px; border-radius: 2px; box-sizing: border-box;
                    &.nooccupy { background-color: @freeCellBG; border: 1px solid #b3d8ff; }
                    &.select { background-color: @selectedCellBG; }
                }
                i:nth-child(1) { background-color: @occupiedCellBG; border: 1px solid #f5c6cb;} // For "被占用"
                span { font-size: 12px; margin-left: 4px; color: #606266;}
            }
        }
    }
    
    .date-selector {
        display: flex;
        align-items: center;
        margin-bottom: 15px;
        background-color: #f9fafc;
        border: 1px solid @borderColor;
        border-radius: 4px;
        padding: 5px;

        .date-selector-header {
            width: 100px;
            padding: 0 10px;
            font-size: 13px;
            font-weight: 500;
            color: #333;
            text-align: center;
        }
        .date-cells-container {
            flex: 1;
            display: flex;
            .date-cell-item {
                flex: 1;
                padding: 8px 0;
                text-align: center;
                cursor: pointer;
                border-right: 1px solid @borderColor;
                transition: background-color 0.3s;
                &:last-child { border-right: none; }
                &:hover { background-color: #eef5ff; }
                &.active {
                    background-color: @selectedCellBG;
                    color: #fff;
                    .date-week, .date-day { color: #fff; }
                }
                .date-content {
                     display: flex; flex-direction: column; align-items: center;
                    .date-week { font-size: 13px; margin-bottom: 2px; }
                    .date-day { font-size: 12px; }
                }
            }
        }
    }

    .rooms-schedule-area {
      h4 {
        font-size: 16px;
        color: #333;
        margin-top: 15px;
        margin-bottom: 8px;
        padding-bottom: 5px;
        border-bottom: 1px dashed #eee;
      }
    }
    
    .room-schedule-container {
        margin-bottom: 20px;
    }

    .room-calendar-grid {
        width: 100%;
        border-collapse: collapse;
        border: 1px solid @borderColor;
        
        th, td {
            border: 1px solid @borderColor;
            text-align: center;
            font-size: 12px;
        }

        thead th {
            background-color: @headerBG;
            padding: 8px 4px;
            font-weight: 500;
            color: #303133;
        }
        
        .time-col-header {
             width: 100px;
        }
        .date-col-header-cell {
            min-width: 80px; /* Adjust as needed */
        }
        .active-date-column {
            background-color: lighten(@selectedCellBG, 35%);
             border-left: 2px solid @selectedCellBG;
             border-right: 2px solid @selectedCellBG;
        }

        .time-row-header-cell {
            width: 100px; /* Match time-col-header */
            padding: 8px 4px;
            background-color: @headerBG;
            font-weight: 500;
        }

        .schedule-grid-cell {
            height: 38px;
            cursor: default; // Default to not clickable
            
            &.free-cell {
                background-color: @freeCellBG;
                cursor: pointer;
                &:hover { background-color: lighten(@freeCellBG, 5%); }
            }
            &.occupied-cell {
                background-color: @occupiedCellBG;
                // Optional: add a visual like a cross-line
                // background-image: linear-gradient(to top right, transparent 49.5%, #aaa 49.5%, #aaa 50.5%, transparent 50.5%);
                color: #a0a0a0;
                 cursor: not-allowed;
            }
            &.selected-cell {
                background-color: @selectedCellBG !important; // Ensure override
                color: #fff;
                font-weight: bold;
                box-shadow: inset 0 0 0 2px darken(@selectedCellBG, 10%);
            }
            &.disabled-date-cell {
                background-color: @disabledColor;
                color: #bbb;
                cursor: not-allowed;
                 // Maybe add a subtle pattern or text
                &:after {
                    content: "-"; // Placeholder for no data
                    font-size: 14px;
                    color: #ccc;
                }
            }
        }
    }
    .empty-rooms {
        margin: 20px 0;
    }
    .pagination-bar {
        margin-top: 20px;
        display: flex;
        justify-content: flex-end;
    }
    .booking-form {
      margin-top: 25px;
      padding-top: 20px;
      border-top: 1px solid #eee;
    }

</style>

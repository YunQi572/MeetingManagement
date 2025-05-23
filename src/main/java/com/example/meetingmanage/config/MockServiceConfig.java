package com.example.meetingmanage.config;

import com.example.meetingmanage.entities.*;
import com.example.meetingmanage.mapper.*;
import com.example.meetingmanage.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建模拟服务，不依赖数据库
 */
@Configuration
public class MockServiceConfig {

    @Bean
    @Primary
    public DepartmentService departmentService() {
        // 使用匿名内部类创建DepartmentService的模拟实现
        return new DepartmentService() {
            @Override
            public Msg getDept(Map<String, Object> para) {
                System.out.println("模拟 getDept: " + para);
                return Msg.success();
            }

            @Override
            public Msg getAll(Map<String, Object> para) {
                System.out.println("模拟 getAll: " + para);
                return Msg.success();
            }

            @Override
            public Msg duplicateCheck(Map<String, Object> para) {
                System.out.println("模拟 duplicateCheck: " + para);
                return Msg.success();
            }

            @Override
            public Msg update(Map<String, Object> para) {
                System.out.println("模拟 update: " + para);
                return Msg.success();
            }

            @Override
            public Msg addDept(Map<String, Object> para) {
                System.out.println("模拟 addDept: " + para);
                return Msg.success();
            }

            @Override
            public Msg deleteById(Map<String, Object> para) {
                System.out.println("模拟 deleteById: " + para);
                return Msg.success();
            }

            @Override
            public Msg selectCondition(Map<String, Object> para) {
                System.out.println("模拟 selectCondition: " + para);
                return Msg.success();
            }

            @Override
            public int seletByNo(String deptNo) {
                System.out.println("模拟 seletByNo: " + deptNo);
                return 1; // 返回一个模拟的部门ID
            }
        };
    }

    @Bean
    @Primary
    public RoomService roomService() {
        return new RoomService() {
            @Override
            public Msg getRoom(Map<String, Object> para) {
                System.out.println("模拟 getRoom: " + para);
                return Msg.success();
            }

            @Override
            public Msg getAll(Map<String, Object> para) {
                System.out.println("模拟 getAll: " + para);
                return Msg.success();
            }

            @Override
            public Msg updateEquipment(Map<String, Object> para) {
                System.out.println("模拟 updateEquipment: " + para);
                return Msg.success();
            }

            @Override
            public Msg updateRoom(Map<String, Object> para) {
                System.out.println("模拟 updateRoom: " + para);
                return Msg.success();
            }

            @Override
            public Msg duplicateCheck(Map<String, Object> para) {
                System.out.println("模拟 duplicateCheck: " + para);
                return Msg.success();
            }

            @Override
            public Msg addRoom(Map<String, Object> para) {
                System.out.println("模拟 addRoom: " + para);
                return Msg.success();
            }

            @Override
            public Msg deleteRoom(Map<String, Object> para) {
                System.out.println("模拟 deleteRoom: " + para);
                return Msg.success();
            }

            @Override
            public Msg selectByNo(Map<String, Object> para) {
                System.out.println("模拟 selectByNo: " + para);
                return Msg.success();
            }

            @Override
            public Msg getFree(Map<String, Object> para) {
                System.out.println("模拟 getFree: " + para);
                return Msg.success();
            }

            @Override
            public List<Room> getRooms() {
                System.out.println("模拟 getRooms");
                return new ArrayList<>(); // 返回空列表
            }
        };
    }

    @Bean
    @Primary
    public ShowStatusService showStatusService() {
        return new ShowStatusService() {
            @Override
            public void updateDefault() {
                System.out.println("模拟 updateDefault");
            }

            @Override
            public void updateRepair(Room room) {
                System.out.println("模拟 updateRepair: " + room);
            }

            @Override
            public void updateOccupy(TempStatus tempStatus) {
                System.out.println("模拟 updateOccupy: " + tempStatus);
            }

            @Override
            public void updateMessage(ShowStatus showStatus) {
                System.out.println("模拟 updateMessage: " + showStatus);
            }
        };
    }

    @Bean
    @Primary
    public ApplicationRecordService applicationRecordService() {
        return new ApplicationRecordService() {
            @Override
            public List<TempStatus> selectOccupy(java.util.Date applyDate) {
                System.out.println("模拟 selectOccupy: " + applyDate);
                return new ArrayList<>(); // 返回空列表
            }

            @Override
            public Msg select(Map<String, Object> para) {
                System.out.println("模拟 select: " + para);
                return Msg.success();
            }

            @Override
            public Msg addApply(Map<String, Object> para, java.util.Date applyDate, Integer applySlot) {
                System.out.println("模拟 addApply: " + para + ", " + applyDate + ", " + applySlot);
                return Msg.success();
            }

            @Override
            public ApplicationRecord getById(Integer id) {
                System.out.println("模拟 getById: " + id);
                ApplicationRecord record = new ApplicationRecord();
                record.setApplicationId(id);
                record.setAuditStatus(0);
                return record;
            }

            @Override
            public Msg revoke(Integer id) {
                System.out.println("模拟 revoke: " + id);
                return Msg.success();
            }

            @Override
            public Msg updateStatus(Integer id, Integer status, String reason) {
                System.out.println("模拟 updateStatus: " + id + ", " + status + ", " + reason);
                return Msg.success();
            }
        };
    }

    @Bean
    @Primary
    public MeetingRecordService meetingRecordService() {
        return new MeetingRecordService() {
            @Override
            public void addRecord(MeetingRecord meetingRecord) {
                System.out.println("模拟 addRecord: " + meetingRecord);
            }

            @Override
            public void revoke(Integer id) {
                System.out.println("模拟 revoke: " + id);
            }
        };
    }
}
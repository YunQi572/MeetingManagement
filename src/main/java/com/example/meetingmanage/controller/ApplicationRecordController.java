package com.example.meetingmanage.controller;

//import com.example.meetingmanage.Annotation.loginCharacter;
import com.example.meetingmanage.entities.ApplicationRecord;
import com.example.meetingmanage.entities.MeetingRecord;
import com.example.meetingmanage.entities.Msg;
import com.example.meetingmanage.service.ApplicationRecordService;
import com.example.meetingmanage.service.DepartmentService;
import com.example.meetingmanage.service.MeetingRecordService;
import com.example.meetingmanage.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/application")
public class ApplicationRecordController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ApplicationRecordService applicationRecordService;

    @Autowired
    private MeetingRecordService meetingRecordService;


    //查看申请
    @GetMapping("/history")
    public Msg history(@RequestParam Map<String,Object> para,@RequestHeader Map<String,Object> header){
        String token = (String) header.get("authorization");
        String deptNo = TokenUtil.getNo(token);
        if (!deptNo.equals("admin")){   //部门账号/管理员不需要id
        //返回id
            int deptId = departmentService.seletByNo(deptNo);
            para.put("deptId",deptId);
        }
        return applicationRecordService.select(para);
    }


    //同意申请，审批通过
//    @loginCharacter
    @PutMapping("/accept")
    public Msg accept(@RequestBody Map<String,Object> para){
        Integer id = (Integer) para.get("applicationId");
        //获取记录
        ApplicationRecord applicationRecord = applicationRecordService.getById(id);
        //封装会议
        MeetingRecord meetingRecord = new MeetingRecord(applicationRecord.getMeetingTheme(),
                applicationRecord.getMeetingSize(),applicationRecord.getDeptId(),
                applicationRecord.getRoomId(),applicationRecord.getApplyDate(),applicationRecord.getApplySlot());
        meetingRecord.setApplyId(applicationRecord.getApplicationId());
        //插入会议
        meetingRecordService.addRecord(meetingRecord);
        //修改申请的状态
        return  applicationRecordService.updateStatus(id,1,"");
    }

    //拒绝申请
//    @loginCharacter
    @PutMapping("/reject")
    public Msg reject(@RequestBody Map<String,Object> para){
        Integer id = (Integer) para.get("applicationId");
        String reason = "";
        if(para.containsKey("rejectReason")){
            reason = (String)para.get("rejectReason");
        }
        //修改申请的状态
        return  applicationRecordService.updateStatus(id,2,reason);
    }




}

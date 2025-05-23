package com.example.meetingmanage.controller;

import com.example.meetingmanage.entities.Msg;
import com.example.meetingmanage.service.DepartmentService;
import com.example.meetingmanage.service.MeetingRecordService;
import com.example.meetingmanage.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/meeting")
public class MeetingRecordController {

    @Autowired
    private MeetingRecordService meetingRecordService;

    @Autowired
    private  DepartmentService departmentService;

    //查看会议    带上分页和pass 0代表未进行，1代表已经进行
    @GetMapping("/history")
    public Msg history(@RequestParam Map<String,Object> para,@RequestHeader Map<String,Object> header){
        String token = (String) header.get("authorization");
        String deptNo = TokenUtil.getNo(token);
        if (!deptNo.equals("admin")){   //部门账号/管理员不需要id
            //返回id
            Integer deptId = departmentService.seletByNo(deptNo);
            para.put("deptId",String.valueOf(deptId));
        }
        return meetingRecordService.getHistory(para);
    }

}

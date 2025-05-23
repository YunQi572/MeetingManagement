package com.example.meetingmanage.controller;

import com.example.meetingmanage.entities.Msg;
import com.example.meetingmanage.entities.ShowStatus;
import com.example.meetingmanage.service.RoomService;
import com.example.meetingmanage.service.ShowStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private ShowStatusService showStatusService;

    // 根据ID查会议室

    @GetMapping("/find/{id}")
    public Msg getRoom(@RequestParam Map<String, Object> para) {
        return roomService.getRoom(para);
    }

    // 分页查询全部会议室（currentPage,pageSize）
    @GetMapping("/findpage")
    public Msg getAllRooms(@RequestParam Map<String, Object> para) {
        return roomService.getAll(para);
    }

    // 修改会议室信息：带什么改什么
    @PutMapping("/update")
    public Msg update(@RequestBody Map<String, Object> para) {
        // 判断参数中是否包含air或projector
        if (para.containsKey("air") || para.containsKey("projector")) {
            // 调用roomService的updateEquipment方法，更新设备信息
            return roomService.updateEquipment(para);
        }
        // 判断参数中是否包含roomStatus
        if (para.containsKey("roomStatus")) {
            // 调用roomService的updateRoom方法，更新会议室信息
            return roomService.updateRoom(para);
        }
        // 调用roomService的duplicateCheck方法，检查会议室信息是否重复
        Msg msg = roomService.duplicateCheck(para);
        // 如果会议室信息不重复
        if (msg.getCode() == 100) {
            // 获取参数中的roomId和roomSize
            Integer id = (Integer) para.get("roomId");
            Integer size = (Integer) para.get("roomSize");
            // 创建ShowStatus对象
            ShowStatus showStatus = new ShowStatus(id, size);
            // 调用showStatusService的updateMessage方法，更新会议室状态信息
            showStatusService.updateMessage(showStatus);
            // 调用roomService的updateRoom方法，更新会议室信息
            return roomService.updateRoom(para);
        } else {
            // 返回重复的会议室信息
            return msg;
        }
    }

    // 添加会议室 （roomNo,roomSize）
    @PostMapping("/add")
    public Msg add(@RequestBody Map<String, Object> para) {
        Msg msg = roomService.duplicateCheck(para);
        if (msg.getCode() == 200)
            return msg;
        return roomService.addRoom(para);
    }

    // 通过ID删除会议室 --实现连带删除
    @DeleteMapping("/delete")
    public Msg delete(@RequestBody Map<String, Object> para) {
        return roomService.deleteRoom(para);
    }

    // 分页模糊查询不带id （currentPage,pageSize,....）
    @GetMapping("/findNo")
    public Msg selectByNo(@RequestParam Map<String, Object> para) {
        return roomService.selectByNo(para);
    }

}

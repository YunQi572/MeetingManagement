package com.example.meetingmanage.service;

import com.example.meetingmanage.entities.Room;
import com.example.meetingmanage.entities.ShowStatus;
import com.example.meetingmanage.entities.TempStatus;
import com.example.meetingmanage.mapper.RoomMapper;
import com.example.meetingmanage.mapper.ShowStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowStatusService {

    @Autowired
    private ShowStatusMapper showStatusMapper;

    @Autowired
    private RoomMapper roomMapper;

    public void updateOccupy(TempStatus occupyRooms) {
        showStatusMapper.updateStatus(occupyRooms);
    }

    public void updateDefault() {
        List<Room> rooms = roomMapper.selectAll();
        for(Room room : rooms){
            showStatusMapper.updateDefault(room);
        }
    }

    public void updateMessage(ShowStatus showStatus) {
        showStatusMapper.updateByPrimaryKey(showStatus);
    }

    public void updateRepair(Room room) {
        showStatusMapper.updateRepair(room);
    }
}

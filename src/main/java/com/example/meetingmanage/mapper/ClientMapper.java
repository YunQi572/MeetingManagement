package com.example.meetingmanage.mapper;

import com.example.meetingmanage.entities.Client;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ClientMapper {
    
    /**
     * Find client by username
     * @param username client username
     * @return Client object if found, null otherwise
     */
    @Select("SELECT * FROM client WHERE username = #{username}")
    Client findByUsername(@Param("username") String username);
    
    /**
     * Find client by username and password
     * @param username client username
     * @param password client password
     * @return Client object if found, null otherwise
     */
    @Select("SELECT * FROM client WHERE username = #{username} AND password = #{password}")
    Client findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}

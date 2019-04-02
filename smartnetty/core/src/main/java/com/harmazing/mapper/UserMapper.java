package com.harmazing.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.harmazing.entity.User;

import java.util.List;

/**
 * Created by ming on 14-9-15.
 */
public interface UserMapper {

    public final static String USER_TB_NAME      = "spms_user";
    public final static String USER_RULE_BINDING = "spms_user_rule_binding";
    public final static String TB_USER_USER      = "tb_user_user";
    public final static String TB_USER_DEVICE    = "spms_user_device";
    public final static String TB_USER_DEVICE_GROUP    = "spms_ud_group";

    @Select("SELECT id FROM " + TB_USER_USER + " WHERE userCode=#{user_code}")
    public String getTbUserUserIdByUserCode(@Param("user_code") String user_code);
    
    @Select("SELECT id, userCode, mobile FROM " + TB_USER_USER + " WHERE userCode=#{user_code}")
    public User getTbUserUserByUserCode(@Param("user_code") String user_code);
    
    @Select("SELECT id FROM " + TB_USER_DEVICE + " WHERE device_id=#{device_id} AND user_id=#{user_id}")
    public String getTbUserDeviceIdByUserIdAndDeviceId(@Param("device_id") String dev_id, @Param("user_id") String user_id);
    
    @Select("SELECT id FROM " + TB_USER_DEVICE + " WHERE mac=#{mac}")
    public List<String> getTbUserDeviceIdsByMAC(@Param("mac") String mac);
    
    @Select("SELECT id FROM " + USER_TB_NAME + " WHERE FIND_IN_SET(ele_area_id, getChildsByArea(#{areaId},'2')) <> 0")
    @ResultType(String.class)
    public List<String> selectUserIdByEleAreaId(@Param("areaId") String areaId);

    @Select("SELECT id FROM " + USER_TB_NAME + " WHERE FIND_IN_SET(biz_area_id, getChildsByArea(#{areaId},'1')) <> 0")
    @ResultType(String.class)
    public List<String> selectUserIdByBizAreaId(@Param("areaId") String areaId);

    @Select("SELECT id FROM " + USER_TB_NAME + " WHERE gw_id=#{gatewayId}")
    @ResultType(String.class)
    public String selectUserIdByGatewayId(@Param("gatewayId") String gatewayId);
    
    @Select("SELECT ele_area_id FROM " + USER_TB_NAME + " WHERE id=#{id}")
    @ResultType(String.class)
    public String findEleAreaById(@Param("id") String id);
    
    @Select("SELECT su.id FROM " + USER_TB_NAME + " su, " +TB_USER_USER + " u WHERE su.sys_user_id = u.id AND u.userCode = #{userCode}")
    @ResultType(String.class)
    public String findSpmsUserIdByUserCode(@Param("userCode") String userCode);
    
    @Insert("INSERT INTO " + TB_USER_DEVICE + " (id, device_id, device_type, is_primary, user_id, version) VALUES (#{id}, #{device_id}, #{device_type}, #{is_primary}, #{user_id}, #{version})" )
    public void insertUserAndDeviceBinding(@Param("id") String id, @Param("device_id") String device_id, @Param("device_type") int device_type, @Param("is_primary") int is_primary, @Param("user_id") String user_id, @Param("version") int version );

    @Delete("DELETE FROM " + TB_USER_DEVICE + " WHERE device_id=#{device_id}" )
    public void deleteUserAndDeviceBinding(@Param("device_id") String device_id);
    
    @Delete("DELETE FROM " + TB_USER_DEVICE + " WHERE mac=#{mac}" )
    public void deleteUserAndDeviceBindingByDevMac(@Param("mac") String mac);
    
    @Delete("DELETE FROM " + TB_USER_DEVICE + " WHERE id=#{id}" )
    public void deleteUserAndDeviceBindingByDevId(@Param("id") String id);
    
    @Insert("INSERT INTO " + TB_USER_DEVICE + " (id, user_id, mobile, device_id, mac, device_type, is_primary ) VALUES( #{id},#{user_id},#{mobile}, #{device_id},#{mac},#{device_type},#{is_primary})") 
	public void insertAcAndUserBinding(@Param("id") String id,
			                           @Param("user_id") String user_id,
			                           @Param("mobile") String mobile,
			                           @Param("device_id") String device_id,
			                           @Param("mac") String mac,
			                           @Param("device_type") int device_type,
			                           @Param("is_primary") int is_primary);
	
	@Delete("DELETE FROM " + TB_USER_DEVICE + " WHERE id=#{id}")
	public void deleteAcAndUserBindingById( @Param("id") String id);
	
	@Delete("DELETE FROM " + TB_USER_DEVICE_GROUP + " WHERE ud_id=#{ud_id}")
	public void deleteUdGroupByUdId( @Param("ud_id") String ud_id);
	
	@Delete("DELETE FROM " + TB_USER_DEVICE_GROUP + " WHERE group_id=#{group_id}")
	public void deleteUdGroupByGroupId( @Param("group_id") String group_id);
}

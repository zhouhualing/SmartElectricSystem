package com.harmazing.spms.area.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.common.dao.SpringDataDAO;

@Repository("areaDAO")
public interface AreaDAO extends SpringDataDAO<Area> {
	
	@Query("from Area where parent.id=:p1 and name=:p2")
	public List<Area> hasName(@Param("p1") String myParent, @Param("p2") String myName);
	
	@Query("from Area where name=:p1")
	public List<Area> findAreaByName(@Param("p1") String name);
	
	
	@Modifying
	@Query("delete from Area WHERE instr(parentIds,:p1) > 0 or id = :p1")
	public void deleteAllChild(@Param("p1") String id);
	
	@Modifying
	@Query("delete from Area WHERE instr(parentIds,:p1) > 0 or id = :p2")
	public void deleteAllChildren(@Param("p1") String id , @Param("p2") String id2);
	
}

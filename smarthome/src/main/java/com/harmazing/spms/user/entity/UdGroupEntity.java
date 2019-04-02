/**
 * 
 */
package com.harmazing.spms.user.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.common.entity.GenericEntity;
import com.harmazing.spms.user.entity.SpmsUserDevice;

@Entity
@Table(name = "tb_ud_group")
public class UdGroupEntity extends GenericEntity {
    private static final long serialVersionUID = 1L;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="area_id")
    private Area area;
    
	@Length(min=0, max=255)
    private String name;
	
	@OneToMany(cascade={CascadeType.REMOVE},mappedBy="udGroup")
//	private List<SpmsUdGroup> udGroups = Lists.newArrayList(); // 拥有子user device group entities
	private Set<SpmsUdGroup> udGroups = new HashSet<SpmsUdGroup>();  

	public static long getSerialversionuid() {
        return serialVersionUID;
    }

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Set<SpmsUdGroup> getUdGroups() {
		return udGroups;
	}

	public void setUdGroups(Set<SpmsUdGroup> udGroups) {
		this.udGroups = udGroups;
	}

}

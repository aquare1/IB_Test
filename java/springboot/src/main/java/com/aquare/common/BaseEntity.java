package com.aquare.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;

/**
 * @author dengtao aquare@163.com
 */
public interface BaseEntity {

	public String getId();

	public void setId(String id);

	@JsonIgnore
	public String getMapTabel();



}

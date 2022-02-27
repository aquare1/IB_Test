package com.aquare.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author dengtao aquare@163.com
 */
@Getter
@Setter
public class BaseDTO<Entity extends BaseEntity> {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Timestamp startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Timestamp endTime;

    String timePickerFieldName;

    Integer pageSize = 0;

    Integer pageNo = 10;

    Entity entity;

}

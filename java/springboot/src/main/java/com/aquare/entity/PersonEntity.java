package com.aquare.entity;

import com.aquare.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dengtao aquare@163.com
 * @createAt 2022/2/23
 */
@Getter
@Setter
public class PersonEntity implements BaseEntity {

    String id;

    String name;

    @Override
    public String getMapTabel() {
        return "person";
    }
}

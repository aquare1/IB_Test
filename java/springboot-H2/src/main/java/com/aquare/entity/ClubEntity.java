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
public class ClubEntity implements BaseEntity {

    String id;

    String clubName;

    @Override
    public String getMapTabel() {
        return "t_club";
    }
}

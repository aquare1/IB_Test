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
public class AdressEntity implements BaseEntity {

    String id;

    String adressData;

    @Override
    public String getMapTabel() {
        return "t_adress";
    }
}

package com.aquare.endpoint;


import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

//@Configuration
//@WebEndpoint(id = "v2/post-person")
public class PostPersonPoint {

   // @WriteOperation
    public Map<String, Object> endpoint() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("message", "this is my endpoint");
        return map;
    }
}

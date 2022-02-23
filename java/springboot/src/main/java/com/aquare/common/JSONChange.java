package com.aquare.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

/**
 * JsonChangeUtil
 * @author dengtao aquare@163.com
 */
@Log4j2
public class JSONChange {

	public static<T> T jsonToObj(Class<T> classname, String jsonStr)
			 {
		ObjectMapper mapper = new ObjectMapper();
		T obj = null;
		try {
			 obj = (T) mapper.readValue(jsonStr,classname);
		} catch (IOException e) {
			log.error(ResultDTO.getErrer(e)+","+jsonStr);

		}
		return obj;
	}

	public static String objToJson(Object obj)  {
		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		try {
			result =  mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error(ResultDTO.getErrer(e));
		}
		return result;
	}
}
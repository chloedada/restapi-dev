package com.chloe.payment.common;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;


public class JsonUtil {

	/**
	 * json object의 필드 타입 별 체크
	 * 
	 * @param : jsonobject - request, 필드 key값, type ex) number, phone, id, email...
	 * @return : boolean 
	 */		
    public static JSONObject getJsonStringFromMap(Map<String, Object> map )
    {
        JSONObject jsonObject = new JSONObject();
        for( Map.Entry<String, Object> entry : map.entrySet() ) {
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
				jsonObject.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
        
        return jsonObject;
    }
	

}

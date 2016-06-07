/**
 * 
 */
package com.typartner.find.common.util.map;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.typartner.find.common.util.HttpClientUtil;
import com.typartner.find.common.util.map.data.poi.request.BaiduGeoDataPoiBaseRequest;
import com.typartner.find.common.util.map.data.poi.request.BaiduGeoDataPoiUpdateRequest;
import com.typartner.find.common.util.map.data.poi.response.BaiduGeoDataPoiResponse;
import com.typartner.find.common.util.map.search.request.BaiduGeoSearchBaseRequest;
import com.typartner.find.common.util.map.search.response.BaiduGeoSearchResponse;

/** 
 * @ClassName: LBSSearchUtil 
 * @Description: 云检索功能
 * @author Nemo
 * @date 2015年11月20日 下午10:09:49  
 */
public class BaiduGeoUtil {
	
	public static final int STATUS_SUCCESS = 0;
	
	/**
	 * 搜索
	 * @param request
	 * @return
	 */
	public static BaiduGeoSearchResponse search(BaiduGeoSearchBaseRequest request){
		BaiduGeoSearchResponse res = null;
		String url = request.getReqUrl();
		String content = request.getReqContent();
		HttpClientUtil client = new HttpClientUtil();
		client.setMethod("GET");
		if(content != null && content.length() > 0){
			url += "?" +content;
		}
		client.setReqURL(url);
//		client.setReqContent(content);
		try {
			client.callHttp();
			String json = client.getResContent();
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, Boolean.TRUE);
			res = mapper.readValue(json, BaiduGeoSearchResponse.class);
			return res;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * POI数据处理
	 * @param request
	 * @return
	 */
	public static BaiduGeoDataPoiResponse dataPoi(BaiduGeoDataPoiBaseRequest request){
		BaiduGeoDataPoiResponse res = null;
		String url = request.getReqUrl();
		String content = request.getReqContent();
		HttpClientUtil client = new HttpClientUtil();
		client.setReqURL(url);
		client.setReqContent(content);
		try {
			client.callHttp();
			String json = client.getResContent();
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, Boolean.TRUE);
			res = mapper.readValue(json, BaiduGeoDataPoiResponse.class);
			return res;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
//		BaiduGoeSearchLocalRequest request = new BaiduGoeSearchLocalRequest();
//		request.setQ("铜锣湾");
		
//		BaiduGeoSearchDetailRequest request = new BaiduGeoSearchDetailRequest();
//		request.setUid("1503207754");
//		
//		BaiduGeoSearchResponse res = BaiduGeoUtil.search(request);
		
//		BaiduGeoDataPoiCreateRequest request = new BaiduGeoDataPoiCreateRequest();
//		request.setTitle("迎泽公园");
//		request.setAddress("山西省太原市迎泽区解放南路");
//		request.setLatitude("112.567542");
//		request.setLongitude("37.860196");
//		request.setCoord_type("1");
		
		BaiduGeoDataPoiUpdateRequest request = new BaiduGeoDataPoiUpdateRequest();
		request.setId("1503207754");
		request.setLatitude("37.860196");
		request.setLongitude("112.567542");
		request.setCoord_type("3");
		
		BaiduGeoDataPoiResponse res = BaiduGeoUtil.dataPoi(request);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, Boolean.TRUE);
		System.out.println("Json2Java: "+mapper.writeValueAsString(res));
	}
}

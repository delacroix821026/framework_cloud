package com.fw.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.fw.common.httpclient.HttpClientUtils;

/**
 * 地理编码工具类
 * 
 * @author zhenxing.li
 * @see http://developer.baidu.com/map/index.php?title=webapi
 * @since [1.0.0]
 */
public final class GeocodingUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeocodingUtils.class);

	private static final String BAIDU_MAPS_API_KEY = "4gp1cYBlmCmwd6pb9uGwYruz"; // 百度地图API
																					// key
	private static final String GAODE_MAPS_API_KEY = "fca95b0c3393e537cc72d0dc3d8a164b"; // 高德地图API
																							// key
	private static final String GEOCODING_API_BAIDU = "http://api.map.baidu.com/geocoder/v2/"; // 百度地址解析和逆地址解析API
	private static final String IP_LOCATION_API_BAIDU = "http://api.map.baidu.com/location/ip"; // 百度IP定位API
	private static final String LOCATION_CONVERT_API_BAIDU = "http://api.map.baidu.com/geoconv/v1/"; // 地图地址转换API
	private static final String LOCATION_CONVERT_API_GAODE = "http://restapi.amap.com/v3/assistant/coordinate/convert"; // 高德地址转换API

	/**
	 * 百度定义的坐标类型
	 */
	private static final String COORD_TYPE_BAIDU = "bd09ll"; // 百度经纬度坐标
	private static final String COORD_TYPE_WGS = "wgs84ll"; // GPS经纬度

	/**
	 * 百度地图服务状态表 返回码 定义 0 正常 1 服务器内部错误 2 请求参数非法 3 权限校验失败 4 配额校验失败 5 ak不存在或者非法
	 * 101 服务禁用 102 不通过白名单或者安全码不对 2xx 无权限 3xx 配额错误
	 */
	private static final String STATUS_ACCESS_BAIDU_API_SUCCESS = "0"; // 调用百度地图API成功的状态值

	/**
	 * 高德地图服务状态表 返回状态的取值规则： 1：成功； 0：失败，未知原因； -11：失败，已存在相同名称表 -21：失败，已创建表达到最大数据
	 */
	private static final String STATUS_ACCESS_GAODE_API_SUCCESS = "1"; // 调用高德地图API成功的状态值

	/**
	 * 根据地址获取地理位置信息
	 * 
	 * @param address
	 *            地址信息 【必填】
	 * @param city
	 *            所在城市【可选】
	 * @return json数组，对象字段说明
	 * 
	 *         <pre>
	 *         { "result": { "confidence": 80, //可信度 "level": "商务大厦", //地址类型
	 *         "location": { "lat": 39.983423843929, "lng": 116.32298658484 },
	 *         "precise": 1 //位置的附加信息，是否精确查找。1为精确查找，0为不精确。 }, "status": 0
	 *         //返回结果状态值， 成功返回0，其他值请查看下方返回码状态表。 }
	 * @see http://developer.baidu.com/map/index.php?title=webapi/guide/
	 *      webservice-geocoding
	 * @since [1.0.0]
	 */
	public static JSONObject getLocationInfoByAddress(String address, String city) {
		if (StringUtils.isBlank(address)) {
			return null;
		}
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new BasicNameValuePair("output", "json"));
		paramsList.add(new BasicNameValuePair("ak", BAIDU_MAPS_API_KEY));
		paramsList.add(new BasicNameValuePair("coordtype", COORD_TYPE_WGS));
		paramsList.add(new BasicNameValuePair("address", address));
		if (StringUtils.isNotEmpty(city)) {
			paramsList.add(new BasicNameValuePair("city", city));
		}
		return HttpClientUtils.get(GEOCODING_API_BAIDU, paramsList);
	}

	/**
	 * 根据地址获取经纬度
	 * 
	 * @param address
	 *            地址信息 【必填】
	 * @param city
	 *            所在城市【可选】
	 * @return json数组，对象字段说明
	 * 
	 *         <pre>
	 *         { "lat": 39.983423843929, "lng": 116.32298658484 }, "status": 0
	 *         //返回结果状态值， 成功返回0，其他值请查看下方返回码状态表。 }
	 * @see http://developer.baidu.com/map/index.php?title=webapi/guide/
	 *      webservice-geocoding
	 * @since [1.0.0]
	 */
	public static JSONObject getLocationByAddress(String address, String city) {
		JSONObject json = getLocationInfoByAddress(address, city);
		if (null != json) {
			if (STATUS_ACCESS_BAIDU_API_SUCCESS.equals(json.getString("status"))) {
				return json.getJSONObject("result").getJSONObject("location");
			} else {
				LOGGER.error("Access Baidu maps api failed, this status is " + json.getString("status")
						+ ", error inof: " + json.getString("message"));
			}
		}
		return null;
	}

	/**
	 * 根据经纬获取详细地址信息
	 * 
	 * @param lat
	 *            纬度【必填】
	 * @param lng
	 *            经度【必填】
	 * @return json数组，对象字段说明
	 * 
	 *         <pre>
	 *   {
	        "result": {
	            "addressComponent": {
	                "city": "杭州市", //城市名
	                "country": "中国", //国家   
	                "country_code": 0,
	                "direction": "", //和当前坐标点的方向，当有门牌号的时候返回数据
	                "distance": "", //和当前坐标点的距离，当有门牌号的时候返回数据
	                "district": "富阳市",
	                "province": "浙江省",
	                "street": "新桥路",
	                "street_number": "" //街道门牌号
	            },
	            "business": "", //所在商圈信息，如 "人民大学,中关村,苏州街"
	            "cityCode": 179,
	            "formatted_address": "浙江省杭州市富阳市新桥路", //结构化地址信息
	            "location": {
	                "lat": 30.092123832154, //纬度坐标
	                "lng": 119.94215420314 //经度坐标
	            },
	            "poiRegions": [],
	            "sematic_description": "鸿世电器公司东南330米" //当前位置结合POI的语义化结果描述。
	        },
	        "status": 0 //返回结果状态值， 成功返回0，其他值请查看
	    }
	 *         </pre>
	 * 
	 * @see http://developer.baidu.com/map/index.php?title=webapi/guide/webservice-geocoding
	 * @author zhenxing.li
	 * @since [1.0.0]
	 */
	public static JSONObject getAddressInfoByLocation(String lat, String lng) {
		if (StringUtils.isBlank(lat) || StringUtils.isBlank(lng)) {
			return null;
		}
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new BasicNameValuePair("output", "json"));
		paramsList.add(new BasicNameValuePair("ak", BAIDU_MAPS_API_KEY));
		paramsList.add(new BasicNameValuePair("coordtype", COORD_TYPE_WGS));
		paramsList.add(new BasicNameValuePair("location", lat + "," + lng));
		return HttpClientUtils.get(GEOCODING_API_BAIDU, paramsList);
	}

	/**
	 * 根据经纬度获取城市
	 * 
	 * @param lat
	 *            纬度
	 * @param lng
	 *            经度
	 * @return String 城市名称
	 * @see http://developer.baidu.com/map/index.php?title=webapi/guide/
	 *      webservice-geocoding
	 * @since [产品/模块版本](可选)
	 */
	public static String getCityByLocation(String lat, String lng) {
		JSONObject json = getAddressInfoByLocation(lat, lng);
		if (null != json) {
			if (STATUS_ACCESS_BAIDU_API_SUCCESS.equals(json.getString("status"))) {
				return json.getJSONObject("result").getJSONObject("addressComponent").getString("city");
			} else {
				LOGGER.error("Access Baidu maps api failed, this status is " + json.getString("status")
						+ ", error inof: " + json.getString("message"));
			}
		}
		return null;
	}

	/**
	 * 根据ip获取地理位置
	 * 
	 * @param ip
	 *            ip地址【必填】
	 * @return json数组，对象字段说明
	 * 
	 *         <pre>
	 *   {
	        "address": "CN|上海|上海|None|CHINANET|0|0", #地址 
	        "content": { #详细内容
	            "address": "上海市", #简要地址
	            "address_detail": { #详细地址信息
	                "city": "上海市", #城市
	                "city_code": 289, #百度城市代码
	                "district": "", #区县
	                "province": "上海市", #省份
	                "street": "", #街道
	                "street_number": "" #门址
	            },
	            "point": { #百度经纬度坐标值
	                "x": "121.48789949",
	                "y": "31.24916171"
	            }
	        },
	        "status": 0 #返回状态码
	    }
	 *         </pre>
	 * 
	 * @see http://developer.baidu.com/map/index.php?title=webapi/ip-api
	 * @since [1.0.0]
	 */
	public static JSONObject getLocationByIp(String ip) {
		if (StringUtils.isBlank(ip)) {
			return null;
		}
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new BasicNameValuePair("ak", BAIDU_MAPS_API_KEY));
		paramsList.add(new BasicNameValuePair("coor", COORD_TYPE_BAIDU));
		paramsList.add(new BasicNameValuePair("ip", ip));
		return HttpClientUtils.get(IP_LOCATION_API_BAIDU, paramsList);
	}

	/**
	 * 根据ip获取定位城市
	 * 
	 * @param ip
	 *            ip地址【必填】
	 * @return String 城市名称
	 * @author zhenxing.li
	 * @see http://developer.baidu.com/map/index.php?title=webapi/ip-api
	 * @since [1.0.0]
	 */
	public static String getCityByIp(String ip) {
		JSONObject json = getLocationByIp(ip);
		if (null != json) {
			if (STATUS_ACCESS_BAIDU_API_SUCCESS.equals(json.getString("status"))) {
				return json.getJSONObject("content").getJSONObject("address_detail").getString("city");
			} else {
				LOGGER.error("Access Baidu maps api failed, this status is " + json.getString("status")
						+ ", error inof: " + json.getString("message"));
			}
		}
		return null;
	}

	/**
	 * 地图坐标转换
	 * @param from 源坐标类型
	 * @param to 目的坐标类型,其取值如下：【注：默认为高德坐标】   <pre>
	 *  1.转化为百度经纬度坐标
	 *      源坐标类型取值为如下：【注：默认为1】
	        1：GPS设备获取的角度坐标;
	        2：GPS获取的米制坐标、sogou地图所用坐标;
	        3：google地图、soso地图、aliyun地图、mapabc地图和amap地图所用坐标
	        4：3中列表地图坐标对应的米制坐标
	        5：百度地图采用的经纬度坐标
	        6：百度地图采用的米制坐标
	        7：mapbar地图坐标;
	        8：51地图坐标
	 *  2.转化为高德经纬坐标 【注：默认为gps】
	 *      源坐标类型取值为如下：
	 *      gps;mapbar;baidu
	 * </pre>
	 * @param lat 纬度 【必填】
	 * @param lng 经度 【必填】
	 * @return 转换后的经纬度JSON对象信息，如下 <pre>
	 *  {
	 *      "x":"119.92449613",
	 *      "y":"30.08336217"
	 *  } </pre>
	 * @see <pre>
	 * http://lbsbbs.amap.com/forum.php?mod=viewthread&tid=724&highlight=%E5%9D%90%E6%A0%87%E8%BD%AC%E6%8D%A2 【注：高德地址坐标转换】
	 * http://developer.baidu.com/map/changeposition.htm 【注：百度地址坐标转换】
	 * </pre>
	 * @since [1.0.0]
	 */
	public static JSONObject convertLocation(String from, String to, String lat, String lng) {
		if (StringUtils.isBlank(lat) || StringUtils.isBlank(lng)) {
			return null;
		}
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		if (StringUtils.isEmpty(to)) {
			to = "2";
		}
		JSONObject json = null;
		switch (to) {
		case "1":
			paramsList.add(new BasicNameValuePair("output", "json"));
			paramsList.add(new BasicNameValuePair("ak", BAIDU_MAPS_API_KEY));
			paramsList.add(new BasicNameValuePair("coords", lng + "," + lat));
			paramsList.add(new BasicNameValuePair("from", from));
			json = HttpClientUtils.get(LOCATION_CONVERT_API_BAIDU, paramsList);
			
			if (null != json && STATUS_ACCESS_BAIDU_API_SUCCESS.equals(json.getString("status"))) {
				return (JSONObject) json.getJSONArray("result").get(0);
			} else {
			    if (json == null) {
			        LOGGER.error("Access Baidu maps api failed, no data is result");
			    } else {
			        LOGGER.error("Access Baidu maps api failed, this status is " + json.getString("status")
			                + ", error inof: " + json.getString("message"));
			    }
			}
			break;
		default:
			paramsList.add(new BasicNameValuePair("output", "json"));
			paramsList.add(new BasicNameValuePair("key", GAODE_MAPS_API_KEY));
			paramsList.add(new BasicNameValuePair("locations", lng + "," + lat));
			paramsList.add(new BasicNameValuePair("coordsys", from));
			json = HttpClientUtils.get(LOCATION_CONVERT_API_GAODE, paramsList);
			if (null != json && STATUS_ACCESS_GAODE_API_SUCCESS.equals(json.get("status"))) {
				JSONObject jsonObject = new JSONObject();
				String[] location = json.getString("locations").split(",");
				jsonObject.put("x", location[0]);
				jsonObject.put("y", location[1]);
				return jsonObject;
			} else {
			    if (json == null) {
			        LOGGER.error("Access Gaode maps api failed, no data is result" );
                } else {
                    LOGGER.error("Access Baidu maps api failed, this status is " + json.getString("status")
                            + ", error inof: " + json.getString("message"));
                }
			}
			break;
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("x", lat);
        jsonObject.put("y", lng);
		return jsonObject;
	}

	public static void main(String[] args) {
		// System.out.println(JSONObject.toJSONString(getAddressInfoByLocation("30.089110",
		// "119.931040")));
		// System.out.println(JSONObject.toJSONString(getCityByLocation("30.089110",
		// "119.931040")));
		// System.out.println(JSONObject.toJSONString(getLocationInfoByAddress("北京市海淀区中关村大街27号中关村大厦二层",
		// "北京市")));
		// System.out.println(JSONObject.toJSONString(getLocationByAddress("北京市海淀区中关村大街27号中关村大厦二层",
		// "北京市")));
		// System.out.println(JSONObject.toJSONString(getLocationByIp("101.95.131.38")));
		// System.out.println(JSONObject.toJSONString(getCityByIp("101.95.131.38")));
		// System.out.println(JSONObject.toJSONString(convertLocation("1",
		// "1","30.089110", "119.931040")));
		// System.out.println(JSONObject.toJSONString(convertLocation("baidu",
		// null,"30.089110", "119.931040")));
		
		/*JSONObject json = GeocodingUtils.getAddressInfoByLocation("29.258823", "119.947763");
		if (null != json) {
			if (0 == json.getInteger("status")) {
				System.out.println(json.getJSONObject("result").getString("formatted_address"));
			} else {
				LOGGER.error("暂无获取到地址");
			}
		}*/
	}

}

package com.anycc.pmp.util;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JsonUtil {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

	// public static Map<String, String> jsonToMap(String jsonText) {
	// Map<String, String> map = new HashMap<String, String>();
	// if (null == jsonText) {
	// return map;
	// }
	// try {
	// JsonFactory jsonFactory = new MappingJsonFactory();
	// // Json解析器
	// JsonParser jsonParser = jsonFactory.createJsonParser(jsonText);
	// // 跳到结果集的开始
	// jsonParser.nextToken();
	// // while循环遍历Json结果
	// while (jsonParser.nextToken() != null) {
	// // 跳转到Value
	// if (jsonParser.nextToken() == JsonToken.START_ARRAY) {
	// // 里面有数组
	// StringBuffer sb = new StringBuffer();
	// String name = jsonParser.getCurrentName();
	// while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
	// sb.append(jsonParser.getText() + ",");
	// }
	// if (sb.length() > 0) {
	// sb.deleteCharAt(sb.length() - 1);
	// }
	// map.put(name, sb.toString());
	// continue;
	// }
	// // 将Json中的值装入Map中
	// map.put(jsonParser.getCurrentName(), jsonParser.getText());
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// log.error("getJson error", e);
	// }
	// return map;
	// }

	private static final JsonFactory JSONFACTORY = new JsonFactory();

	/**
	 * 转换Java Bean 为 json
	 */
	public static String beanToJson(Object o) {
		StringWriter sw = new StringWriter(300);
		JsonGenerator jsonGenerator = null;

		try {
			jsonGenerator = JSONFACTORY.createJsonGenerator(sw);
			MAPPER.writeValue(jsonGenerator, o);
			return sw.toString();

		} catch (Exception e) {
			log.error("beanToJson", e);
			return null;

		} finally {
			if (jsonGenerator != null) {
				try {
					jsonGenerator.close();
				} catch (Exception e) {
					log.error("beanToJson", e);
				}
			}
		}
	}

	/**
	 * json 转 javabean
	 * 
	 * @param json
	 * @return
	 */
	public static Object jsonToBean(String json, Class clazz) {
		try {
			return MAPPER.readValue(json, clazz);
		} catch (Exception e) {
			log.error("jsonToBean", e);
			return null;
		}
	}

	/**
	 * 转换Java Bean 为 HashMap
	 */
	public static Map<String, Object> beanToMap(Object o) {
		try {
			return MAPPER.readValue(beanToJson(o), HashMap.class);
		} catch (Exception e) {
			log.error("beanToMap", e);
			return null;
		}
	}

	/**
	 * 转换Json String 为 HashMap
	 */
	public static Map<String, Object> jsonToMap(String json) {
		try {
			Map<String, Object> map = MAPPER.readValue(json, HashMap.class);
			//
			// for (Map.Entry<String, Object> entry : map.entrySet()) {
			// if (entry.getValue() instanceof Collection
			// || entry.getValue() instanceof Map) {
			// entry.setValue(beanToJson(entry.getValue()));
			// }
			// }

			return map;
		} catch (Exception e) {
			log.error("jsonToMap", e);
			return null;
		}
	}

	public static class jsonParseException extends Exception {
		public jsonParseException(String message) {
			super(message);
		}
	}

	/**
	 * List 转换成json
	 * 
	 * @param list
	 * @return
	 */
	public static String listToJson(List<Map<String, String>> list) {
		JsonGenerator jsonGenerator = null;
		StringWriter sw = new StringWriter();
		try {
			jsonGenerator = JSONFACTORY.createJsonGenerator(sw);
			new ObjectMapper().writeValue(jsonGenerator, list);
			jsonGenerator.flush();
			return sw.toString();
		} catch (Exception e) {
			log.error("listToJson", e);
			return null;
		} finally {
			if (jsonGenerator != null) {
				try {
					jsonGenerator.flush();
					jsonGenerator.close();
				} catch (Exception e) {
					log.error("listToJson", e);
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * json 转List
	 * 
	 * @param json
	 * @return
	 */
	public static List<Map<String, String>> jsonToList(String json) {
		try {
			if (json != null && !"".equals(json.trim())) {
				JsonParser jsonParse = JSONFACTORY
						.createJsonParser(new StringReader(json));

				ArrayList<Map<String, String>> arrayList = (ArrayList<Map<String, String>>) new ObjectMapper()
						.readValue(jsonParse, ArrayList.class);
				return arrayList;
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error("jsonToList", e);
			return null;
		}
	}

	public static void main(String[] args) {
		// String json =
		// "{\"openid\":\"oMCR1jpm8aVloLN-1SMHRhJ2iOkQ\",\"nickname\":\"jargo\",\"privilege\":[\"1\",\"2\"],\"sex\":1,\"language\":\"zh_CN\",\"city\":\"扬州\",\"province\":\"江苏\",\"country\":\"中国\",\"headimgurl\":\"http://wx.qlogo.cn/mmopen/Nc5abknPSnXYY1yPyHcaYquythHpHZLkcLXaKl35WB0n9sp9eu2yUSoI0aCzU6dibSKCZzFId2MKFVvniaKhlxEgPf9zmStDnV/0\"}";
		// String json
		// ="{\"access_token\":\"ACCESS_TOKEN\", \"expires_in\":7200000000000,\"refresh_token\":\"REFRESH_TOKEN\", \"openid\":\"OPENID\", \"scope\":\"SCOPE\"}";
		// Map<String, Object> map = jsonToMap(json);
		// Iterator<String> it = map.keySet().iterator();
		// while (it.hasNext()) {
		// String key = (String) it.next();
		// Object value = map.get(key);
		// System.out.println(key + " " + value + " "+value.getClass());
		// if(value instanceof List){
		// System.out.println("********"+value.toString()+"*******");
		// }
		// }
		// WeixinUser weixinUser = new WeixinUser();
		// weixinUser.setNickname((String)map.get("nickname"));
		// weixinUser.setSex((Integer)map.get("sex"));
		// weixinUser.setProvince((String)map.get("province"));
		// weixinUser.setCity((String)map.get("city"));
		// weixinUser.setCountry((String)map.get("country"));
		// weixinUser.setHeadimgurl((String)map.get("headimgurl"));
		// weixinUser.setPrivilege(map.get("privilege").toString());
		// System.out.println(weixinUser);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("passport", "test");
		map.put("password", "123456");
		map.put("username", "test");
		System.out.println(beanToJson(map));
	}
}

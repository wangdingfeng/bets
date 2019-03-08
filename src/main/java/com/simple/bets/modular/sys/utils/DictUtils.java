
package com.simple.bets.modular.sys.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.simple.bets.core.common.util.SpringContextUtils;
import com.simple.bets.core.common.util.JsonMapper;
import com.simple.bets.core.redis.JedisUtils;
import com.simple.bets.modular.sys.dao.DictMapper;
import com.simple.bets.modular.sys.model.DictModel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 字典工具类
 * @author wangdingfeng
 * @version 2018-5-29
 */
public class DictUtils {
	
	private static DictMapper dictDao = SpringContextUtils.getBean(DictMapper.class);


	public static final String CACHE_DICT_MAP = "dictMap";

	
	public static String getDictLabel(String value, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			for (DictModel dict : getDictList(type)){
				if (type.equals(dict.getDictType()) && value.equals(dict.getDictValue())){
					return dict.getDictLabel();
				}
			}
		}
		return defaultValue;
	}
	
	public static String getDictLabels(String values, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(values)){
			List<String> valueList = Lists.newArrayList();
			for (String value : StringUtils.split(values, ",")){
				valueList.add(getDictLabel(value, type, defaultValue));
			}
			return StringUtils.join(valueList, ",");
		}
		return defaultValue;
	}

	public static String getDictValue(String label, String type, String defaultLabel){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)){
			for (DictModel dict : getDictList(type)){
				if (type.equals(dict.getDictType()) && label.equals(dict.getDictLabel())){
					return dict.getDictValue();
				}
			}
		}
		return defaultLabel;
	}
	
	public static List<DictModel> getDictList(String type){
		Map<String, Object> dictMap = JedisUtils.getObjectMap(CACHE_DICT_MAP);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			List<DictModel> dicts = dictDao.selectAllChildrenDict();
			for (DictModel dict : dicts){
				List<DictModel> dictList = (List<DictModel>)dictMap.get(dict.getDictType());
				if (dictList != null){
					dictList.add(dict);
				}else{
					dictMap.put(dict.getDictType(), Lists.newArrayList(dict));
				}
			}
			//一天过期
			JedisUtils.setObjectMap(CACHE_DICT_MAP,dictMap,86400);
		}
		List<DictModel> dictList = (List<DictModel>) dictMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}

	/**
	 * 清楚字典缓存
	 */
	public static void clearCacheDict(){
		JedisUtils.del(CACHE_DICT_MAP);
	}

	/**
	 * 返回字典列表（JSON）
	 * @param type
	 * @return
	 */
	public static String getDictListJson(String type){
		return JsonMapper.toJsonString(getDictList(type));
	}
}


package com.simple.bets.modular.sys.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.simple.bets.core.common.util.SpringContextUtils;
import com.simple.bets.core.common.util.JsonMapper;
import com.simple.bets.core.model.Tree;
import com.simple.bets.core.redis.JedisUtils;
import com.simple.bets.modular.sys.dao.DictMapper;
import com.simple.bets.modular.sys.model.Dict;
import com.simple.bets.modular.sys.model.Office;
import com.simple.bets.modular.sys.service.OfficeService;
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
			for (Dict dict : getDictList(type)){
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
			for (Dict dict : getDictList(type)){
				if (type.equals(dict.getDictType()) && label.equals(dict.getDictLabel())){
					return dict.getDictValue();
				}
			}
		}
		return defaultLabel;
	}
	
	public static List<Dict> getDictList(String type){
		Map<String, Object> dictMap = JedisUtils.getObjectMap(CACHE_DICT_MAP);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			for (Dict dict : dictDao.selectAllChildrenDict()){
				List<Dict> dictList = (List<Dict>)dictMap.get(dict.getDictType());
				if (dictList != null){
					dictList.add(dict);
				}else{
					dictMap.put(dict.getDictType(), Lists.newArrayList(dict));
				}
			}
			//一天过期
			JedisUtils.setObjectMap(CACHE_DICT_MAP,dictMap,86400);
		}
		List<Dict> dictList = (List<Dict>) dictMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
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

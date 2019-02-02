package com.simple.bets.core.beetl.fn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.bets.modular.sys.utils.DictUtils;
import com.simple.bets.core.common.lang.StringUtils;
import com.simple.bets.core.common.util.JsonMapper;
import com.simple.bets.modular.sys.model.Dict;
import org.beetl.core.BodyContent;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

/**
 * 获取页面元素htmml
 */
public class ToDocument implements Function {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private JsonNode data;

	@Override
	public Object call(Object[] paras, Context ctx) {
		String type = (String) paras[0];
		String content = ((BodyContent) paras[1]).getBody();

		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			data = objectMapper.readValue(JsonMapper.toJsonString(paras[2]), JsonNode.class);
			
//			logger.info(data.toString());

			if (type.equals("form")) { // form
				Document doc = DocumentHelper.parseText("<div>" + content + "</div>");
				listNodes(doc.getRootElement());
				// content = doc.asXML(); //dom4 标签为空自动闭合！！！！！！！！！！！！！
				content = asXml(doc);

//				logger.info(content);
				
				return content.replace("<![CDATA[", "").replace("]]>", "");

			} else if(type.equals("select")) {// select
//				String id = (String) paras[3];
				String name = (String) paras[3];
				String val = (String) paras[4];
				String onVal = (String) paras[5];
//				logger.info("id- " + id + " name- " + name);
				
				String idKey = StringUtils.isBlank(val)?"id":val;
				StringBuilder sb = new StringBuilder("<option value=\"\" >请选择</option>\n");
				
				String opStr = paras[2].toString();
				if(opStr.startsWith("getDictList")) {
					List<Dict> opData = DictUtils.getDictList(opStr.split(":")[1]);
					data = objectMapper.readValue(JsonMapper.toJsonString(opData), JsonNode.class);
					
					idKey = "value";
				}
				
				Iterator<JsonNode> it = data.elements();
				while(it.hasNext()) {
					JsonNode node = it.next();
					String value = node.get(idKey).asText("");

					sb.append("<option value=\"").append(value).append("\" ");
                    if (StringUtils.isNotBlank(onVal) && onVal.equals(value)) {
                        sb.append("selected=\"selected\" ");
                    }
                    sb.append(">");
					sb.append(node.get(name).asText(""));
					sb.append("</option>\n");
				}
				
				String temp = sb.toString();
//				logger.info(temp);
				
				return temp;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void listNodes(Element node) {
		// 当前节点下面子节点迭代器
		Iterator<Element> it = node.elementIterator();
		// 遍历
		while (it.hasNext()) {
			Element e = it.next();
			elementMethod(e);
			
			listNodes(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void elementMethod(Element node) {
		Attribute atr = node.attribute("class");
		if(atr != null && atr.getValue().contains("input-group")) {// 遇到treeselect、iconselect组件跳过
			return;
		}
		
		// input 动态绑定数据 
		List<Element> inputElmts = node.elements("input");
		
		for (Element element : inputElmts) {
			String key = element.attribute("name").getValue();

            JsonNode jNode;
			String eleValue;
			if(key.contains(".")) {
				String[] arr = key.split("\\.");
                jNode = data.get(arr[0])==null?null:data.get(arr[0]).get(arr[1]);
			} else {
				jNode = data.get(key);
			}
//			JsonNode jNode = data.get(key);
            eleValue = jNode == null ? "":jNode.asText("");

			atr = element.attribute("type");
			if(atr != null && atr.getValue().equals("radio")) {
				Attribute atrRadio = element.attribute("value");
				if(atrRadio.getValue().equals(eleValue)) {
					element.addAttribute("checked", "true");
				}
				continue;
			}
			if(atr != null && atr.getValue().equals("checkbox")) {
				continue;
			}
			if(atr != null && atr.getValue().equals("hidden") && element.attribute("value")!=null) {
				continue;
			}
			element.addAttribute("value", eleValue);
			
			atr = element.attribute("class");
			if(atr != null && atr.getValue().contains("readonly")) {
				element.addAttribute("readonly", "readonly");
			}
		}
		
		//textarea 动态绑定
		List<Element> txtAreaElmts = node.elements("textarea");
		for (Element element : txtAreaElmts) {
			
			String key = element.attribute("name").getValue();
			
			JsonNode jNode = data.get(key);
			element.setText(jNode==null?"":jNode.asText(""));
			//element.addAttribute("value", jNode==null?"":jNode.asText(""));
			
			atr = element.attribute("class");
			if(atr != null && atr.getValue().contains("readonly")&&jNode!=null) {
				element.addAttribute("readonly", "readonly");
			}
		}
		
		
		// select model 设置默认选中项
		List<Element> optElmts = node.elements("option");
		for (Element element : optElmts) {
			String opValue = element.attribute("value").getValue();
			
			String path = node.attribute("id").getValue();
			String pathArr[];
			
			JsonNode jNode;
			String ckValue;
			
			if(path.contains(".")) {
				pathArr = path.split("\\.");
				jNode = data.get(pathArr[0])==null?null:data.get(pathArr[0]).get(pathArr[1]);
				ckValue = jNode==null?"":jNode.asText("");
				
			} else {
				jNode = data.get(path);
				ckValue = jNode==null?"":jNode.asText("");
			}
			
			if(StringUtils.isNotBlank(ckValue) && ckValue.equals(opValue)) {
				element.addAttribute("selected", "selected");
			}
		}
		
	}
	
	
	private String asXml(Document document){
	    OutputFormat format = new OutputFormat();
	    format.setEncoding("UTF-8");
	    format.setExpandEmptyElements(true);
	    StringWriter out = new StringWriter();
	    XMLWriter writer = new XMLWriter(out, format);
	    writer.setEscapeText(false);
	    try {
	        writer.write(document);
	        writer.flush();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return out.toString();
	}

}

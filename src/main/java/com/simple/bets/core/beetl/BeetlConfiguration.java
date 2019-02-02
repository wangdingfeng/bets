/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.simple.bets.core.beetl;

import com.simple.bets.modular.sys.utils.DictUtils;
import com.simple.bets.core.common.util.ToolUtils;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * beetl拓展配置,绑定一些工具类,方便在模板中直接调用
 *
 * @author wangdingfeng
 * @Date 2018/2/22 21:03
 */
public class BeetlConfiguration extends BeetlGroupUtilConfiguration {

    @Autowired
    private Environment env;

    @Override
    public void initOther() {
        //注册工具类包
        groupTemplate.registerFunctionPackage("tool",new ToolUtils());

        groupTemplate.registerFunctionPackage("DictUtils",new DictUtils());

        groupTemplate.registerFunction("env", new Function() {
            @Override
            public String call(Object[] paras, Context ctx) {
                String key = (String) paras[0];
                String value = env.getProperty(key);
                if (value != null) {
                    return getStr(value);
                }
                if (paras.length == 2) {
                    return (String) paras[1];
                }
                return null;
            }

            String getStr(String str) {
                try {
                    return new String(str.getBytes("iso8859-1"), StandardCharsets.UTF_8);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

package com.simple.bets.core.beetl.fn;

import com.simple.bets.core.common.lang.StringUtils;
import org.beetl.core.Context;
import org.beetl.core.Function;

import java.util.List;

public class InArrayString implements Function {

    @Override
    public Object call(Object[] paras, Context ctx) {
        String word = (String) paras[0];
        List<String> arrString = (List<String>) paras[1];

        if (StringUtils.isBlank(word) || arrString == null)
            return false;

        for (String s : arrString) {
            if (word.equals(StringUtils.trim(s))) {
                return true;
            }
        }
        return false;
    }
}

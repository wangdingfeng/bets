package com.simple.bets.modular.sys.controller;

import com.alibaba.fastjson.JSON;
import com.simple.bets.core.common.util.ToolUtils;
import com.simple.bets.core.base.model.RedisInfo;
import com.simple.bets.core.base.model.ResponseResult;
import com.simple.bets.core.annotation.Log;
import com.simple.bets.core.redis.service.RedisService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

/**
 * @Author wangdingfeng
 * @Description //redis
 * @Date 10:50 2019/2/12
 **/

@Controller
@RequestMapping("/sys/redis")
public class RedisController {

    private static final String INTEGER_PREFIX = "(integer) ";

    private static final String PAGE_SUFFIX = "modular/sys/redis";

    @Autowired
    private RedisService redisService;

    /**
     * 跳转到redis 详细信息列表
     *
     * @param model
     * @return
     */
    @RequestMapping("/info")
    @RequiresPermissions("redis:list")
    public String getRedisInfo(Model model) {
        List<RedisInfo> infoList = this.redisService.getRedisInfo();
        model.addAttribute("infoList", infoList);
        return PAGE_SUFFIX + "/redis-info";
    }

    /**
     * 跳转到redis 命令操作界面
     *
     * @param model
     * @return
     */
    @RequestMapping("/terminal")
    @RequiresPermissions("redis:terminal")
    public String redisTerminal(Model model) {
        String osName = System.getProperty("os.name");
        model.addAttribute("osName", osName);
        return PAGE_SUFFIX + "/redis-terminal";
    }

    /**
     * 获取key数量
     *
     * @return
     */
    @RequestMapping("keysSize")
    @ResponseBody
    public String getKeysSize() {
        return JSON.toJSONString(redisService.getKeysSize());
    }

    /**
     * 获取内存信息
     *
     * @return
     */
    @RequestMapping("memoryInfo")
    @ResponseBody
    public String getMemoryInfo() {
        return JSON.toJSONString(redisService.getMemoryInfo());
    }

    /**
     * 执行redis命令
     *
     * @return
     */
    @Log("执行Redis keys命令")
    @RequestMapping("keys")
    @ResponseBody
    public ResponseResult keys(String arg) {
        try {
            Set<String> set = this.redisService.getKeys(arg);
            return ResponseResult.ok(set);
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 执行Redis get命令
     *
     * @param arg
     * @return
     */
    @Log("执行Redis get命令")
    @RequestMapping("get")
    @ResponseBody
    public ResponseResult get(String arg) {
        try {
            String result = this.redisService.get(arg);
            return ResponseResult.ok(result == null ? "" : result);
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @Log("执行Redis set命令")
    @RequestMapping("set")
    @ResponseBody
    public ResponseResult set(String arg) {
        try {
            String[] args = arg.split(",");
            if (args.length == 1)
                return ResponseResult.error("(error) ERR wrong number of arguments for 'set' command");
            else if (args.length != 2)
                return ResponseResult.error("(error) ERR syntax error");
            String result = this.redisService.set(args[0], args[1]);
            return ResponseResult.ok(result);
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @Log("执行Redis del命令")
    @RequestMapping("del")
    @ResponseBody
    public ResponseResult del(String arg) {
        try {
            String[] args = arg.split(",");
            Long result = this.redisService.del(args);
            return ResponseResult.ok(INTEGER_PREFIX + result);
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @Log("执行Redis exists命令")
    @RequestMapping("exists")
    @ResponseBody
    public ResponseResult exists(String arg) {
        try {
            int count = 0;
            String[] arr = arg.split(",");
            for (String key : arr) {
                if (this.redisService.exists(key))
                    count++;
            }
            return ResponseResult.ok(INTEGER_PREFIX + count);
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @Log("执行Redis pttl命令")
    @RequestMapping("pttl")
    @ResponseBody
    public ResponseResult pttl(String arg) {
        try {
            return ResponseResult.ok(INTEGER_PREFIX + this.redisService.pttl(arg));
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @Log("执行Redis pexpire命令")
    @RequestMapping("pexpire")
    @ResponseBody
    public ResponseResult pexpire(String arg) {
        try {
            String[] arr = arg.split(",");
            if (arr.length != 2 || !ToolUtils.isValidLong(arr[1])) {
                return ResponseResult.error("(error) ERR wrong number of arguments for 'pexpire' command");
            }
            return ResponseResult.ok(INTEGER_PREFIX + this.redisService.pexpire(arr[0], Long.valueOf(arr[1])));
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
    }
}

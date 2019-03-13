package com.simple.bets;

import cn.hutool.core.util.ReflectUtil;
import com.simple.bets.modular.sys.model.RoleModel;
import com.simple.bets.modular.sys.service.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Method;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BetsApplicationTests {
    @Autowired
    RoleService roleService;
    @Test
    public void contextLoads() throws Exception {
        RoleModel role = new RoleModel();
        role.setRoleName("这是一个测试角色");
        roleService.save(role);
        System.out.println(role.getRoleName());
    }

}


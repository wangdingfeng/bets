package com.simple.bets.modular.sys.service;

import com.simple.bets.core.base.model.Tree;
import com.simple.bets.modular.sys.model.Menu;
import com.simple.bets.core.base.service.IService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

@CacheConfig(cacheNames = "MenuService")
public interface MenuService extends IService<Menu> {

    List<Menu> findUserPermissions(String userName);

    List<Menu> findUserMenus(String userName);

    List<Menu> findAllMenus(Menu menu);

    /**
     * 查询菜单树
     * @param isAll 是否全部菜单
     * @return
     */
    Tree<Menu> getMenuTree(boolean isAll);

    List<Tree<Menu>> getUserMenu(String userName);

    Menu findById(Long menuId);

    Menu findByNameAndType(String menuName, String type);

    @Cacheable(key = "'url_'+ #p0")
    List<Map<String, String>> getAllUrl(String p1);
    /**
     * 保存更新菜单
     * @param menu
     */
    void saveOrUpdate(Menu menu);

    /**
     * 更新排序
     * @param menu
     */
    void updateMenuSort(Menu menu);

    /**
     * 删除菜单
     * @param id
     */
    void deleteMenu(Long id);
}

package com.simple.bets.modular.sys.service;

import com.simple.bets.core.base.model.Tree;
import com.simple.bets.modular.sys.model.MenuModel;
import com.simple.bets.core.base.service.IService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

@CacheConfig(cacheNames = "MenuService")
public interface MenuService extends IService<MenuModel> {

    List<MenuModel> findUserPermissions(String userName);

    List<MenuModel> findUserMenus(String userName);

    List<MenuModel> findAllMenus(MenuModel menu);

    /**
     * 查询菜单树
     * @param isAll 是否全部菜单
     * @return
     */
    Tree<MenuModel> getMenuTree(boolean isAll);

    List<Tree<MenuModel>> getUserMenu(String userName);

    MenuModel findById(Long menuId);

    MenuModel findByNameAndType(String menuName, String type);

    @Cacheable(key = "'url_'+ #p0")
    List<Map<String, String>> getAllUrl(String p1);
    /**
     * 保存更新菜单
     * @param menu
     */
    void saveOrUpdate(MenuModel menu);

    /**
     * 更新排序
     * @param menu
     */
    void updateMenuSort(MenuModel menu);

    /**
     * 删除菜单
     * @param id
     */
    void deleteMenu(Long id);
}

package com.spring.persistence;

import com.spring.domain.model.RoleResource;
import com.spring.domain.vo.RoleResourcesVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:${Description}
 * @Author : Mr.Cheng
 * @Date:2017/6/24
 */
@Repository
public interface RoleResourcesMapper {
    /**
     * 添加角色资源
     * @param roleResource
     * @return
     */
    int addRoleResource(@Param("RoleResource") RoleResource roleResource);

    /**
     * 添加角色资源列表
     * @param roleResourceList
     * @return
     */
    int addRoleResources(@Param("RoleResources") List<RoleResource> roleResourceList);


    /**
     * 获得角色资源
     * @param roleId
     * @return
     */
    RoleResourcesVO getRoleResourcesVO(@Param("roleId") Integer roleId);

    /**
     *获得角色通过资源id
     * @return
     */
    List<RoleResourcesVO> listRoleByResourcesId(@Param("id") Integer id);
}

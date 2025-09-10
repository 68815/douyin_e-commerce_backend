package app.config;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * SaToken权限认证实现类
 * 实现StpInterface接口，用于获取用户的权限和角色信息
 */
@Component
public class SaTokenPermissionImpl implements StpInterface {

    /**
     * 返回指定账号id所拥有的权限码集合
     * @param loginId 账号id
     * @param loginType 账号类型
     * @return 权限码列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 这里应该根据实际业务从数据库或缓存中获取用户权限
        // 示例：返回用户拥有的权限列表
        List<String> permissionList = new ArrayList<>();
        
        // 根据用户ID获取权限（这里需要实现具体的权限查询逻辑）
        // 例如：从数据库查询用户权限，或者从缓存中获取
        
        // 示例权限
        if ("1".equals(loginId.toString())) {
            // 管理员权限
            permissionList.add("user:add");
            permissionList.add("user:delete");
            permissionList.add("user:update");
            permissionList.add("user:query");
            permissionList.add("user:ban");
            permissionList.add("user:unban");
            permissionList.add("admin:manage");
        } else if ("2".equals(loginId.toString())) {
            // 普通用户权限
            permissionList.add("user:query");
            permissionList.add("user:update");
        }
        
        return permissionList;
    }

    /**
     * 返回指定账号id所拥有的角色标识集合
     * @param loginId 账号id
     * @param loginType 账号类型
     * @return 角色标识列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 这里应该根据实际业务从数据库或缓存中获取用户角色
        // 示例：返回用户拥有的角色列表
        List<String> roleList = new ArrayList<>();
        
        // 根据用户ID获取角色（这里需要实现具体的角色查询逻辑）
        // 例如：从数据库查询用户角色，或者从缓存中获取
        
        // 示例角色
        if ("1".equals(loginId.toString())) {
            // 管理员角色
            roleList.add("admin");
            roleList.add("super-admin");
        } else if ("2".equals(loginId.toString())) {
            // 普通用户角色
            roleList.add("user");
        } else {
            // 默认用户角色
            roleList.add("guest");
        }
        
        return roleList;
    }
}
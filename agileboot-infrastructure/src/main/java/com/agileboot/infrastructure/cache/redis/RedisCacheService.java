package com.agileboot.infrastructure.cache.redis;

import cn.hutool.extra.spring.SpringUtil;
import com.agileboot.infrastructure.cache.RedisUtil;
import com.agileboot.infrastructure.web.domain.login.WebLoginUser;
import com.agileboot.infrastructure.web.domain.login.RoleInfo;
import com.agileboot.orm.system.entity.SysPostEntity;
import com.agileboot.orm.system.entity.SysRoleEntity;
import com.agileboot.orm.system.entity.SysUserEntity;
import com.agileboot.orm.system.service.ISysPostService;
import com.agileboot.orm.system.service.ISysRoleService;
import com.agileboot.orm.system.service.ISysUserService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * @author valarchie
 */
@Component
@RequiredArgsConstructor
public class RedisCacheService {

    @NonNull
    private RedisUtil redisUtil;

    public RedisCacheTemplate<String> captchaCache;
    public RedisCacheTemplate<WebLoginUser> loginUserCache;
    public RedisCacheTemplate<SysUserEntity> userCache;
    public RedisCacheTemplate<SysRoleEntity> roleCache;

    public RedisCacheTemplate<SysPostEntity> postCache;

//    public RedisCacheTemplate<RoleInfo> roleModelInfoCache;

    @PostConstruct
    public void init() {

        captchaCache = new RedisCacheTemplate<>(redisUtil, CacheKeyEnum.CAPTCHAT);

        loginUserCache = new RedisCacheTemplate<>(redisUtil, CacheKeyEnum.LOGIN_USER_KEY);

        userCache = new RedisCacheTemplate<SysUserEntity>(redisUtil, CacheKeyEnum.USER_ENTITY_KEY) {
            @Override
            public SysUserEntity getObjectFromDb(Object id) {
                ISysUserService userService = SpringUtil.getBean(ISysUserService.class);
                return userService.getById((Serializable) id);
            }
        };

        roleCache = new RedisCacheTemplate<SysRoleEntity>(redisUtil, CacheKeyEnum.ROLE_ENTITY_KEY) {
            @Override
            public SysRoleEntity getObjectFromDb(Object id) {
                ISysRoleService roleService = SpringUtil.getBean(ISysRoleService.class);
                return roleService.getById((Serializable) id);
            }
        };

//        roleModelInfoCache = new RedisCacheTemplate<RoleInfo>(redisUtil, CacheKeyEnum.ROLE_MODEL_INFO_KEY) {
//            @Override
//            public RoleInfo getObjectFromDb(Object id) {
//                UserDetailsService userDetailsService = SpringUtil.getBean(UserDetailsService.class);
//                return userDetailsService.getRoleInfo((Long) id);
//            }
//
//        };

        postCache = new RedisCacheTemplate<SysPostEntity>(redisUtil, CacheKeyEnum.POST_ENTITY_KEY) {
            @Override
            public SysPostEntity getObjectFromDb(Object id) {
                ISysPostService postService = SpringUtil.getBean(ISysPostService.class);
                return postService.getById((Serializable) id);
            }

        };


    }


}

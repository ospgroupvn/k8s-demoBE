package com.osp.bttp.dao.service;

import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.repository.db3.AccUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class RedisService {
    //logger
    private Logger logger = LogManager.getLogger(RedisService.class);
    private static final String OTP_REQUEST_COUNT_KEY_PREFIX = "otp_request_count:";
    private static final int MAX_OTP_REQUESTS_PER_DAY = 30;
    private static final long EXPIRATION_DURATION = 24 * 60 * 60; // 24 hours
    @Autowired
    private AccUserRepository userRepository;
    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;
    @Value("${redis.enable}")
    private boolean redisEnable;
    @Autowired
    private GroupService groupService;
    public void deleteCacheAuthoInfoByUserId(Long userId) {
        try {
            AccUser accUser = userRepository.findById(userId).orElse(null);
            if (accUser != null) {
                redisTemplate.delete(Constants.PARAMETER.REDIS_KEY_AUTHORITY_ALL + accUser.getUsername());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


    public void deleteAll() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    

    public void reloadCacheUser(Long userId, String username) {
        String cacheKey = Constants.PARAMETER.REDIS_KEY_AUTHORITY_ALL + username;
        if ( redisEnable && redisTemplate.hasKey(cacheKey) && ((List<String>) redisTemplate.opsForValue().get(cacheKey)).size() > 0) {
            deleteCacheAuthoInfoByUserId(userId);
        } else {
            List<String> list = groupService.loadListAuthorityOfUserByUserIdAll(userId);
            if (list != null && list.size() > 0) {
                if ( redisEnable ) redisTemplate.opsForValue().set(cacheKey, list);
                if ( redisEnable ) redisTemplate.expire(cacheKey, 10, TimeUnit.MINUTES);
            }
        }
    }

    public void deleteCacheAuthoInfoByUserIds(List<Long> listUser) {
        try {
            List<String> listUsername = userRepository.findAllByIds(listUser);
            if (listUsername != null && listUsername.size() > 0) {
                for (String username : listUsername) {
                    redisTemplate.delete(Constants.PARAMETER.REDIS_KEY_AUTHORITY_ALL + username);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void showInfo() {
        logger.info("Redis info: username and host port ");
        logger.info(redisTemplate.getConnectionFactory().getConnection().info("server"));
        //show all keys
        Set<Serializable> keys = redisTemplate.keys("*");
        for (Serializable key : keys) {
            logger.info("Key: " + key);
        }

        //set key=test value = test1
        if ( redisEnable ) redisTemplate.opsForValue().set("test5", "test5", 10, TimeUnit.MINUTES);

    }
}

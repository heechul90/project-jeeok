package com.jeeok.jeeokmember.core.auth.repository;

import com.jeeok.jeeokmember.core.auth.redis.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}

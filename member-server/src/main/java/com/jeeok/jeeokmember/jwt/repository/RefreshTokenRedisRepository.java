package com.jeeok.jeeokmember.jwt.repository;

import com.jeeok.jeeokmember.jwt.redis.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}

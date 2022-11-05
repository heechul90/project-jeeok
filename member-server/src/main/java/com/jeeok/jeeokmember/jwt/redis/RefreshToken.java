package com.jeeok.jeeokmember.jwt.redis;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private String memberId;
    private String refreshTokenId;

    public static RefreshToken of(String memberId, String refreshTokenId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.memberId = memberId;
        refreshToken.refreshTokenId = refreshTokenId;
        return refreshToken;
    }
}

package com.jeeok.jeeokmember.jwt.dto;

import com.jeeok.jeeokmember.core.domain.AuthType;
import com.jeeok.jeeokmember.core.domain.Member;
import com.jeeok.jeeokmember.core.domain.RoleType;
import lombok.*;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OAuthAttributeDto {

    private Map<String, Object> attributes; // OAuth2 반환하는 유저정보 MAP
    private String nameAttributeKey;
    private String name;
    private String email;
    private AuthType authType;

    public OAuthAttributeDto(Map<String, Object> attributes, String nameAttributeKey, String name, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    public static OAuthAttributeDto of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        // 여기서 네이버와 카카오 등 구분 (ofNaver, ofKakao)
        if ("naver".equals(registrationId)) {
            return ofNaver(userNameAttributeName, attributes);
        } else if ("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        } else if ("google".equals(registrationId)) {
            return ofGoogle(userNameAttributeName, attributes);
        } else if ("github".equals(registrationId)) {
            return ofGithub(userNameAttributeName, attributes);
        }

        return null;
    }

    //naver
    private static OAuthAttributeDto ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributeDto.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .authType(AuthType.NAVER)
                .build();
    }

    //kakao
    private static OAuthAttributeDto ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributeDto.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .authType(AuthType.KAKAO)
                .build();
    }

    //google
    private static OAuthAttributeDto ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributeDto.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .authType(AuthType.GOOGLE)
                .build();
    }

    //github
    private static OAuthAttributeDto ofGithub(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributeDto.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .authType(AuthType.GITHUB)
                .build();
    }

    public Member toMember(OAuthAttributeDto attributeDto){
        return Member.createMember()
                .email(this.email)
                .password("temp")
                .name(this.name)
                .phoneNumber(null)
                .roleType(RoleType.ROLE_USER)
                .authType(this.authType)
                .build();
    }
}

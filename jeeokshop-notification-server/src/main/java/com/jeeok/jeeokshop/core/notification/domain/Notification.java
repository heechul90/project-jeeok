package com.jeeok.jeeokshop.core.notification.domain;

import com.jeeok.jeeokshop.common.entity.Yn;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private Long memberId;
    private String title;
    private String message;

    @Enumerated(EnumType.STRING)
    private Yn readYn;

    @Builder(builderMethodName = "createNotification")
    public Notification(Long memberId, String title, String message) {
        this.memberId = memberId;
        this.title = title;
        this.message = message;
        this.readYn = Yn.N;
    }
}

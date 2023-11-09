package com.uexcel.spring.security.client.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ChangeEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String newEmail;
    @Column(nullable = true)
    private String oldEmailtoken;
    @Column(nullable = true)
    private String newEmailtoken;
    private Date expirationTime;
    @Column(nullable = false, unique = true)
    private Long targetId;

    public ChangeEmail(String newEmail, Long targetId, String oldEmailtoken) {
        this.newEmail = newEmail;
        this.targetId = targetId;
        this.oldEmailtoken = oldEmailtoken;
        this.newEmailtoken = UUID.randomUUID().toString();
        this.expirationTime = expiryTime();
    }

    public ChangeEmail(String newEmailtoken) {
        this.newEmailtoken = newEmailtoken;
        this.oldEmailtoken = UUID.randomUUID().toString();
        this.expirationTime = expiryTime();
    }

    private Date expiryTime() {
        // expiration time = 10 min
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, 10);
        return new Date(calendar.getTime().getTime());
    }

}

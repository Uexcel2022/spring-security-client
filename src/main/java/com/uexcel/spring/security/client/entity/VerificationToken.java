package com.uexcel.spring.security.client.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
    private String token;
    private Date takenExpirateTime; // from java.util

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_VERIFICATION_TOKEN"))
    private User user;

    public VerificationToken(User user) {
        super();
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.takenExpirateTime = expiryTime();
    }

    public VerificationToken(String token) {
        super();
        this.token = token;
        this.takenExpirateTime = expiryTime();
    }

    private Date expiryTime() {
        // expiration time = 10 min
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, 10);
        return new Date(calendar.getTime().getTime());
    }

}

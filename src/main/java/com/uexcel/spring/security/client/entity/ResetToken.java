package com.uexcel.spring.security.client.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER_IN_RESET_TOKEN"), nullable = false)
    private User user;

    public ResetToken(User user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.expirationTime = ExpiryTime();
    }

    public ResetToken(String token) {
        this.token = token;
        this.expirationTime = ExpiryTime();
    }

    private Date ExpiryTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, 10);

        return new Date(calendar.getTime().getTime());
    }
}

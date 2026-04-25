package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "password_recovery")
public class PasswordRecovery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "password_recovery_id", updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "password_recovery_user", nullable = false)
    @Setter(AccessLevel.NONE)
    private User user;

    @Column(name = "password_recovery_token", nullable = false)
    private String token;

    @Column(name = "password_recovery_expiration_date_time", updatable = false, nullable = false)
    LocalDateTime expirationDateTime;

    @PrePersist
    public void setExpirationDateTime(){
        this.expirationDateTime = LocalDateTime.now().plusMinutes(10);
    }

    public boolean isTokenValid() {
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(this.expirationDateTime);
    }

    public PasswordRecovery(
            User user,
            String token
    ){
        this.user = user;
        this.token = token;
    }

    @Override
    public String toString() {
        return "PasswordRecovery{" +
                "id=" + id +
                ", user=" + user +
                ", token='" + token + '\'' +
                ", expirationDateTime=" + expirationDateTime +
                '}';
    }
}

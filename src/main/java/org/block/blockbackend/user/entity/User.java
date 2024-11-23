package org.block.blockbackend.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="users")
@NoArgsConstructor
@Getter
public class User {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String passwd;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal credit;

    // @UpdateTimestamp
    @Setter
    @Column(name="last_connectect_time")
    private LocalDateTime lastConnectTime;

    @CreationTimestamp
    @Column(name="create_at", nullable = false)
    private LocalDateTime createAT;

    @ColumnDefault("'USER'")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User(String id, String passwd, String name, BigDecimal credit, Role role) {
        this.id = id;
        this.passwd = passwd;
        this.name = name;
        this.credit = credit;
        this.role = role;
    }
}

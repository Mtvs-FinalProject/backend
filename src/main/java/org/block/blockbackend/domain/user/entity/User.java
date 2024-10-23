package org.block.blockbackend.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import java.sql.Timestamp;

@Entity
@Table(name="users")
@NoArgsConstructor
@Getter
public class User {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String passwd;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int credit;

    @Column(name="last_connectect_time")
    private Timestamp lastConnectTime;

    @Column(name="create_at", nullable = false)
    private Timestamp createAT;

    @Column(nullable = false)
    private Role role;

    public User(String id, String passwd, String name, int credit, Timestamp lastConnectTime, Timestamp createAT, Role role) {
        this.id = id;
        this.passwd = passwd;
        this.name = name;
        this.credit = credit;
        this.lastConnectTime = lastConnectTime;
        this.createAT = createAT;
        this.role = role;
    }
}

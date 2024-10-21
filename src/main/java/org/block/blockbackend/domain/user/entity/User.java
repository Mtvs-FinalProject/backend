package org.block.blockbackend.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import java.sql.Timestamp;

@Entity
@Table
@AllArgsConstructor
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

    @Column(nullable = true)
    private Role role;
}

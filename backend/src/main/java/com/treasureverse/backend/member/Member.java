package com.treasureverse.backend.member;

import com.treasureverse.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 30)
    private String username;
    private String password;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String phoneNumber;
    private String postalCode;
    @Column(nullable = false) private String address;
    private String addressDetail;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Role role;

    protected Member() { }
    public Member(String username, String password, String name, String phoneNumber, String postalCode, String address, String addressDetail) {
        this.username = username; this.password = password; this.name = name; this.phoneNumber = phoneNumber;
        this.postalCode = postalCode; this.address = address; this.addressDetail = addressDetail; this.role = Role.USER;
    }
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
}

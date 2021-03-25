package com.hubert.momoservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "fingerprints")
public class Fingerprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    @Column(
            nullable = false,
            unique = true,
            columnDefinition = "TEXT"
    )
    private String imageUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id"
    )
    private User user;

    public Fingerprint() {
    }

    public Fingerprint(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Fingerprint(String imageUrl, User user) {
        this.imageUrl = imageUrl;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package com.hubert.momoservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    @Column(
            nullable = false,
            unique = true,
            columnDefinition = "TEXT"
    )
    private String apiToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id"
    )
    private User user;

    public Token() {
    }

    public Token(String apiToken) {
        this.apiToken = apiToken;
    }

    public Token(String apiToken, User user) {
        this.apiToken = apiToken;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

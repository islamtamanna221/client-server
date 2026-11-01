package com.codeexplorer.model;

public class LoginResponse {

    private Long id;
    private String username;
    private int pokeballs;

    public LoginResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getPokeballs() {
        return pokeballs;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPokeballs(int pokeballs) {
        this.pokeballs = pokeballs;
    }
}

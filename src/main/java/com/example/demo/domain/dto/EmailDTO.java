package com.example.demo.domain.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class EmailDTO implements Serializable{

    @Email
    @NotEmpty
    private String email;

    public EmailDTO(String email) {
        this.email = email;
    }

    public EmailDTO(){
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

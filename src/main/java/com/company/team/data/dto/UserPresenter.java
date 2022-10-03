package com.company.team.data.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserPresenter implements Serializable {

    @JsonInclude
    String mail;

    public UserPresenter() {
        this.mail = "long@";
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UserPresenter(@JsonProperty("mail") String mail) {
        this.mail = mail;
    }


}

package com.redhat.saiello.bank.account.domain.model.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.saiello.bank.account.domain.model.PersonalInfo;
import lombok.ToString;

@ToString
public class BankAccountCreated extends BankAccountEvent {

    public static final String TYPE = "AccountCreated";

    private final PersonalInfo personalInfo;

    @JsonCreator
    public BankAccountCreated(@JsonProperty("personalInfo") PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    @Override
    public String type() {
        return TYPE;
    }

    public PersonalInfo getPersonalInfo(){
        return personalInfo;
    }

}

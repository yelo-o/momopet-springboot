package com.momo.domain.member;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String si;
    private String gu;

    public Address() {
    }

    public Address(String si, String gu) {
        this.si = si;
        this.gu = gu;
    }
}

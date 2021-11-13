package com.lukman.people.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Person {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private int numberOfItemsBought;
    private double totalAmountSpent;
    private boolean verified;
}

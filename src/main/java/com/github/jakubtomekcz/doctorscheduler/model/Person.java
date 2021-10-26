package com.github.jakubtomekcz.doctorscheduler.model;

public record Person(String name) {

    public static Person person(String name) {
        return new Person(name);
    }
}

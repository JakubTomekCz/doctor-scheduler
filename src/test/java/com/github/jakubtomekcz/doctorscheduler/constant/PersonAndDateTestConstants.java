package com.github.jakubtomekcz.doctorscheduler.constant;

import com.github.jakubtomekcz.doctorscheduler.model.Date;
import com.github.jakubtomekcz.doctorscheduler.model.Person;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PersonAndDateTestConstants {

    public static final Person LENNY = new Person("Lenny");
    public static final Person CARL = new Person("Carl");
    public static final Person HOMER = new Person("Homer");
    public static final Person BARNEY = new Person("Barney");

    public static final Date MONDAY = dDayPlusNDays(0);
    public static final Date TUESDAY = dDayPlusNDays(1);
    public static final Date WEDNESDAY = dDayPlusNDays(2);
    public static final Date THURSDAY = dDayPlusNDays(3);
    public static final Date FRIDAY = dDayPlusNDays(4);
    public static final Date SATURDAY = dDayPlusNDays(5);
    public static final Date SUNDAY = dDayPlusNDays(6);

    public static Date date(String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date + "T00:00:00");
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return new Date(java.util.Date.from(instant));
    }

    public static Date dDayPlusNDays(int days) {
        LocalDateTime dDay = LocalDateTime.parse("2021-11-01T00:00:00");
        LocalDateTime dDayPlusNDays = dDay.plusDays(days);
        Instant instant = dDayPlusNDays.atZone(ZoneId.systemDefault()).toInstant();
        return new Date(java.util.Date.from(instant));
    }
}


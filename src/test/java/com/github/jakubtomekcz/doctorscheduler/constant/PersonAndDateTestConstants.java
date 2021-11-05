package com.github.jakubtomekcz.doctorscheduler.constant;

import com.github.jakubtomekcz.doctorscheduler.model.Date;
import com.github.jakubtomekcz.doctorscheduler.model.Person;

import static com.github.jakubtomekcz.doctorscheduler.model.Date.dDayPlusNDays;

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
}


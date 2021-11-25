package com.example.v2ourbook.services;

import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class TimeService {

    public TimeService() {
    }

    public Date calculateDateInNWeeks(int weeks){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, weeks * 7);
        return calendar.getTime();
    }
}

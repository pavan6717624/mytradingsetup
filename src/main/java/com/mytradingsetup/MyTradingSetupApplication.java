package com.mytradingsetup;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyTradingSetupApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyTradingSetupApplication.class, args);
//		for(int i=0;i<200;i++)
//		{
//			System.out.println("<span style=\"text-align:center\"> {{ name["+i+"] }}\n"+
//	        "<canvas id=\"MyChart"+i+"\">{{ chart["+i+"] }}</canvas>\n"+
//	        "</span>");
//		}
//		LocalDate now = LocalDate.now();
//		now = now.plusDays(3);
//		System.out.println(now);
//		getExpiryDate(now);
	}
	
//	public static void getExpiryDate(LocalDate date)
//	{
//		LocalDate lastThrusday = date.with(TemporalAdjusters.lastInMonth(DayOfWeek.THURSDAY));
//	
//		if(lastThrusday.compareTo(date) < 0)
//			lastThrusday = date.plusDays(20).with(TemporalAdjusters.lastInMonth(DayOfWeek.THURSDAY));
//		System.out.println(lastThrusday.compareTo(date)+" "+lastThrusday);
//	}

}

//package ir.ac.tums.mail.util;

package veritesting.benchmarksElena;

import java.util.*;

public class PDate
{
	// public int[]  MonthDay={0,31,28,31,30,31,30,31,31,30,31,30,31};
	static private int[] MonthDayF=
		{
		0,31,31,31,31,31,31,30,30,30,30,30,29};
	static private int[] MonthDay=
		{
		0,31,28,31,30,31,30,31,31,30,31,30,31};

	public static String toGregorianString(int DayF,int MonthF,int YearF) //input is SHAMSI date
	{
		// int YearF;int MonthF;int DayF;
		int YearM=0;
		int MonthM=0;
		int DayM=0;
		int Index;
		boolean Kabise;
		String targetDate;
		//CALL  DateInitial()
		if((YearF+1)%4>0)
		{
			Kabise=false;
		}
		else
		{
			Kabise=true;
		}
		Index=0;
		if(((YearF+2)%4)==0)
		{
			MonthDay[2]=29;
			Index=1;
		}
		YearM=2000+(YearF-1379);
		switch(MonthF)
		{
			case 1:
				MonthM=4;
				DayM=DayF-11;
				break;
			case 2:
				MonthM=5;
				DayM=DayF-10;
				break;
			case 3:
				MonthM=6;
				DayM=DayF-10;
				break;
			case 4:
				MonthM=7;
				DayM=DayF-9;
				break;
			case 5:
				MonthM=8;
				DayM=DayF-9;
				break;
			case 6:
				MonthM=9;
				DayM=DayF-9;
				break;
			case 7:
				MonthM=10;
				DayM=DayF-8;
				break;
			case 8:
				MonthM=11;
				DayM=DayF-9;
				break;
			case 9:
				MonthM=12;
				DayM=DayF-9;
				break;
			case 10:
				MonthM=1;
				DayM=DayF-10-Index;
				YearM=YearM+1;
				break;
			case 11:
				MonthM=2;
				DayM=DayF-11-Index;
				YearM=YearM+1;
				break;
			case 12:
				MonthM=3;
				DayM=DayF-9-Index;
				YearM=YearM+1;
				break;
		}
		if(Kabise)
		{
			DayM=DayM-1;
		}
		if(DayM<1)
		{
			if(MonthM==1)
			{
				YearM=YearM-1;
			}
			MonthM=((MonthM+10)%12)+1;
			DayM=DayM+MonthDay[MonthM];
		}
		if((Index==1)&&(MonthF==10)&&(1<=DayF)&&(DayF<=10))
		{
			DayM=DayM+1;
		}
		if((Index==1)&&(MonthF==12)&&(1<=DayF)&&(DayF<=10))
		{
			DayM=DayM-1;
		}
		MonthDay[2]=28;
		targetDate=YearM+"/";
		if(MonthM<10)
		{
			targetDate=targetDate+"0";
		}
		targetDate=targetDate+MonthM+"/";
		if(DayM<10)
		{
			targetDate=targetDate+"0";
		}
		targetDate=targetDate+DayM;
		return(targetDate);
	}

	public static Date toGregorianDate(int DayF,int MonthF,int YearF)
	{
		// int YearF;int MonthF;int DayF;
		int YearM=0;
		int MonthM=0;
		int DayM=0;
		Date date=new Date(1);
		int Index;
		boolean Kabise;
		String targetDate;
		if((YearF+1)%4>0)
		{
			Kabise=false;
		}
		else
		{
			Kabise=true;
		}
		Index=0;
		if(((YearF+2)%4)==0)
		{
			MonthDay[2]=29;
			Index=1;
		}
		YearM=2000+(YearF-1379);
		switch(MonthF)
		{
			case 1:
				MonthM=4;
				DayM=DayF-11;
				break;
			case 2:
				MonthM=5;
				DayM=DayF-10;
				break;
			case 3:
				MonthM=6;
				DayM=DayF-10;
				break;
			case 4:
				MonthM=7;
				DayM=DayF-9;
				break;
			case 5:
				MonthM=8;
				DayM=DayF-9;
				break;
			case 6:
				MonthM=9;
				DayM=DayF-9;
				break;
			case 7:
				MonthM=10;
				DayM=DayF-8;
				break;
			case 8:
				MonthM=11;
				DayM=DayF-9;
				break;
			case 9:
				MonthM=12;
				DayM=DayF-9;
				break;
			case 10:
				MonthM=1;
				DayM=DayF-10-Index;
				YearM=YearM+1;
				break;
			case 11:
				MonthM=2;
				DayM=DayF-11-Index;
				YearM=YearM+1;
				break;
			case 12:
				MonthM=3;
				DayM=DayF-9-Index;
				YearM=YearM+1;
				break;
		}
		if(Kabise)
		{
			DayM=DayM-1;
		}
		if(DayM<1)
		{
			if(MonthM==1)
			{
				YearM=YearM-1;
			}
			MonthM=((MonthM+10)%12)+1;
			DayM=DayM+MonthDay[MonthM];
		}
		if((Index==1)&&(MonthF==10)&&(1<=DayF)&&(DayF<=10))
		{
			DayM=DayM+1;
		}
		if((Index==1)&&(MonthF==12)&&(1<=DayF)&&(DayF<=10))
		{
			DayM=DayM-1;
		}
		MonthDay[2]=28;
		targetDate=YearM+"/";
		if(MonthM<10)
		{
			targetDate=targetDate+"0";
		}
		targetDate=targetDate+MonthM+"/";
		if(DayM<10)
		{
			targetDate=targetDate+"0";
		}
		targetDate=targetDate+DayM;
		GregorianCalendar gCalendar=new GregorianCalendar();
		gCalendar.set(gCalendar.DAY_OF_MONTH,DayM);
		gCalendar.set(gCalendar.MONTH,MonthM);
		gCalendar.set(gCalendar.YEAR,YearM);
		date=gCalendar.getTime();
		return date;
	}
	/*
		public static void main(String[] args) {
	  Date date = new Date(1);
	  PDate pdate = new PDate();
	  String str = pdate.setShamsi(1,1,1360);
	  date = pdate.setShamsiDate (1,1,1360);
	  java.sql.Date sdate = new java.sql.Date(1);
	  sdate.setTime(date.getTime());
	  System.out.println (sdate.toString() + "    " + str);
	 }
	 */

}
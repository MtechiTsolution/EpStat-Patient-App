package com.example.addpatient;
import java.util.Calendar;

public class AgeCalculation {
    private int startYear;
    private int startMonth;
    private int startDay;
    private int endYear;
    private int endMonth;
    private int endDay;
    private int resYear;
    private int resMonth;
    private int resDay;
    private Calendar start;
    private Calendar end;
    public String getCurrentDate()
    {
        end=Calendar.getInstance();
        endYear=end.get(Calendar.YEAR);
        endMonth=end.get(Calendar.MONTH);
        endMonth++;
        endDay=end.get(Calendar.DAY_OF_MONTH);
        return endDay+":"+endMonth+":"+endYear;
    }
    public void setDateOfBirth(int sYear, int sMonth, int sDay)
    {
        startYear=sYear;
        startMonth=sMonth;
        startMonth++;
        startDay=sDay;

    }
    public void calcualteYear()
    {
        resYear=endYear-startYear;

    }

    public void calcualteMonth()
    {
        if(endMonth>=startMonth)
        {
            resMonth= endMonth-startMonth;
        }
        else
        {
            resMonth=endMonth-startMonth;
            resMonth=12+resMonth;
            resYear--;
        }

    }
    public void  calcualteDay()
    {

        if(endDay>=startDay)
        {
            resDay= endDay-startDay;
        }
        else
        {
            resDay=endDay-startDay;
            resDay=30+resDay;
            if(resMonth==0)
            {
                resMonth=11;
                resYear--;
            }
            else
            {
                resMonth--;
            }

        }
    }

    public String getResult()
    {
        return resYear +" Years " + resMonth + " Months " +resDay + " Days ";
    }

}


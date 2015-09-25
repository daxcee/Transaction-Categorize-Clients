//This is the class for subscription. Each subscription in the input file is an object of class Susbcription.

public class Subscription {
    int subsID, amount, duration; //duration will be set the correct value when method AllSubscriptions.analyzeSubscriptions() is called.
    String start, end;
    int startMonth, startDay, startYear;
    int endMonth, endDay, endYear;
 
    //Constructor   
    Subscription(int subsID, int amount, int duration, String start, String end) {
        this.subsID = subsID;
        this.amount = amount;
        this.duration = duration;
        this.start = start;
        this.end = end;
    }

    //This method determines the subscription type of a subscription (daily, monthly, yearly, or one-off).
    public String subscriptionType() {
        //The date when a subscription appeared the first time. It will be set the correct value when method AllSubscriptions.analyzeSubscriptions() is called.
        String[] startSplit = start.split("/");
        startMonth = Integer.parseInt(startSplit[0]);
        startDay = Integer.parseInt(startSplit[1]);
        startYear = Integer.parseInt(startSplit[2]);

        //The date when a subscription appeared the last time. It will be set the correct value when method AllSubscriptions.analyzeSubscriptions() is called.
        String[] endSplit = end.split("/");
        endMonth = Integer.parseInt(endSplit[0]);
        endDay = Integer.parseInt(endSplit[1]);
        endYear = Integer.parseInt(endSplit[2]);

        //Count the number of years/months/days from the start date to the end date of a subscription.
        int numYears = endYear - startYear + 1;
        int numMonths = endMonth - startMonth + 12 * (endYear - startYear) + 1;
        int numDays = days(endMonth, endDay, endYear) - days(startMonth, startDay, startYear) + 1;

        //Determine the type of a subscription. For example if duration equals one, it is a one-off subscriptioin. If duration equals the number of years that the susbscription lasted, then it is a yearly subscription, etc. 
        if(duration == 1)
            return "one-off";
        else if(duration == numYears)
            return "yearly";
        else if(duration == numMonths)
            return "monthly";
        else if(duration == numDays)
            return "daily";
        else
            return "undefined type";
    }

    private int days(int m, int d, int y) {
        m = (m + 9) % 12;
        y = y - m/10;
        return 365*y + y/4 - y/100 + y/400 + (m*306 + 5)/10 + ( d - 1 );
    }

}

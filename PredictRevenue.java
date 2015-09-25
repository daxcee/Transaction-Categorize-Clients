//This class solves the second bonus question: Predict annual revenue for year 2015 (based on historical retention and new subscribers).

import java.util.*;
import java.io.*;

public class PredictRevenue {
    public void analyzePredictRevenue(String inputName, String outputName1, String outputName2) throws FileNotFoundException {

        //Input file.
        File inputFile = new File(inputName);
        Scanner input = new Scanner(inputFile);
        input.useDelimiter("[,\n]");

        //First output file. Outputs the number of each subscription type in each year, and the average amount of each subscription type.
        File outputFile1 = new File(outputName1);
        if(outputFile1.exists()) {
            System.out.println("File \""+outputName1+"\" already exists.");
            System.exit(1);
        }
        PrintWriter output1 = new PrintWriter(outputFile1);

        //Second output file. Outputs the number of three different yearly subscription types for each year (see explanation below), and the final answer of this question: the predicted annual revenue for year 2015.
        //The three different yearly subscription types for a certain year are:
        //1. The yearly subscriptions that start in this year,
        //2. The yearly subscriptions that end in this year,
        //3. The yearly subscriptions that goes through this year, i.e., the subscription's start year < this year < the subscription's end year.
        File outputFile2 = new File(outputName2);
        if(outputFile2.exists()) {
            System.out.println("File \""+outputName2+"\" already exists.");
            System.exit(1);
        }
        PrintWriter output2 = new PrintWriter(outputFile2);

        //subscriptionMap is the result of running the method AllSubscriptions.analyzeSubscriptions(). This Map stores all the subscriptions. Each entry is a subscription. The key is the subscription ID, and the value is an object of Subscription of this subscription.
        Map<Integer, Subscription> subscriptionMap = new HashMap<>();
        AllSubscriptions allSubscriptions = new AllSubscriptions();
        boolean outputSubscriptionsOrNot = false;

        try{
            subscriptionMap = allSubscriptions.analyzeSubscriptions(inputName, "", outputSubscriptionsOrNot);
        }

        catch(FileNotFoundException ex) {
            System.out.println("File \""+inputName+"\" not found!");
            System.exit(1);
        }

        //Read from the input file "subscription_report.csv", skip the first row which are not numbers.
        for(int i = 0; i < 4; i++)
            input.next();

        int subsID = 0, amount = 0;

        //totoalAmountDaily is the total amount of all daily subscriptions, same with totalAmountMonthly, etc. 
        int totalAmountDaily = 0, totalAmountMonthly = 0, totalAmountYearly = 0, totalAmountOneOff = 0;

        //totoalNumDaily is the total number of all daily subscriptions, same with totalNumMonthly, etc. 
        int totalNumDaily = 0, totalNumMonthly = 0, totalNumYearly = 0, totalNumOneOff = 0;
        String transDate = "";
        int startYear = 1966, endYear = 2014;
        int numberOfYears = endYear - startYear + 1;

        //Array numDaily stores the number of daily subscriptions in each year, with the index being (year - 1966), same with numMonthly, etc.
        int[] numDaily = new int[numberOfYears];
        int[] numMonthly = new int[numberOfYears];
        int[] numYearly = new int[numberOfYears];
        int[] numOneOff = new int[numberOfYears];

        //Array numStart stores the number of the first yearly subscription type described above (i.e., the yearly subscriptions that start in this year) for each year, with the index being (year - 1966), same with numEnd and numThrough.
        int[] numStart = new int[numberOfYears];
        int[] numEnd = new int[numberOfYears];
        int[] numThrough = new int[numberOfYears];

        //Array numTotal stores the total number of the three different yearly subscription types described above. It is also the number of yearly subscriptions in each year.
        int[] numTotal = new int[numberOfYears];

        int j = 0;

        while(input.hasNext()) {
            String item = input.next();

            if(j % 4 == 1) {
                subsID = Integer.parseInt(item);
            }

            if(j % 4 == 2) {
                amount = Integer.parseInt(item);
            }

            if(j % 4 == 3) {
                transDate = item;
                int year = Integer.parseInt(transDate.split("/")[2]);
                
                Subscription subscription = subscriptionMap.get(subsID);
                String subsType = subscription.subscriptionType();

                //Calculate the array elements of arrays numDaily, totalAmountDaily, totalNumDaily, etc. The meanings of these arrays were described above.
                switch(subsType) {
                    case "daily" : numDaily[year - startYear]++;
                                   totalAmountDaily += amount;
                                   totalNumDaily++;
                                   break;
                    case "monthly" : numMonthly[year - startYear]++;
                                     totalAmountMonthly += amount;
                                     totalNumMonthly++;
                                     break;
                    case "yearly" : numYearly[year - startYear]++;
                                    totalAmountYearly += amount;
                                    totalNumYearly++;
                                    break;
                    case "one-off" : numOneOff[year - startYear]++;
                                     totalAmountOneOff += amount;
                                     totalNumOneOff++;
                                     break;
                    default : System.out.println("Undefined type!");
                              System.exit(1);
                }
            }

            j++;

        }

        //Calculate the array elements of numStart, numEnd, numThrough. The meanings of these arrays were described above.
        for(Map.Entry<Integer, Subscription> entry : subscriptionMap.entrySet()) {
            Subscription subscription = entry.getValue();
            if(subscription.subscriptionType() == "yearly") {
                numStart[subscription.startYear - startYear]++;
                numEnd[subscription.endYear - startYear]++;
                for(int i = subscription.startYear + 1; i <= subscription.endYear - 1; i++) {
                    numThrough[i - startYear]++;
                }
            }
        }

        //Ouput results.

        //output1 stores the number of each subscription type in each year, and the average amount of each subscription type.
        output1.println("Year, # of daily subs in this year, # of monthly subs in this year, # of yearly subs in this year, # of one-off subs in this year");

        //output2 stores the number of three different yearly subscription types for each year (see explanation below), and the final answer of this question: the predicted annual revenue for year 2015.
        output2.println("Year, # of subs started in this year, # of subs ended in this year, # of yearly through this year, # of total yearly this year");

        for(int i = 0; i < numberOfYears; i++) {
            int currentYear = i + startYear;

            output1.println(currentYear + ", " + numDaily[i] + ", " + numMonthly[i] + ", " + numYearly[i] + ", " + numOneOff[i]);

            numTotal[i] = numStart[i] + numEnd[i] + numThrough[i];
            output2.println(currentYear + ", " + numStart[i] + ", " + numEnd[i] + ", " + numThrough[i] + ", " + numTotal[i]);

        }
        
        output1.println("Average amount of daily subs, Average amount of monthly subs, Average amount of yearly subs, Average amount of one-off subs");
        output1.println(totalAmountDaily / totalNumDaily + ", " + totalAmountMonthly / totalNumMonthly + ", " + totalAmountYearly / totalNumYearly + ", " + totalAmountOneOff / totalNumOneOff);
        
        //Calculate and output the predicted annual revenue for year 2015 in output2.
        double averageAmountYearly = totalAmountYearly / totalNumYearly;
        double averageNumEnd = (double) (numEnd[numberOfYears - 1] + numEnd[numberOfYears - 2]) / 2.0;
        double predictedRevenue = averageNumEnd * averageAmountYearly;
        output2.println("Predicted annual revenue for year 2015:");
        output2.println(predictedRevenue);
        

        output1.close();   
        output2.close();   
    }


}

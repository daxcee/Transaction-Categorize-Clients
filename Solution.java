//This class controls the running of all methods.

import java.io.*;

public class Solution {
    public static void main(String[] args) {
        String inputName = "subscription_report.csv";

        //Define classes for each question
        AllSubscriptions allSubscriptions = new AllSubscriptions();
        Revenues revenues = new Revenues();
        PredictRevenue predictRevenue = new PredictRevenue();

        boolean outputSubscriptionsOrNot = true;

        //Call methods in each class to solve each question
        try{
            allSubscriptions.analyzeSubscriptions(inputName, "outputSubscriptions.csv", outputSubscriptionsOrNot);
            revenues.analyzeRevenues(inputName,"outputRevenues.csv");
            predictRevenue.analyzePredictRevenue(inputName, "outputNumSubsEachType.csv", "outputPredictRevenue.csv");
        }

        catch(FileNotFoundException ex) {
            System.out.println("File \""+inputName+"\" not found!");
            System.exit(1);
        }

    }  
}

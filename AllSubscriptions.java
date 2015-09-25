//This class solves the basic question: output a list of subscription IDs, their subscription type (daily, monthly, yearly, one-off), and the duration of their subscription.

import java.util.*;
import java.io.*;

public class AllSubscriptions {
    public Map<Integer, Subscription> analyzeSubscriptions(String inputName, String outputName, boolean outputSubscriptionOrNot) throws FileNotFoundException {

        //The input file.
        File inputFile = new File(inputName);
        Scanner input = new Scanner(inputFile);
        input.useDelimiter("[,\n]");

        //Read from the input file "subscription_report.csv", skip the first row which are not numbers.
        for(int i = 0; i < 4; i++)
        input.next();

        int subsID = 0;
        int amount = 0;
        String transDate = "";

        //subscriptionMap stores all the subscriptions. Each entry is a subscription. The key is the subscription ID, and the value is an object of Subscription of this subscription.
        Map<Integer, Subscription> subscriptionMap = new HashMap<>();
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
                //If this subscription is not in subscriptionMap, then create an entry for this subsciption in subscriptionMap, otherwise increase the duration by 1 and update the end date of the subscription.
                if(!subscriptionMap.containsKey(subsID)) {
                    Subscription subs = new Subscription(subsID, amount, 1, transDate, transDate);
                    subscriptionMap.put(subsID, subs);
                } else {
                    Subscription subs = subscriptionMap.get(subsID);
                    subs.duration++;
                    subs.end = transDate;
                }
        
          }
      
          j++;
        }
        input.close();

        if(outputSubscriptionOrNot == true) {
            //The output file
            File outputFile = new File(outputName);
            if(outputFile.exists()) {
                System.out.println("File \""+outputName+"\" already exists.");
                System.exit(1);
            }
            PrintWriter output = new PrintWriter(outputFile);

            //Output the results
            output.println("Subscription ID, Subscription type, Duration");

            for(Map.Entry<Integer, Subscription> entry : subscriptionMap.entrySet()) {
                int subscriptionID = entry.getKey();
                Subscription subscription = entry.getValue();
                String typeValue = subscription.subscriptionType();
                String unit = "";

                switch(typeValue) {
                    case "daily" : unit = "days";
                                   break;
                    case "monthly" : unit = "months";
                                     break;
                    case "yearly" : unit = "years";
                                    break;
                    case "one-off" : unit = "time";
                                    break;
                    case "undefined type" : unit = "undefined type";
                                    break;
                    default: System.out.println("Error: subscription type does not match.");
                             System.exit(1);
                }     

                output.println(subscriptionID+", "+typeValue+", "+subscription.duration+" "+unit);
            }
            output.close();
        }

        return subscriptionMap;
    }

}

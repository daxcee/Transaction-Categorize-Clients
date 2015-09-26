# Transaction-Categorize-Clients

This is the solution to the MindSumo challenge for Capital One: Use transaction data to categorize clients:
https://www.mindsumo.com/contests/credit-card-transactions

Five Java classes were written to solve this project. Three of these classes solves the three questions (including the bonus questions) respectively. Two other classes are helper classes. The classes are explained below. The three questions are solved with time and space complexity O(n), where n is the number of subscriptions. It took roughly 8 seconds to run all the codes for all the questions in my laptop.

*****************************
**  1. The class Solution  **
*****************************

This is a short helper class. It contains the main method which controls the running of all methods that solves the three questions.

*********************************
**  2. The class Subscription  **
*********************************
 
This is a helper class for subscription. Each subscription in the input file is an object of class Susbcription. 

The data memembers are the subsctiption ID, the amount, and the date when this susbcription first appeared, and the date when this subscription last appeared. The member method subscriptionType() returns the type (daily/monthly/yearly/one-off) of a subscription. 

The algorithm of the method subscriptionType() is:

    (1) Count the duration of this subscription. This is done in the class AllSubscriptions: the duration is updated to the correct value when its memember method AllSubscriptions.analyzeSubscriptions() is called. 
    (2) Find the the date when this susbcription first appeared, and the date when this subscription last appeared. This is also done in the class AllSubscriptions: these dates are updated to the correct values when its memember method AllSubscriptions.analyzeSubscriptions() is called. 
    (3) From these two dates we can calculate the number of days/months/years that this subscription has lasted. 
    (4) By comparing duration with thethe number of days/months/years that this subscription has lasted, we can determine the subscription type. If duration equals one, then it is a one-off subscription. If duration equals the number of years this subscription has lasted, then it is a yearly subscription, etc.

*************************************
**  3. The class AllSubscriptions  **
*************************************

This class solves the basic question: output a list of subscription IDs, their subscription type (daily, monthly, yearly, one-off), and the duration of their subscription. 

The memember method analyzeSubscriptions takes the input and out files as input parameters. This method creates an output file "outputSubscriptions.csv" (attached) which contains the list required by the project question. In the output file, each line is a subscription, which contains the subscription ID, the subscription type, and the duration. 

The first few lines in the output file are:

-- File starts --

Subscription ID, Subscription type, Duration

3159, monthly, 85 months

3160, monthly, 68 months

3164, one-off, 1 time

3165, daily, 31 days

... ...


-- File ends --

This method also returns a HashMap which stores all the subscriptions. Each entry is a subscription. The key is the subscription ID, and the value is an object of Subscription of this subscription. The object conatins all information about this subscription, like subscription type, duration, start and end date. This HashMap will be used in other classes.

The algorithm of the method analyzeSubscriptions() is:

    (1) Define the input file, and read from the input file "subscription_report.csv", skip the first row which are not numbers. 
    (2) Define a HashMap. For each line in the input file, read the subscription ID, the amount and the date. If the current subscription does not exist in the HashMap, create it. Otherwise update the duration and end date of this subscription in the HashMap.
    (3) Define the output file. Read entries in the HashMap. Each entry corresponds to a susbcription. Output the subscriptions in the format shown above.

*****************************
**  4. The class Revenues  **
*****************************

This class solves the first bonus question: Give annual revenue numbers for all years between 1966 and 2014. Which years had the highest revenue growth, and highest revenue loss?

The memeber method analyzeRevenues() takes input and out files as input parameters. This method creates an output file "outputRevenues.csv"  (attached)  which contains the data required by the project question. In the output file, each line contains the year and the annual revenue of that year. At the end of the output file, there are also years which had the highest revenue growth, and years which had the highest revenue loss. 

The first and last few lines of the output file are:

-- File starts --

Year, Annual revenue

1966, 36431250

1967, 55206230

1968, 68890920

1969, 77045920

... ...

Years had the highest revenue growth:

1967

Years had the highest revenue loss:

1991

-- File ends --

The algorithm of the method analyzeSubscriptions() is:

    (1) Define the input and output files. Read from the input file "subscription_report.csv", skip the first row which are not numbers. 
    (2) Define an array "revenues" to store the revenue in each year. The index of the array is (year - 1966) and the value is the revenue in that year. 
    (3) For each line in the input file, read the amount and the year. Add the current amount to revenues[year - startYear]. After reading all the lines in the input file, revenues[year - startYear] should equal the total revenue in that year.
    (4) Define an array to store the growth/loss in each year. 
    (5) Define the variable "highestGrowth" with inital value 0, and define the variable "highestLoss" with inital value 0.
    (6) A loop starting from year 1966 and ending in year 2014. The growth/loss in year (i + 1966) can be calculated as revenues[i] - revenues[i - 1].  
    (7) If the growth/loss in current year is greater than the highestGrowth/highestLoss, update the value of highestGrowth/highestLoss to be the growth/loss in current year.
    (8) After the loop, the variables highestGrowth and highestLoss should have the correct value. We can obtain the corresponding years from the index.
    (9) Output the results in the format shown above.

***********************************
**  5. The class PredictRevenue  ** 
***********************************

This class solves the second bonus question: Predict annual revenue for year 2015 (based on historical retention and new subscribers). The answer I got is that the predicted annual revenue for year 2015 is around 888305.5 (USD).

The memeber method analyzePredictRevenue() takes input and out files as input parameters. This method creates two output files: "outputNumSubsEachType.csv" and "outputPredictRevenue.csv"  (attached) . These two output files will be explained later.

**** General idea ****

My idea to solve this problem is as following. First study the behaviour of each subscription type. More specifically, the number of subscriptions of each type in each year. The attached file "plot-number-of-each-type-vs-year.pdf" is a plot of such information. The horizontal axis is year, and the vertical axis is number of each subscription type in the corresponding year. From the plot, we can draw the following two interesting conclusions:

    (a) Different subscription types behave very differently as a function of year. So we need to consider them seperately when making predictions of the revenue.
    (b) The number of daily, monthly, and one-off subscriptions fall to zero after some year. The only subscription with non-zero number after year 2000 is the yearly subscription. So we only need to consider yearly subscriptions when making predictions of the revenue. This simplies our work considerably. 

Then we look at the yearly subscriptions into more depth. For each year, it has three different types of yearly subscriptions:

    (a) The yearly subscriptions that start in the current year. These subscriptions will continue to the next year, because otherwise they would not be "yearly" but "one-off" subscriptions. I would call them the "Started" type.
    (b) The yearly subscriptions that end in the current year. These subscription will not continue to the next year, because they have already ended in the current year. I would call them the "Ended" type.
    (c) The yearly subscriptions that goes through the current year, i.e., the subscription's start year < this year < the subscription's end year. These subscriptions will continue to the next year, because they haven't ended. I would call them the "Through" type.

I also made a plot of these three types, which are in the attached file "plot-predict-revenue.pdf". From the plot, we can also make the similar two conclusions as in the first plot:

    (a) Different yearly subscription types behave very differently as a function of year. So we need to consider them seperately when making predictions of the revenue.
    (b) The "Started" type and "Through" type fall to zero after some year. So we only need to consider the "Ended" type in the year 2015 when making predictions of the revenue in that year. This  again simplies our work considerably. 

If we look at the curve of the "Ended" type, we see that it is almost a constant (about 240) after year 2000, so the number of "Ended" type in 2015 should also be close to that constant (240). So we can calculate the predicted revenue in 2015 as folloing:

Predicted revenue in 2015 = Number of "Ended" yearly type in 2015 predicted from the constant (240) * Average amount of yearly subscriptions, which is roughly 888305.5 (USD).

In the code, to be more accurate, I took the average of the "Ended" type in 2013 and 2014 to be the predicted number of the "Ended" type in 2015.

**** Algorithm ****

The algorithm of the method analyzePredictRevenue() is:
    (1) Define the input and output files. 
    (2) Get a HashMap from the return value of calling AllSubscriptions.analyzeSubscriptions(). This HashMap stores all the subscriptions. Each entry is a subscription. The key is the subscription ID, and the value is an object of Subscription of this subscription.
    (3) Read from the input file "subscription_report.csv", skip the first row which are not numbers. 
    (4) For each line in the input file, read the subscription ID, the amount, and the year. 
    (5) Look up the subscription ID in the HashMap, the value returned from the HashMap is the subscription object. This object contains the type of this subscription. 
    (6) If the subscription type is daily, then increase the number of daily subscriptions in this year by one, increase the total number of daily subscriptions by one, and add the amount to the total amount of daily subscriptions. 
    (7) Traverse the HashMap. For each yearly subscription in the HashMap, we can obtain the start year and end year (those are member data in the Subscription class). Then we increase the number of "Started" type in the start year of this subscription by one, increase the number of "Ended" type in the end year of this subscription by one, and increase the number of "Through" type in all the years that this subscription has passed trough by one.
    (8) Calculate the average amount of yearly subscriptions, and the predicted number of "Ended" yearly types in 2015 (in the way described in the "General idea" part above), and calculate the predicted revenue in 2015 (in the way described in the "General idea" part above).
    (9) Output in the format described below.

**** Output files description ****

The output file "outputNumSubsEachType.csv" contains the number of each subscription type in each year, and the average amount of each subscription type. 

The first and last few lines of the output file "outputNumSubsEachType.csv" are:

-- File starts --

Year, # of daily subs in this year, # of monthly subs in this year, # of yearly subs in this year, # of one-off subs in this year

1966, 7277, 2254, 334, 179

1967, 7349, 6096, 636, 189

1968, 8140, 9424, 923, 194

1969, 6949, 12539, 1222, 152

... ...

Average amount of daily subs, Average amount of monthly subs, Average amount of yearly subs, Average amount of one-off subs

3658, 3706, 3709, 3736

-- File ends --

The output file "outputPredictRevenue.csv" contains the number of three different yearly subscription types for each year (see explanation below), and the final answer of this question: the predicted annual revenue for year 2015.

The three different yearly subscription types for a certain year are:

The first and last few lines of the output file "outputNumSubsEachType.csv" are:

-- File starts --

Year, # of subs started in this year, # of subs ended in this year, # of yearly through this year

1966, 334, 0, 0, 334

1967, 302, 0, 334, 636

1968, 287, 0, 636, 923

1969, 299, 6, 917, 1222

... ...

Predicted annual revenue for year 2015:
888305.5

-- File ends --


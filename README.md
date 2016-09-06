# Airline-Data-Analysis

1) Cheapest Airline -- For N = 1 and N = 200 representing scheduled flight minutes.  For each year and each carrier, 
estimate the intercept and the slope of a simple linear regression using the scheduled flight time to explain average
ticket prices. For a given year, compare carriers at N by computing intercept+slope*N. The least expensive carrier is 
the carrier with lowest price at N for the most years. 

2) Connections and Missed Connections -- A connection is any pair of flight F and G of the same carrier such as 
F.Destination = G.Origin and the scheduled departure of G is  <= 6 hours and >= 30 minutes after the scheduled arrival of F. 
A connection is missed when the actual arrival of F < 30 minutes before the actual departure of G. 
Output the number of connections and missed connections per airline, per year. 

3) Prediction of Flight Delay -- Build a model based on Random Forest algorithm. Train the model with the historical dataset.
Test and validate the future delays. As a measure of accuracy, use the sum of the percentage of on-time flights misclassified 
as delayed and the percentage of delayed flights misclassified as on-time.

4) Propose two hop routes between Source and Destination -- Build model based on historical file which contains scheduled flights 
for the next year, to create itineraries, and one request file in the following format  year, month, day, origin, destination, 
ignore. For each request, propose an itinerary, written flight_num, flight_num, duration.  

5) Spark implementation of Cheapest Airline -- For N = 1 and N = 200 representing scheduled flight minutes.  For each year and each carrier, 
estimate the intercept and the slope of a simple linear regression using the scheduled flight time to explain average
ticket prices. For a given year, compare carriers at N by computing intercept+slope*N. The least expensive carrier is 
the carrier with lowest price at N for the most years. 


Historical data set can be obtained from http://www.rita.dot.gov/bts/sites/rita.dot.gov.bts/files/subject_areas/airline_information/index.html

All program uses AWS amazon EC2 machines to achieve parallelism. S3 storage to store all the dataset.

## Combined Count/Time-based Sliding Window 

This project illustrates how to work with sliding windows in Flink using a count-based slide and a time-based window length (e.g. W(l=30 min, s= 1)).
Consider a stream of user event data where you need to process the events of the last 15 minutes whenever a new record arrives. 


## Example Application
The provided example application reads [Uber trip data](https://github.com/fivethirtyeight/uber-tlc-foil-response) that summarizes the Uber pickups during July 2014.
It is structured as follows:

``` "Date/Time","Lat","Lon","Base" ```

For every ten records that arrive the stream processor, the count of pickups per base of the last thirty minutes is emitted. 
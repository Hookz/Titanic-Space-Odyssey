package sample;


import java.io.*;
import java.util.*;

//Change file name to Trajectory calculation.
public class Trajectory {

    public long launchDateToT;
    public long launchDateToE;
    public static Vector3D fromEarth;
    public static Vector3D toTitan;
    public static Vector3D earthToTitan;
    public static long launchToTitan;
    public static long landOnTitan;
    public static Vector3D titanToEarth;
    public static long launchToEarth;
    public static long landOnEarth;
    public static long timeToTitan;
    public static long timeToEarth;

    public static long asLong(String str){
        long timeOfLaunch;
        char[] timeTArr;
        String years="";
        String days="";
        String hours="";
        String minutes="";
        String seconds="";
        long yearsL;
        long daysL;
        long hoursL;
        long minutesL;
        long secondsL;

        timeTArr = str.toCharArray();

        for (int i = 1; i<timeTArr.length; i++) {
            if (5 < i && i < 14) {
                years += Character.toString(timeTArr[i]);
            }
            if (20 < i &&i < 24) {
                days += timeTArr[i];
            }
            if (31 < i && i < 34) {
                hours += timeTArr[i];
            }
            if (43<i && i < 46) {
                minutes += timeTArr[i];
            }
            if (56 < i){
                seconds += timeTArr[i];
            }
        }
        yearsL = Long.parseLong(years);
        daysL = Long.parseLong(days);
        hoursL = Long.parseLong(hours);
        minutesL = Long.parseLong(minutes);
        secondsL = Long.parseLong(seconds);
        timeOfLaunch = (yearsL*365*24*60*60+daysL*24*60*60+hoursL*60*60+minutesL*60+secondsL);
        return timeOfLaunch;
    }

    public static void pleaseJustRun(){
        List<Vector3D> earthPositions = new ArrayList<>();
        List<Vector3D> titanPositions = new ArrayList<>();
        List<Vector3D> earthVelocity = new ArrayList<>();
        List<Vector3D> titanVelocity = new ArrayList<>();
        List<String> time = new ArrayList<>();
        final String REMOVE = "at time: ";

        File in = new File("coordinates.txt");
        Scanner read = null;
        try {
            read = new Scanner(in);
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        }


        //store all data points of universe in the ArrayList.
        while(read.hasNextLine()) {
            String readLine = read.nextLine();
            Vector3D temp = new Vector3D();

            if(readLine.equals("Titan: position ") ){
                String[] splitter = read.nextLine().split("\\s");
                temp.x = Double.parseDouble(splitter[2]);

                splitter = read.nextLine().split("\\s");
                temp.y = Double.parseDouble(splitter[2]);

                splitter = read.nextLine().split("\\s");
                temp.z = Double.parseDouble(splitter[2]);

                titanPositions.add(temp);
            }
            else if(readLine.equals("Titan: velocity ")){
                String[] splitter = read.nextLine().split("\\s");
                temp.x = Double.parseDouble(splitter[2]);

                splitter = read.nextLine().split("\\s");
                temp.y = Double.parseDouble(splitter[2]);

                splitter = read.nextLine().split("\\s");
                temp.z = Double.parseDouble(splitter[2]);

                titanVelocity.add(temp);
            }
            else if(readLine.equals("Earth: position: ")){
                String[] splitter = read.nextLine().split("\\s");
                temp.x = Double.parseDouble(splitter[2]);

                splitter = read.nextLine().split("\\s");
                temp.y = Double.parseDouble(splitter[2]);

                splitter = read.nextLine().split("\\s");
                temp.z = Double.parseDouble(splitter[2]);

                earthPositions.add(temp);
            }
            else if(readLine.equals("Earth: velocity ")){
                String[] splitter = read.nextLine().split("\\s");
                temp.x = Double.parseDouble(splitter[2]);

                splitter = read.nextLine().split("\\s");
                temp.y = Double.parseDouble(splitter[2]);

                splitter = read.nextLine().split("\\s");
                temp.z = Double.parseDouble(splitter[2]);

                earthVelocity.add(temp);
            }
            else if(readLine.substring(0,2).equals("at")){ //
//                String line = read.nextLine();

                readLine = readLine.replaceAll(REMOVE, "");
                time.add(readLine);
            }
        }

        read.close();

        //find min distance
        double minDistance = Double.MAX_VALUE;
        int stoppingIndex =0;
        Vector3D tempEarth = new Vector3D();
        Vector3D tempTitan = new Vector3D();
        String tempTime = null;
        String landingDate = null;
        //find min distance between the interval

        for(int i = 5; i < titanPositions.size(); i++) {
            double distance = titanPositions.get(i).distance(earthPositions.get(i-5));

            if(distance < minDistance) {
                tempEarth = earthPositions.get(i-5);
                tempTitan = titanPositions.get(i);
                earthToTitan = tempTitan.sub(tempEarth);
                launchToTitan = Trajectory.asLong(time.get(i-5));
                landOnTitan = Trajectory.asLong(time.get(i));
                tempTime = time.get(i-5);
                landingDate = time.get(i);
                minDistance = distance;
                stoppingIndex = i;
            }
        }

        System.out.println("___Earth to Titan____");

        System.out.println("The distance is of travel: " + minDistance);
        System.out.println("At launch earth position is (x, y, z): " + "(" + tempEarth.x + ", " + tempEarth.y + ", " + tempEarth.z + ")");
        System.out.println("The titan position at landing is (x, y, z): " + "(" + tempTitan.x + ", " + tempTitan.y + ", " + tempTitan.z + ")");
        System.out.println("time of launch " + tempTime);
        System.out.println("time of landing " + landingDate);

        System.out.println("___TITAN TO EARTH____");
        //other way

        Vector3D toEarth = null;
        Vector3D fromTitan = null;
        String time2 = null;
        double newMinDistance = Double.MAX_VALUE;
        for(int i = stoppingIndex+5; i < earthPositions.size(); i++) {
            double distance = earthPositions.get(i).distance(titanPositions.get(i-5));

            if(distance < newMinDistance) {
                toEarth = earthPositions.get(i);
                fromTitan = titanPositions.get(i-5);
                titanToEarth = toEarth.sub(fromTitan);
                launchToEarth = Trajectory.asLong(time.get(i-5));
                landOnEarth = Trajectory.asLong(time.get(i));
                newMinDistance = distance;
                time2 = time.get(i);
            }
        }
        System.out.println(time2);
        System.out.println("The distance is of travel: " + newMinDistance);
        System.out.println("At launch earth position is (x, y, z): " + "(" + toEarth.x + ", " + toEarth.y + ", " + toEarth.z + ")");
        System.out.println("The titan position at landing is (x, y, z): " + "(" + fromTitan.x + ", " + fromTitan.y + ", " + fromTitan.z + ")");
        System.out.println("time of launch " + launchToEarth + " seconds");
        timeToTitan = landOnTitan - launchToTitan;
        timeToEarth = landOnEarth - launchToEarth;
       }


}

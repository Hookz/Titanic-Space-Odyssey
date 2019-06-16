package sample;

import java.io.*;
import java.util.*;

//Change file name to Trajectory calculation.
public class Trajectory {
    public static void main(String args[]) throws FileNotFoundException {
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
        int stopingIndex =0;
        Vector3D tempEarth = new Vector3D();
        Vector3D tempTitan = new Vector3D();
        String tempTime = null;
        String landingDate = null;
        //find min distance between the interval
        for(int i = 1; i < titanPositions.size(); i++) {
            double distance = titanPositions.get(i).distance(earthPositions.get(i-1));

            if(distance < minDistance) {
                tempEarth = earthPositions.get(i-1);
                tempTitan = titanPositions.get(i);
                tempTime = time.get(i-1);
                landingDate = time.get(i);
                minDistance = distance;
                stopingIndex = i;
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
        double newMinDistance = Double.MAX_VALUE;
        Vector3D finalEarth = new Vector3D();
        Vector3D finalTitan = new Vector3D();
        String launchTitan = null;
        String landingEarth = null;
        for(int i = stopingIndex+2; i < earthPositions.size(); i++) {
            double distance = earthPositions.get(i).distance(titanPositions.get(i-1));

            if(distance < newMinDistance) {
                finalEarth = earthPositions.get(i);
                finalTitan = titanPositions.get(i-1);
                launchTitan = time.get(i-1);
                landingEarth = time.get(i);
                newMinDistance = distance;

            }
        }

        System.out.println("The distance is of travel: " + newMinDistance);
        System.out.println("At launch earth position is (x, y, z): " + "(" + finalEarth.x + ", " + finalEarth.y + ", " + finalEarth.z + ")");
        System.out.println("The titan position at landing is (x, y, z): " + "(" + finalTitan.x + ", " + finalTitan.y + ", " + finalTitan.z + ")");
        System.out.println("time of launch " + launchTitan);
        System.out.println("time of landing " + landingEarth);



    }
}

/*
Name: Pavan Athreya Narasimha Murthy
USD ID: 9129210968
Email: pavan.athreya@usc.edu
Course: CSCI 576
Instructor: Prof. Parag Havaldar
Semester: Fall 2018
Project: Assignment 1
*/

//package com.CSCI576.Assigment1;

////
//  Part two of the Assignment 1 - Fall 2018 - CSCI 576
////
public class MyPart2 {

    ////
    //  Entry point of the program
    ////
    public static void main(String[] args) {
        System.out.println("Welcome to part two of Assignment 1: CSCI576 || Fall 2018 || Pavan Athreya || pavanatn@usc.edu");
        if (args.length == 3){
            ImageDisplay ren = new ImageDisplay();
            ren.RotationSpeed = ParseRotationsPerSecond(args[1]);
            ren.FramesPerSecond = ParseFramesPerSecond(args[2]);
            ren.NumberOfLines = ParseNumberOfLines(args[0])/2;
            ren.AntiAliasing = false;
            ren.ScalingFactor = 1.0;
            ren.labelText1 = "Original Video (Left)";
            ren.labelText2 = "Video after modification (Right)";
            ren.ExecutionMode = 2;//Video Mode
            ren.samplePeriod = (int)(1000/ren.FramesPerSecond);
            ren.UpdateDegree = (360*ren.RotationSpeed)/1000;
            ren.showIms(args);
            ren.StartRenderingVideo(ren);
        }else {
            UsageInformation();
        }
    }

    ////
    //  Prints the usage information
    ////
    public static void UsageInformation(){
        System.out.println("Invalid Usage of the program");
        System.out.println("Usage Information:");
        System.out.println("java MyPart2 \"Number of Lines\" \"Rotations per second\" \"Frames Per Second\"");
        System.exit(1);
    }

    ////
    //  Parse Number of lines to draw from Command Line
    ////
    private static int ParseNumberOfLines(String NumberOfLines){
        System.out.println("Parsing the number of lines required to draw");
        int lines = Integer.parseInt(NumberOfLines);
        if (lines > 0){
            return lines;
        }else {
            System.out.println("Invalid Number of lines");
            UsageInformation();
        }
        return 0;
    }

    ////
    //  Parse Speed of rotation to draw from Command Line
    ////
    private static double ParseRotationsPerSecond(String rps){
        System.out.println("Parsing the rotations per second");
        double rotations = Double.parseDouble(rps);
        if (rotations > 0 & rotations < 20){
            System.out.printf("Rotations per second: %f\n", rotations);
            return rotations;
        }else{
            System.out.println("Invalid speed of rotation. It is difficult to sample at this rate");
            UsageInformation();
        }
        return 0;
    }

    ////
    //  Parse frames per second to sample from Command Line
    ////
    private static double ParseFramesPerSecond(String fps){
        System.out.println("Parsing Frames Per Second Parameter");
        double frames = Double.parseDouble(fps);
        if(frames > 0){
            System.out.printf("Frame Per Second: %f\n",frames);
            return frames;
        }else{
            UsageInformation();
        }
        return 0;
    }
}

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
//  Extra Credit Part of the Assignment 1 - Fall 2018 - CSCI 576
////
public class MyExtraCredit {

    static int width = 512;
    static int height = 512;

    ////
    //  Entry point of the program
    ////
    public static void main(String[] args) {
        System.out.println("Welcome to part three(Extra Credit) of Assignment 1: CSCI576 || Fall 2018 || Pavan Athreya || pavanatn@usc.edu");
        if (args.length == 5){
            ImageDisplay ren = new ImageDisplay();
            ren.RotationSpeed = ParseRotationsPerSecond(args[1]);
            ren.FramesPerSecond = ParseFramesPerSecond(args[2]);
            ren.NumberOfLines = ParseNumberOfLines(args[0])/2;
            ren.ScalingFactor = ParseScalingFactor(args[3]);
            ren.AntiAliasing = ParseAntiAliasing(args[4]);
            ren.labelText1 = "Original Video (Left)";
            ren.labelText2 = "Video after modification (Right)";
            ren.ExecutionMode = 3;//Video Mode for EXtra Credit
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
        System.out.println("java MyExtraCredit \"Number of Lines\" \"Rotations per second\" \"Frames Per Second\"");
        System.exit(1);
    }

    ////
    //  Parse Number of lines to draw from Command Line
    ////
    public static int ParseNumberOfLines(String NumberOfLines){
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
    public static double ParseRotationsPerSecond(String rps){
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
    public static double ParseFramesPerSecond(String fps){
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

    ////
    //  Parse Scaling Factor from Command Line
    ////
    private static float ParseScalingFactor(String scalingFactor){
        System.out.println("Calculating New Image Width and Height");
        float scaling = Float.parseFloat(scalingFactor);
        if (scaling > 0 & scaling < 20){
            System.out.printf("New Scaled Image Width: %d Height: %d%n",Math.round(width/scaling), Math.round(height/scaling));
            return scaling;
        }else{
            System.out.println("Invalid Scaling Factor since the image will be bigger then the original image or way to small to render");
            UsageInformation();
        }
        return 0;
    }

    ////
    //  Parse Anti-Aliasing Flag
    ////
    private static boolean ParseAntiAliasing(String Aliasing){
        System.out.println("Parsing Anti-Aliasing Boolean");
        int aliasing = Integer.parseInt(Aliasing);
        if((aliasing == 0) | (aliasing == 1)){
            if (aliasing == 0){
                System.out.println("Skipping anti-aliasing");
                return false;
            }else{
                System.out.println("Applying anti-aliasing");
                return true;
            }
        }else{
            UsageInformation();
        }
        return false;
    }
}

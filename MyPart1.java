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
//  Part one of the Assignment 1 - Fall 2018 - CSCI 576
////
public class MyPart1 {

    static int width = 512;
    static int height = 512;

    ////
    //  Entry point of the program
    ////
    public static void main(String[] args) {
        System.out.println("Welcome to part one of Assignment 1: CSCI576 || Fall 2018 || Pavan Athreya || pavanatn@usc.edu");
        if (args.length == 3){
            ImageDisplay ren = new ImageDisplay();
            ren.NumberOfLines = ParseNumberOfLines(args[0])/2;
            ren.ScalingFactor = ParseScalingFactor(args[1]);
            ren.AntiAliasing = ValidateAntiAliasing(args[2]);
            ren.labelText1 = "Original image (Left)";
            ren.labelText2 = "Image after modification (Right)";
            ren.ExecutionMode = 1;//Image Mode
            ren.showIms(args);

        }else {
            PrintUsageInformation();
        }
    }

    ////
    //  Prints the usage information
    ////
    public static void PrintUsageInformation(){
        System.out.println("Invalid Usage of the program");
        System.out.println("Usage Information:");
        System.out.println("java MyPart1 \"Number of Lines\" \"Scaling Factor\" \"Anti-Aliasing\"");
        System.out.println("Number of lines and Scaling factor should be positive and Anti-Aliasing should be 0 or 1");
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
            PrintUsageInformation();
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
            PrintUsageInformation();
        }
        return 0;
    }

    ////
    //  Parse Anti-Aliasing Flag
    ////
    private static boolean ValidateAntiAliasing(String Aliasing){
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
            PrintUsageInformation();
        }
        return false;
    }
}

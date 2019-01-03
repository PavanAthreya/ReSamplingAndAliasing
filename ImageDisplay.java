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

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImageDisplay implements ActionListener {
	//Output Display Frame
	JFrame frame;

	//Labels at the top to show left side and the right side of the images
	JLabel lbIm1;
	JLabel lbIm2;

	//Images that are displayed, left side and right side
	BufferedImage img;
	BufferedImage newImg;

	//Image frames for original and processed images
	int width = 512;
	int height = 512;
	int newWidth = 0;
	int newHeight = 0;

	//Parameters for Assignment
	int NumberOfLines;
	double ScalingFactor;
	boolean AntiAliasing;

	//Parameters for the second and the extra credit part of the assignment
	double PixelPerAngle;
	int ExecutionMode; //1 = Image, 2 - Video, 3 - Extra Credit
	double RotationSpeed;
	double FramesPerSecond;
	int curLeft=0;
	int curRight=0;
	int samplePeriod=0;
	double UpdateDegree;
	long curSampleTime=0;
	long PreviousTime =0;

	//Strings to hold label texts based on the program that is executed
	String labelText1;
	String labelText2;

	////
	// 	Draws a black line on the given buffered image from the pixel defined by (x1, y1) to (x2, y2)
	////
	public void drawLine(BufferedImage image, double x1, double y1, double x2, double y2) {
		Graphics2D g = image.createGraphics();
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(1));
		System.out.printf("Line Parameters: x1 = %f, y1 - %f, x2 = %f, y2 = %f\n",x1,y1,x2,y2);
		g.drawLine((int) Math.ceil(x1), (int) Math.ceil(y1), (int) Math.ceil(x2), (int) Math.ceil(y2));
		g.drawImage(image, 0, 0, null);
	}

	////
	//	Main method that is executed when the program wants to display the image
	////
	public void showIms(String[] args) {

		//Calculate new width and height in case Scaling Factor is mentioned
		GetNewImageWidthAndHeight();

		// Initialize a plain white images
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		newImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

		//Set background color for the image
		int ind = 0;
		for (int y = 0; y < height; y++) {

			for (int x = 0; x < width; x++) {

				// byte a = (byte) 255;
				byte r = (byte) 255;
				byte g = (byte) 255;
				byte b = (byte) 255;

				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
				img.setRGB(x, y, pix);
				ind = ind + 1;
			}
		}

		//Draw Borders for the images
		DrawBorders(img);
		DrawBorders(newImg);

		//Lines for the Original Image
		DrawLinesInsideTheOriginalImage();

		//Scaling the new image
		ScaleTheOriginialImageIntoNewImage();

		// Use labels to display the images
		frame = new JFrame();
		GridBagLayout gLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gLayout);

		System.out.println("Finished Processing Image");

		JLabel lbText1 = new JLabel();
		lbText1.setText(this.labelText1);
		lbText1.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel lbText2;
		if (AntiAliasing){
			if (ExecutionMode == 1) {
				lbText2 = new JLabel("Image after modification (Right) with Anti-Aliasing filter");
			}else{
				lbText2 = new JLabel("Video after limiting frames per second");
			}
		}else{
			lbText2 = new JLabel();
			lbText2.setText(labelText2);
		}
		lbText2.setHorizontalAlignment(SwingConstants.CENTER);
		lbIm1 = new JLabel(new ImageIcon(img));
		lbIm2 = new JLabel(new ImageIcon(newImg));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		frame.getContentPane().add(lbText1, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		frame.getContentPane().add(lbText2, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		frame.getContentPane().add(lbIm1, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		frame.getContentPane().add(lbIm2, c);

		System.out.println("Presenting the Image");
		frame.pack();
		frame.setVisible(true);
		//Exit on Close is set so that when the rendering is closed, the program would exit and release all memory that was allocated
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	////
	//	Method to draw borders for the image passed in the argument
	////
	private void DrawBorders(BufferedImage image){
		System.out.println("Drawing borders for both the images");
		drawLine(image, 0, 0, image.getWidth() - 1, 0);                // top edge
		drawLine(image, 0, image.getHeight() - 1, image.getWidth() - 1, image.getHeight() - 1);    // bottom edge
		drawLine(image, 0, 0, 0, image.getHeight() - 1);                // left edge
		drawLine(image, image.getWidth() - 1, image.getHeight() - 1, image.getWidth() - 1, 0);    // right edge
	}

	////
	// Method to draw lines on the original image by calculating the number of lines required and the angle between each line
	////
	private void DrawLinesInsideTheOriginalImage() {
		CalculatePixelPerAngle(width);
		for (int i = 0; i < NumberOfLines; i++) {
			System.out.printf("Drawing Line: %d\n", i);
			double angle = GetAngle(i, NumberOfLines);
			if (angle % 45 == 0) {
				//Standard lines
				DrawStandardLine(Math.round(angle));
			} else {
				//convert angle from degrees to radians
//				double radians = (double) (angle * Math.PI / 180);

//				if (angle > 0 && angle < 45) {
//					drawLine(img, (width / 2) + RoundedSinAngle(radians), 0, (width / 2) - RoundedSinAngle(radians), height - 1);
//				} else if (angle > 45 && angle < 90) {
//					drawLine(img, width, RoundedCosAngle(radians), 0, height - RoundedCosAngle(radians));
//				} else if (angle > 90 && angle < 135) {
//					drawLine(img, width, (height / 2) + RoundedCosAngle(radians), 0, (height / 2) - RoundedCosAngle(radians));
//				} else if (angle > 135 && angle < 180) {
//					drawLine(img, width - RoundedSinAngle(radians), height - 1, RoundedSinAngle(radians), 0);
//				} else {
//					System.out.println("Not Sure where to draw this line");
//				}
				if (angle > 0 && angle < 45) {
					drawLine(img, ((width / 2) + (angle * this.PixelPerAngle + Adjustments(i))), 0, ((width / 2) - (angle * this.PixelPerAngle- Adjustments(i))), height - 1);
				} else if (angle > 45 && angle < 90) {
					angle = angle - 45;
					drawLine(img, width, ((angle * this.PixelPerAngle) + Adjustments(i)), 0, (height - (angle * this.PixelPerAngle) - Adjustments(i)));
				} else if (angle > 90 && angle < 135) {
					angle = angle - 90;
					drawLine(img, width, ((height / 2) + (angle * this.PixelPerAngle) + Adjustments(i)), 0, ((height / 2) - (angle * this.PixelPerAngle) - Adjustments(i)));
				} else if (angle > 135 && angle < 180) {
					angle = angle - 135;
					drawLine(img, (width - (angle * this.PixelPerAngle) - Adjustments(i)), height - 1, ((angle * this.PixelPerAngle) + Adjustments(i)), 0);
				} else {
					System.out.println("Not Sure where to draw this line");
				}
			}
		}
	}

	////
	//	Adjustments for rounding off the pixel per angle values
	///
	private double Adjustments(int i){
		System.out.printf("Adjustment: %f\n",(((i*(double)(width/NumberOfLines)/4)/45))*2);
		return (((double)i*(width/NumberOfLines)/4)/45)*2;
	}

	////
	//	Method to draw the scaled image based on the Scaling Factor mentioned in command line argument
	////
	private void ScaleTheOriginialImageIntoNewImage() {
		BufferedImage someImage = CloneImage(img);
		//Return deep copy of original image in case there is no scaling and no aliasing
		if((AntiAliasing == false) && (ScalingFactor == 1.0)){
			newImg = CloneImage(img);
			return;
		}
		//Pass the image through a low pass filter logic in case anti0aliasing option is chosen
		if (AntiAliasing) {
			System.out.println("Scaling the image with anti-aliasing (Low pass filter)");
			someImage = FilteredImage(img);
		} else {
			System.out.println("Scaling the image without anti-aliasing");
		}

		//Draw background color for the image on the right
		drawBackground(newImg);
		for (int y = 0; y < newHeight; y++) {
			for (int x = 0; x < newWidth; x++) {
				int color = GetColor(someImage, x, y, ScalingFactor);
				newImg.setRGB(x, y, color);
			}
		}
	}

	////
	//	Method to draw standard line spaced in 45 degree angles
	////
	private void DrawStandardLine(double angle) {
		System.out.printf("Drawing Standard line at angle: %f\n", angle);
		switch ((int) angle) {
			case 0: {
				drawLine(img, (width / 2), 0, (width / 2), height - 1);
			}
			break;
			case 45: {
				drawLine(img, (width - 1), 0, 0, height - 1);
			}
			break;
			case 90: {
				drawLine(img, 0, height / 2, width - 1, height / 2);
			}
			break;
			case 135: {
				drawLine(img, width, height, 0, 0);
			}
			break;
			//Redundant
//			case 180:{
//				drawLine(img, width/2, height,(width/2), 0);
//			}break;
//			case 225:{
//				drawLine(img, 0, height,width-1, 0);
//			}break;
//			case 270:{
//				drawLine(img, 0, height/2,width-1, height/2);
//			}break;
//			case 315:{
//				drawLine(img, 0, 0,width-1, height-1);
//			}break;
			default: {
				System.out.println("Switch Case default executed. Something wrong with the angle value passed");
			}
			break;
		}
	}

	////
	//	Gets angle for each iteration while drawing lines on the image
	////
	private double GetAngle(int i, int lines) {
		return (i * (double) (180 / lines));
	}

	////
	//	Computes the number of pixels needed for each angle in the frame
	////
	private void CalculatePixelPerAngle(int width) {
		this.PixelPerAngle = (double) width / 90;
	}

	////
	//	Calculates the scaled width and height of the images needed to be rendered on the right
	////
	private void GetNewImageWidthAndHeight() {
		System.out.println("Calculating New Image Width and Height");
		if (this.ScalingFactor > 0 & this.ScalingFactor < 20) {
			this.newWidth = (int) Math.ceil(this.width / this.ScalingFactor);
			this.newHeight = (int) Math.ceil(this.height / this.ScalingFactor);
			System.out.printf("New Scaled Image Width: %d Height: %d%n", this.newWidth, this.newHeight);
		} else {
			System.out.println("Invalid Scaling Factor since the image will be bigger then the original image or way to small to render");
		}
	}

	////
	//	Deep copy of the passed image to prevent reference retention in shallow copies of the object
	////
	private BufferedImage CloneImage(BufferedImage img) {
		ColorModel model = img.getColorModel();
		boolean alpha = model.isAlphaPremultiplied();
		WritableRaster rasterScan = img.copyData(null);
		return new BufferedImage(model, rasterScan, alpha, null);
	}

	////
	// 	Low pass filter logic to remove aliasing effects
	////
	private BufferedImage FilteredImage(BufferedImage image) {
		System.out.println("Applying low pass filter on the image");
		BufferedImage newImage = CloneImage(image);
		for (int y = 1; y < image.getHeight() - 1; y++)
			for (int x = 1; x < image.getWidth() - 1; x++) {
				int r = 0, g = 0, b = 0;
				int[] XDirection = {-1, 0, 1};
				int[] YDirection = {-1, 0, 1};
				for (int dX : XDirection)
					for (int dY : YDirection) {
						int tC = image.getRGB(dX + x, dY + y);
						r += (tC >> 16) & 0xff;
						g += (tC >> 8) & 0xff;
						b += (tC) & 0xff;
					}
				int color = 0xff000000 | (((r / 9) & 0xff) << 16)
						| (((g / 9) & 0xff) << 8) | ((b / 9) & 0xff);
				newImage.setRGB(x, y, color);
			}
		return newImage;
	}

	////
	//	Utility method to compute the color for the (x,y) position in the image
	////
	private int GetColor(BufferedImage img, int x, int y, double scale) {
		return img.getRGB((int) (x * scale), (int) (y * scale));
	}

//	private double RoundedCosAngle(double radians){
//		return Math.cos(radians);
//	}
//
//	private double RoundedSinAngle(double radians){
//		return Math.sin(radians);
//	}

	//Part - 2 Section of the assignment
	////
	//	Starts the rotation of the images
	////
	public void StartRenderingVideo(ImageDisplay obj){
		System.out.println("Start rotating image");
		int delay = ExecutionMode == 2 ? 0 : 50;
		Timer timer = new Timer(delay,obj);
		timer.start();
	}

	////
	//	Method called at every timer interval fired
	////
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean UpdateSample = false;
		long CurrentTime = System.currentTimeMillis();
		curLeft = (curLeft - (int)(UpdateDegree * (CurrentTime - PreviousTime)) + 360) % 360;
		if(curLeft < 0) curLeft += 360;
		PreviousTime = System.currentTimeMillis();

		//Extra Credit
		if (ExecutionMode == 3){
			if(CurrentTime - curSampleTime > samplePeriod){
				curSampleTime = CurrentTime;
				UpdateSample = true;
			}
		//Part 2
		}else{
			if (CurrentTime - curSampleTime > samplePeriod) {
				curSampleTime = CurrentTime;
				UpdateSample = true;
				curLeft = (curRight - (int) (UpdateDegree * samplePeriod) + 360) % 360;
				curRight = curLeft;
			}
		}
		this.update(img, newImg, NumberOfLines, curLeft, ScalingFactor, AntiAliasing, UpdateSample);
		frame.repaint();
	}

	////
	//	Update the image to make it seem like rotating
	////
	public void update(BufferedImage originalImage, BufferedImage sampleImage, int lines, int cur, double scale, boolean antiAlias, boolean updateSampleImg) {
		drawBackground(originalImage);
		for (int i = 0; i < lines; i++) {
			double degree = (360.00 / lines * i + cur) % 360;
			drawLine(originalImage, originalImage.getWidth() / 2,originalImage.getHeight() / 2, degree);
		}
		if(updateSampleImg == true){
			BufferedImage temp = scaleImage(originalImage, scale, antiAlias);
			for (int y = 0; y < sampleImage.getHeight(); y++)
				for (int x = 0; x < sampleImage.getWidth(); x++) {
					sampleImage.setRGB(x, y, temp.getRGB(x, y));
				}
		}
	}

	////
	//	Draw lines in incremental degrees for the second part of the assignment
	////
	private void drawLine(BufferedImage img, int startX, int startY, double degree) {
		int color = 0xff000000 | ((0 & 0xff) << 16) | ((0 & 0xff) << 8) | (0 & 0xff);

		if (degree < 45 || degree > 315 || degree > 135 && degree < 225) {
			int xDir = (degree >= 0 && degree < 90 || degree > 270 && degree < 360) ? 1 : -1;
			double y = startY;
			for (int x = startX; x >= 0 && x < img.getWidth(); x += xDir) {
				img.setRGB(x, (int) Math.floor(y), color);
				double delta = -xDir * Math.tan(Math.PI / 180 * degree);
				y = y + delta;
				int temp = (int) Math.floor(y);
				if (!(temp < img.getHeight() && temp >= 0))
					break;
			}
		} else {
			int yDir = (degree > 180) ? 1 : -1;
			int xDir = (degree >= 0 && degree < 90 || degree < 270 && degree < 180) ? 1 : -1;
			double x = startX;
			for (int y = startY; y >= 0 && y < img.getHeight(); y += yDir) {
				img.setRGB((int) Math.floor(x), y, color);
				double delta = 1 / Math.tan(Math.PI / 180 * degree) * xDir;
				x = x + delta;
				int temp = (int) Math.floor(x);
				if (!(temp < img.getWidth() && temp >= 0))
					break;
			}
		}
	}

	////
	//	Method to draw background for the image
	////
	public void drawBackground(BufferedImage img) {
		// draw background
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				byte r = (byte) 255;
				byte g = (byte) 255;
				byte b = (byte) 255;
				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8)
						| (b & 0xff);
				img.setRGB(x, y, pix);
			}
		}
	}

	////
	//	Resample and reduce the size of the image
	////
	public BufferedImage scaleImage(BufferedImage originalImage, double scale, boolean antiAlias) {
		if (scale <= 0.0 || (scale == 1 && antiAlias == false))
			return originalImage;
		// scale=2;
		if (antiAlias) {
			originalImage = FilteredImage(originalImage);
		}
		int nWidth = (int) (width / scale), nHeight = (int) (height / scale);
		BufferedImage newImg = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_INT_RGB);
		drawBackground(newImg);
		for (int y = 0; y < nHeight; y++)
			for (int x = 0; x < nWidth; x++) {
				int color = GetColor(originalImage, x, y, scale);
				newImg.setRGB(x, y, color);
			}
		return newImg;
	}

}
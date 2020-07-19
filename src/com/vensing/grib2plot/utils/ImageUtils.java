package com.vensing.grib2plot.utils;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

public class ImageUtils {

	/**
	 * 顺时针旋转90度（通过交换图像的整数像素RGB 值）
	 * 
	 * @param bi
	 * @return
	 */
	public static BufferedImage rotateClockwise90(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		BufferedImage bufferedImage = new BufferedImage(height, width, bi.getType());
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				bufferedImage.setRGB(height - 1 - j, width - 1 - i, bi.getRGB(i, j));
		return bufferedImage;
	}

	/**
	 * 逆时针旋转90度（通过交换图像的整数像素RGB 值）
	 * 
	 * @param bi
	 * @return
	 */
	public static BufferedImage rotateCounterclockwise90(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		BufferedImage bufferedImage = new BufferedImage(height, width, bi.getType());
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				bufferedImage.setRGB(j, i, bi.getRGB(i, j));
		return bufferedImage;
	}

	/**
	 * 旋转180度（通过交换图像的整数像素RGB 值）
	 * 
	 * @param bi
	 * @return
	 */
	public static BufferedImage rotate180(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		BufferedImage bufferedImage = new BufferedImage(width, height, bi.getType());
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				bufferedImage.setRGB(width - i - 1, height - j - 1, bi.getRGB(i, j));
		return bufferedImage;
	}

	/**
	 * 水平翻转,以Y轴为中心翻转
	 * 
	 * @param bi
	 * @return
	 */
	public static BufferedImage rotateHorizon(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		BufferedImage bufferedImage = new BufferedImage(width, height, bi.getType());
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				bufferedImage.setRGB(width - i - 1, j, bi.getRGB(i, j));
		return bufferedImage;
	}

	/**
	 * 垂直翻转，以X轴为中心翻转
	 * 
	 * @param bi
	 * @return
	 */
	public static BufferedImage rotateVertical(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		BufferedImage bufferedImage = new BufferedImage(width, height, bi.getType());
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				bufferedImage.setRGB(i, height - 1 - j, bi.getRGB(i, j));
		return bufferedImage;
	}

	/**
	 * 返回指定长宽的透明背景的BufferedImage对象
	 * 
	 * @param width
	 * @param height
	 * @return BufferedImage
	 */
	public static BufferedImage TransparencyToZero(int width, int height) {

		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D gd = bi.createGraphics();
		bi = gd.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		gd = bi.createGraphics();

		return bi;
	}

}
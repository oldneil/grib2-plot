package com.vensing.grib2plot.utils;

import com.alibaba.fastjson.JSONArray;



public class GradientUtils {

	/**
	 * 计算线性渐变色
	 *
	 * @param value      value
	 * @param colorArr   颜色数组
	 * @param breakArray 颜色分级
	 * @return int[]
	 */
	public static int[] getlinearColor(double value, JSONArray colorArr, double[] breakArray) {
		for (int i = 0; i < breakArray.length; i++) {
			double breakValue = breakArray[i];
			JSONArray rgba = colorArr.getJSONArray(i);
			if (value <= breakValue) {
				if (i == 0) {
					return arrayToPixel(rgba);
				} else {
					double lastBreak = breakArray[i - 1];
					JSONArray lastrgba = colorArr.getJSONArray(i - 1);
					int[] pixel = new int[4];
					// 计算RGB的线性值
					pixel[0] = (int) linearInterpolation(value, lastBreak, breakValue, lastrgba.getFloat(0),
							rgba.getFloat(0));
					pixel[1] = (int) linearInterpolation(value, lastBreak, breakValue, lastrgba.getFloat(1),
							rgba.getFloat(1));
					pixel[2] = (int) linearInterpolation(value, lastBreak, breakValue, lastrgba.getFloat(2),
							rgba.getFloat(2));
					pixel[3] = (int) linearInterpolation(value, lastBreak, breakValue, lastrgba.getFloat(3),
							rgba.getFloat(3));
					return pixel;
				}
			}
		}
		return arrayToPixel(colorArr.getJSONArray(colorArr.size() - 1));
	}

	/**
	 * 线性插值
	 *
	 * @param x       插值点坐标
	 * @param x1      顶点坐标
	 * @param x2      顶点坐标
	 * @param x1value 顶点数值
	 * @param x2value 顶点数值
	 * @return 插值后的数值
	 */
	private static float linearInterpolation(double x, double x1, double x2, float x1value, float x2value) {
		return (float) (((x2 - x) / (x2 - x1) * x1value) + ((x - x1) / (x2 - x1) * x2value));
	}

	/**
	 * array转int[] 获取像素点颜色
	 *
	 * @param rgba rgbaArray
	 * @return int[]
	 */
	private static int[] arrayToPixel(JSONArray rgba) {
		int[] pixel = new int[4];
		pixel[0] = rgba.getInteger(0);
		pixel[1] = rgba.getInteger(1);
		pixel[2] = rgba.getInteger(2);
		pixel[3] = rgba.getInteger(3);
		return pixel;
	}
}

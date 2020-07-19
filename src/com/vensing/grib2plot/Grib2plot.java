package com.vensing.grib2plot;

import com.alibaba.fastjson.JSONArray;
import com.vensing.grib2plot.utils.GradientUtils;
import com.vensing.grib2plot.utils.ImageUtils;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Grib2plot {

    public static void main(String[] args) throws IOException {
        String featureName = "Temperature_height_above_ground";
        //使用json格式表示colorTable， from panoply default color table
        String colorTable = "[[5,7,206,1],[41,101,255,1],[95,180,255,1],[136,219,255,1],[170,243,255,1],"
                + "[225,253,184,1],[255,233,0,1],[255,163,0,1],[255,55,0,1],[219,0,0,1],[132,0,0,1]]";

        ImageOutputStream ios =null;
        try {
            NetcdfFile nc = NetcdfFile.open("data/gfs.t00z.grb2");
            float[][] values = readVariableDatas(nc, featureName);
            double[] dataInterval = getDataInterVal(values);
            //crate image with lon*lat size
            BufferedImage bi = ImageUtils.TransparencyToZero(values[0].length, values.length);
            setRgbValue(bi, values, colorTable, dataInterval);
            Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("PNG");
            ImageWriter writer = it.next();
            File f = new File("images/" + featureName + ".PNG");
            ios = ImageIO.createImageOutputStream(f);
            writer.setOutput(ios);
            writer.write(bi);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(ios!=null)ios.close();
        }
    }

    /**
     * Geo2D变量，最少三维，最多四维。时间、[空间、] 纬度、经度
     * @param nc
     * @param featureName
     * @return
     * @throws IOException
     */
    public static float[][] readVariableDatas(NetcdfFile nc, String featureName) throws IOException {

        float[][] datas = null;
        int[] origin;
        int[] shape;
        try {
            Variable variable = nc.findVariable(featureName);
            List<Dimension> dimensions = variable.getDimensions();

            int[] shapes = variable.getShape();
            if(shapes.length == 3){
                //3 dimensions data (eg: time=1, lat=361, lon=720)
                origin = new int[]{0, 0, 0};
                shape = new int[]{1, shapes[1], shapes[2]};
                float[][][] data = (float[][][]) variable.read(origin, shape).copyToNDJavaArray();
                datas = data[0];
            }else if(shapes.length == 4){
                //4 dimensions data (eg: time=1, level=?, lat=361, lon=720)
                origin = new int[]{0, 0, 0, 0};
                //always get data on level=0
                shape = new int[]{1, 1,shapes[2], shapes[3]};
                float[][][][] data = (float[][][][]) variable.read(origin, shape).copyToNDJavaArray();
                datas = data[0][0];
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(nc!=null)nc.close();
        }
        return datas;
    }

    /**
     * 设置图片像素的rgba颜色值
     * @param bi
     * @param dataMatrix
     * @param colorTable
     * @param dataInterval
     */
    public static void setRgbValue(BufferedImage bi, float[][] dataMatrix, String colorTable, double[] dataInterval) {
        JSONArray colorArr = JSONArray.parseArray(colorTable);
        for (int i = 0; i < dataMatrix.length; i++) {
            for (int j = 0; j < dataMatrix[i].length; j++) {
                float data = dataMatrix[i][j];
                int[] rgb = GradientUtils.getlinearColor(data, colorArr, dataInterval);
                int color = ((0xFF << 24) | (rgb[0] << 16) | (rgb[1] << 8) | rgb[2]);
                bi.setRGB(j, i, color);
            }
        }
    }


    /**
     * 获取colorTable 间隔
     * @param values
     * @return
     */
    public static double[] getDataInterVal(float[][] values){
        double[] dataInterval = new double[11];
        float max = values[0][0];
        float min = values[0][0];
        for (float[] value : values) {
            for (float v : value) {
                if (max < v) {
                    max = v;//算出最大值
                }
                if (min > v) {
                    min = v;//算出最小值
                }
            }
        }
        float step = (max -min)/10;
        for(int i=0;i<dataInterval.length;i++){
            dataInterval[i] = min + step*i;
        }
        return dataInterval;
    }
}

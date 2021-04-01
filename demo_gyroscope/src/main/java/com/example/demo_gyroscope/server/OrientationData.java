package com.example.demo_gyroscope.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrientationData {
	
	private static final Logger LOG = LoggerFactory.getLogger(ReactorServer.class);

    private int x;
    private int y;
    private int z;

    public OrientationData() {
    }

    public OrientationData(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Orientation {" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
    
    public static OrientationData average(List<OrientationData> data) {
    	final OrientationData result = new OrientationData();
    	data.stream().forEach(od -> {result.x+=od.x; result.y+=od.y; result.z+=od.z;});
    	result.x = result.x / data.size();
    	result.y = result.y / data.size();
    	result.z = result.z / data.size();
    	LOG.info("calculating average: {}", result.toString());
    	return result;
    }
}
package com.example.remote_ros;

import android.util.Log;
import ros.Publisher;
import ros.RosBridge;
import ros.msgs.geometry_msgs.Twist;
import ros.msgs.geometry_msgs.Vector3;


public class TwistPublisher {
    private static TwistPublisher instance = new TwistPublisher();
    private RosBridge ros;
    private Publisher twistPub;
    
    private double vx;
    private double vy;
    private double angular;
    
    private TwistPublisher() {
        ros = new RosBridge();
    }
    
    public static TwistPublisher getInstance() {
        return instance;
    }
    
    public boolean init(String ip) {
        ros = new RosBridge();
        Log.d("remoteros", "initialization of rosbridge");
        ros.connect(ip, true);
        if (ros.hasConnected()) {
            Log.d("remoteros", "success");
            twistPub = new Publisher("/order", "geometry_msgs/Twist", ros);
            return true;
        } else {
            Log.d("remoteros", "failure");
            return false;
        }
    }
    
    public void update() {
        Twist twist = new Twist(new Vector3(vx, vy, 0), new Vector3(0, 0, angular));
        if (ros.hasConnected()) {
            twistPub.publish(twist);
        }
        Log.d("remoteros", "x:" + twist.linear.x +
                "y:" + twist.linear.y +
                "az:" + twist.angular.z);
    }
    
    public double getVx() {
        return vx;
    }
    
    public void setVx(double vx) {
        this.vx = vx;
    }
    
    public double getVy() {
        return vy;
    }
    
    public void setVy(double vy) {
        this.vy = vy;
    }
    
    public double getAngular() {
        return angular;
    }
    
    public void setAngular(double angular) {
        this.angular = angular;
    }
}
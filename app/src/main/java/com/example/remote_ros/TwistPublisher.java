package com.example.remote_ros;

import android.os.AsyncTask;
import android.util.Log;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import ros.msgs.geometry_msgs.Twist;
import ros.msgs.geometry_msgs.Vector3;

import java.io.IOException;
import java.io.StringWriter;


public class TwistPublisher {
    private static TwistPublisher instance = new TwistPublisher();
    private Ros ros;
    private Topic twistPub;
    
    private double vx;
    private double vy;
    private double angular;
    
    private TwistPublisher() {
        ros = new Ros();
    }
    
    public static TwistPublisher getInstance() {
        return instance;
    }
    
    public boolean init(String ip) {
        ros = new Ros(ip);
        Log.d("remoteros", "initialization of rosbridge");
    
        if (ros.connect()) {
            Log.d("remoteros", "success");
            twistPub = new Topic(ros, "/order", "geometry_msgs/Twist");
            return true;
        } else {
            Log.d("remoteros", "failure");
            return false;
        }
    }
    
    public void update() {
        Twist twist = new Twist(new Vector3(vx, vy, 0), new Vector3(0, 0, angular));
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter writer = new StringWriter();
        JsonGenerator generator;
        try {
            generator = jsonFactory.createGenerator(writer);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(generator, twist);
            if (ros.isConnected()) {
                Message message = new Message(writer.toString(), "geometry_msgs/Twist");
                AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        twistPub.publish(message);
                        return null;
                    }
                };
                asyncTask.execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
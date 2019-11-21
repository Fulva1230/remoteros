package com.example.remote_ros;

import android.util.Log;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import ros.msgs.geometry_msgs.Twist;
import ros.msgs.geometry_msgs.Vector3;

import java.io.IOException;
import java.io.StringWriter;


public class TwistPublisher {
    private static TwistPublisher instance = new TwistPublisher();
    private Topic twistPub;
    private RosComutor rosComutor;
    
    private double vx;
    private double vy;
    private double angular;
    
    private TwistPublisher() {
    }
    
    public static TwistPublisher getInstance() {
        return instance;
    }
    
    public void init() {
        RosHandlerFactory.getInstance().getRosComutor().run((ros) -> {
            twistPub = new Topic(ros, "/order", "geometry_msgs/Twist");
        });
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
            Message message = new Message(writer.toString(), "geometry_msgs/Twist");
            rosComutor.run((ros) -> {
                twistPub.publish(message);
            });
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
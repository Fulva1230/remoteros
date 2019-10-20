package com.example.remote_ros;

import android.util.Log;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import lombok.Getter;
import lombok.Setter;

public class TwistPublisher {
    private static TwistPublisher instance = new TwistPublisher();
    private Ros ros;
    private Topic twistTopic;
    @Getter
    @Setter
    private double vx;
    @Getter
    @Setter
    private double vy;
    @Getter
    @Setter
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
            twistTopic = new Topic(ros, "/order", "geometry_msgs/Twist");
            return true;
        } else {
            Log.d("remoteros", "failure");
            return false;
        }
    }
    
    public void update() {
        Message msg = new Message("");
        msg.setMessageType("geometry_msgs/Twist");
        twistTopic.publish(msg);
    }
}

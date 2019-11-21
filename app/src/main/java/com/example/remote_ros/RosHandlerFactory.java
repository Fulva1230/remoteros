package com.example.remote_ros;

import android.os.AsyncTask;
import android.util.Log;
import edu.wpi.rail.jrosbridge.Ros;

import java.util.function.Consumer;

public class RosHandlerFactory {
    static private RosHandlerFactory instance = new RosHandlerFactory();
    private Ros ros;
    
    static public RosHandlerFactory getInstance() {
        return instance;
    }
    
    public RosComutor getRosComutor() {
        return new ConRosComutor(ros);
    }
    
    public boolean init(String ip) {
        ros = new Ros(ip);
        Log.d("remoteros", "initialization of rosbridge");
        if (ros.connect()) {
            Log.d("remoteros", "success");
            return true;
        } else {
            Log.d("remoteros", "failure");
            return false;
        }
    }
    
    public static class ConRosComutor implements RosComutor {
        private final Ros ros;
        
        private ConRosComutor(Ros ros) {
            this.ros = ros;
        }
        
        @Override
        public void run(Consumer<Ros> consumer) {
            RosCommuteTask asyncTask = new RosCommuteTask(ros);
            asyncTask.execute(consumer);
        }
    }
    
    private static class RosCommuteTask extends AsyncTask<Consumer<Ros>, Void, Void> {
        private final Ros ros;
        
        private RosCommuteTask(Ros ros) {
            this.ros = ros;
        }
        
        @Override
        protected Void doInBackground(Consumer<Ros>... consumers) {
            for (Consumer<Ros> consumer : consumers) {
                consumer.accept(ros);
            }
            return null;
        }
    }
}

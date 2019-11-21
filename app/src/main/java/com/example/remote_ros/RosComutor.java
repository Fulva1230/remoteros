package com.example.remote_ros;

import edu.wpi.rail.jrosbridge.Ros;

import java.util.function.Consumer;

public interface RosComutor {
    void run(Consumer<Ros> consumer);
}

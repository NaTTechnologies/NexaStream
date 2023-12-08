package com.nat.nexastream.example.news;

import com.nat.nexastream.annotations.distribution.DistributableTask;
import com.nat.nexastream.annotations.distribution.Node;

import java.util.Map;

@Node(name = "example")
public interface ExampleNodeScript {
    @DistributableTask(name = "hello-world",
            enviroment = DistributableTask.Enviroment.JCTL)
    public Object helloWorld(Map<String, Object> returnValues);
}

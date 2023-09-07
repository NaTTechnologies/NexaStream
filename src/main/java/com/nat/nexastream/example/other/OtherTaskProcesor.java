package com.nat.nexastream.example.other;

import com.nat.nexastream.annotations.distribution.DistributableTask;
import com.nat.nexastream.annotations.distribution.Node;

@Node(name = "otherNode")
public class OtherTaskProcesor {
    @DistributableTask(priority = 150, name = "data", dependencies = {"dataFetcher"})
    public void data() {
        // Logica para obtener datos necesarios
        System.out.println("data");
    }
}

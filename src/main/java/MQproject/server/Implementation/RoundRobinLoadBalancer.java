package MQproject.server.Implementation;


import MQproject.server.model.Broker;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
@Service
public class RoundRobinLoadBalancer {
    private List<Broker> brokers;
    private int currentIndex;

    public RoundRobinLoadBalancer(List<Broker> brokers) {
        if (brokers == null || brokers.isEmpty()) {
            throw new IllegalArgumentException("Broker list cannot be null or empty");
        }
        this.brokers = new ArrayList<>();
        this.currentIndex = 0;
    }

    public Broker getNextBroker() {
        Broker nextBroker = brokers.get(currentIndex);
        currentIndex = (currentIndex + 1) % brokers.size();
        return nextBroker;
    }

    // public static void main(String[] args) {
    //     List<String> brokerList = List.of("Broker1", "Broker2", "Broker3");
        
    //     RoundRobinLoadBalancer loadBalancer = new RoundRobinLoadBalancer(brokerList);

    //     // Simulate incoming requests
    //     for (int i = 0; i < 10; i++) {
    //         String nextBroker = loadBalancer.getNextBroker();
    //         System.out.println("Request " + (i + 1) + " routed to: " + nextBroker);
    //     }
    // }
}

package MQproject.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import MQproject.server.Implementation.BrokerLoadBalancerImpl;
import MQproject.server.Implementation.ConsumerLoadBalancerImpl;
import MQproject.server.Implementation.ServerImplementation;


@SpringBootTest
class ServerApplicationTests {

	private ServerImplementation serverImpl;
	private BrokerLoadBalancerImpl brokerLoadBalancerImpl;
	private ConsumerLoadBalancerImpl consumerLoadBalancerImpl;

	public HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitionsTest = new HashMap<>();
	public HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions = new HashMap<>();
	public HashMap<Integer, ArrayList<Integer>> consumerIdToPartitionsTest = new HashMap<>();



	@BeforeEach
    void setUp() {
        serverImpl = new ServerImplementation();

    }

	@Test
	void testLoadBalancerOnConsumerBirth() {
		int brokerId1 = 1;
		
		ArrayList<Integer> leaderPartitionsTest = new ArrayList<>(Arrays.asList(1, 2));
		ArrayList<Integer> replicaPartitionsTest = new ArrayList<>(Arrays.asList(3, 4));
		brokerIdToLeaderPartitionsTest.put(brokerId1, leaderPartitionsTest);
		brokerIdToReplicaPartitions.put(brokerId1, replicaPartitionsTest);
	
		// Invoke the method under test and assert the result
		Assertions.assertThrows(NullPointerException.class, () -> {
			consumerLoadBalancerImpl.balanceOnConsumerBirth(consumerIdToPartitionsTest, replicaPartitionsTest, null);
		});
	}


	@Test
	void testGenerateToken() {
		Assertions.assertDoesNotThrow(() -> serverImpl.generateToken());
	}

	@Test
	void testLoadBalancerOnBrokerBirth() {
		int brokerId1 = 1;
		
		ArrayList<Integer> leaderPartitionsTest = new ArrayList<>(Arrays.asList(1, 2));
		ArrayList<Integer> replicaPartitionsTest = new ArrayList<>(Arrays.asList(3, 4));
		brokerIdToLeaderPartitionsTest.put(brokerId1, leaderPartitionsTest);
		brokerIdToReplicaPartitions.put(brokerId1, replicaPartitionsTest);
	
		// Invoke the method under test and assert the result
		Assertions.assertThrows(NullPointerException.class, () -> {
			brokerLoadBalancerImpl.balanceOnBrokerBirth(brokerIdToLeaderPartitionsTest, brokerIdToReplicaPartitions, null);
		});
	}

	@Test
	void testLoadBalancerOnBrokerDeath() {
		int brokerId1 = 1;
		
		ArrayList<Integer> leaderPartitionsTest = new ArrayList<>(Arrays.asList(1, 2));
		ArrayList<Integer> replicaPartitionsTest = new ArrayList<>(Arrays.asList(3, 4));
		brokerIdToLeaderPartitionsTest.put(brokerId1, leaderPartitionsTest);
		brokerIdToReplicaPartitions.put(brokerId1, replicaPartitionsTest);
	
		// Invoke the method under test and assert the result
		Assertions.assertThrows(NullPointerException.class, () -> {
			brokerLoadBalancerImpl.balanceOnBrokerDeath(brokerIdToLeaderPartitionsTest, brokerIdToReplicaPartitions, brokerId1);
		});
	}

}

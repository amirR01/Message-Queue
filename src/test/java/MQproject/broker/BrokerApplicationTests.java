package MQproject.broker;

import MQproject.broker.Implementation.BrokerServerServiceImpl;
import MQproject.broker.Interface.BrokerServerService;
import MQproject.broker.model.message.BrokerServerMessageAboutBrokers;
import MQproject.broker.model.message.MessageType;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;


@SpringBootTest
class BrokerApplicationTests {

	private BrokerServerServiceImpl brokerServerServiceImpl ;

	public HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitionsTest = new HashMap<>();
	public HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions = new HashMap<>();
	public HashMap<Integer, ArrayList<Integer>> consumerIdToPartitionsTest = new HashMap<>();



	@BeforeEach
    void setUp() {
        brokerServerServiceImpl = new BrokerServerServiceImpl();
    }

	@Test
	void testLoadBalancerOnConsumerBirth() {
		int brokerId1 = 1;
		
		// ArrayList<Integer> leaderPartitionsTest = new ArrayList<>(Arrays.asList(1, 2));
		// ArrayList<Integer> replicaPartitionsTest = new ArrayList<>(Arrays.asList(3, 4));
		// brokerIdToLeaderPartitionsTest.put(brokerId1, leaderPartitionsTest);
		// brokerIdToReplicaPartitions.put(brokerId1, replicaPartitionsTest);
	
		// // Invoke the method under test and assert the result
		// Assertions.assertThrows(NullPointerException.class, () -> {
		// 	consumerLoadBalancerImpl.balanceOnConsumerBirth(consumerIdToPartitionsTest, replicaPartitionsTest, null);
		// });
		Assertions.assertDoesNotThrow(() -> brokerServerServiceImpl.getBrokerAddress(brokerId1));

	}


	@Test
	void testGetBrokerId() {
		Assertions.assertDoesNotThrow(() -> brokerServerServiceImpl.getMyBrokerId());
	}

	@Test
	void testRegisterToServer() {
		int brokerId1 = 1;
		
		// ArrayList<Integer> leaderPartitionsTest = new ArrayList<>(Arrays.asList(1, 2));
		// ArrayList<Integer> replicaPartitionsTest = new ArrayList<>(Arrays.asList(3, 4));
		// brokerIdToLeaderPartitionsTest.put(brokerId1, leaderPartitionsTest);
		// brokerIdToReplicaPartitions.put(brokerId1, replicaPartitionsTest);
	
		// // Invoke the method under test and assert the result
		// Assertions.assertThrows(NullPointerException.class, () -> {
		// 	brokerLoadBalancerImpl.balanceOnBrokerBirth(brokerIdToLeaderPartitionsTest, brokerIdToReplicaPartitions, null);
		// });

		// myBrokerId

		// BrokerServerMessageAboutBrokers bigMessage = new BrokerServerMessageAboutBrokers();
        // bigMessage.messages.add(
        //         new BrokerServerMessageAboutBrokers.BrokerServerSmallerMessageAboutBrokers(
        //                 myBrokerId, myIp, myPort, MessageType.REGISTER_BROKER
        //         )
        // );

		// Assertions.assertDoesNotThrow(() -> brokerServerServiceImpl.registerToServer());

	}

	// @Test
	// void testLoadBalancerOnBrokerDeath() {
	// 	int brokerId1 = 1;
		
	// 	ArrayList<Integer> leaderPartitionsTest = new ArrayList<>(Arrays.asList(1, 2));
	// 	ArrayList<Integer> replicaPartitionsTest = new ArrayList<>(Arrays.asList(3, 4));
	// 	brokerIdToLeaderPartitionsTest.put(brokerId1, leaderPartitionsTest);
	// 	brokerIdToReplicaPartitions.put(brokerId1, replicaPartitionsTest);
	
	// 	// Invoke the method under test and assert the result
	// 	Assertions.assertThrows(NullPointerException.class, () -> {
	// 		brokerLoadBalancerImpl.balanceOnBrokerDeath(brokerIdToLeaderPartitionsTest, brokerIdToReplicaPartitions, brokerId1);
	// 	});
	// }

}



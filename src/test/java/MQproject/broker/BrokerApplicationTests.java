package MQproject.broker;

import MQproject.broker.Interface.BrokerServerService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
class BrokerApplicationTests {
	@Autowired
	BrokerServerService brokerServerService;
	private final RestTemplate restTemplate = new RestTemplate();


	@Test
	void contextLoads() {

	}

	@Test
	void testProduceMessage(){
//		Message = sadsadsd;
//		String awnser = brokerServerService.produceMessage();
//		ResponseEntity restTemplate.postForEntity(
//
//		)
//		Assert.assertEquals();
	}
	//     broker id , partition ids
	HashMap<Integer, List<Integer>> partitionsLeaders = new HashMap<>();
	HashMap<Integer, List<Integer>> partitionsReplicas = new HashMap<>();



}

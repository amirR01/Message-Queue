package MQproject.server.Implementation;


// TODO: broker is lazier than server -> server should notify effected brokers

import MQproject.server.Interface.BrokerLoadBalancer;

import java.util.ArrayList;
import java.util.HashMap;

public class BrokerLoadBalancerImpl implements BrokerLoadBalancer {
    public void balanceOnBrokerDeath(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions, HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions, Integer deadBrokerId) {

    }

    public void balanceOnBrokerBirth(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions, HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions, Integer bornBrokerId) {

    }

    public void balanceOnPartitionDeath(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions, HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartitions, Integer bornPartitionId) {

    }

    public void balanceOnPartitionBirth(HashMap<Integer, ArrayList<Integer>> brokerIdToLeaderPartitions, HashMap<Integer, ArrayList<Integer>> brokerIdToReplicaPartition, Integer deadPartitionId) {

    }
}

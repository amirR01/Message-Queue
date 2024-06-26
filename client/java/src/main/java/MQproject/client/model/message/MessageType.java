package MQproject.client.model.message;

public enum MessageType {
    REGISTER_PRODUCER,
    REGISTER_CONSUMER,
    PRODUCE_MESSAGE,
    GET_PARTITION,
    CONSUME_MESSAGE,
    ASSIGN_BROKER,
    PULL_BROKER,
    REMOVE_BROKER,
    ADD_REPLICA,
    REMOVE_REPLICA,
    ADD_PARTITION,
    REMOVE_PARTITION,
    ADD_CLIENT,
    REMOVE_CLIENT,
    ADD_MESSAGE,
    REMOVE_MESSAGE,
    ADD_SUBSCRIPTION,
    REMOVE_SUBSCRIPTION,
    ADD_CONSUMER,
    REMOVE_CONSUMER,
    ADD_PRODUCER,
    REMOVE_PRODUCER,
    ADD_METRICS,
    REMOVE_METRICS,
    GET_METRICS,
    RESET_METRICS,
    RUN_MONITORING,
    STOP_MONITORING,
    REGISTER_BROKER,
}

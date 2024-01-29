package MQproject.server.Implementation;

import java.util.UUID;

public class KeyGenerator {
    
    public static String generateKey() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static void main(String[] args) {
        System.out.println("some generated key");
        System.out.println(generateKey());
    }
}

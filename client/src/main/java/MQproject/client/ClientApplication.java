package MQproject.client;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;

@SpringBootApplication
@EnableFeignClients
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);

	}
	@Bean
	public PromptProvider myPromptProvider() {
		return () -> new AttributedString("shell:>",
				AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
	}

}

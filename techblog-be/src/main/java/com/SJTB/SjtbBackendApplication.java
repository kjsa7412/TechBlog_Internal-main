package com.SJTB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync//비동기 실행 활성화(해당 함수에서 Async 어노테이션 사용필요)
@EnableScheduling//스케줄링 활성화(Scheduled 어노테이션 사용)
@EnableJpaAuditing
@SpringBootApplication
public class SjtbBackendApplication {
	public static void main(String[] args) {
		// OpenAI API 통신을 위한 TLS 버전 추가
		System.setProperty("https.protocols", "TLSv1.2");
		String httpsProtocols = System.getProperty("https.protocols");
		System.out.println("########## https.protocols: " + httpsProtocols);

		SpringApplication.run(SjtbBackendApplication.class, args);
	}
}

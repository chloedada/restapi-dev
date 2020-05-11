package com.chloe.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.chloe.payment.model.CardInfoRepository;
import com.chloe.payment.model.PaymentInfo;

@SpringBootApplication
public class PaymentMainApplication {

	@Autowired
	private CardInfoRepository cardInfoRepository;

	@Bean
	public ApplicationRunner applicationRunner() {
		return new ApplicationRunner() {

			@Override
			public void run(ApplicationArguments args) throws Exception {
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(PaymentMainApplication.class, args);
	}

}

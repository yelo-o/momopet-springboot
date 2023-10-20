package com.momo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class MomopetSpringbootApplication {

	public static void main(String[] args) {
//		SpringApplication.run(MomopetSpringbootApplication.class, args);
		//설정한 값을 pid command 사용
		SpringApplication application = new SpringApplication(MomopetSpringbootApplication.class);
		application.addListeners(new ApplicationPidFileWriter());
		application.run(args);
	}

}

package net.adam85w.ddd.boundedcontextcanvas.management;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Bounded Context Canvas Management Application
 *
 * @author Adam Woźniak <adam85.w@gmail.com>
 */
@SpringBootApplication
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT0S")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

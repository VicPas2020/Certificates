package com.example.demo;

import com.codahale.metrics.*;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

@SpringBootApplication

public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		MetricRegistry metricRegistry = new MetricRegistry();
		Gauge<Long> gauge = new AttendanceRatioGauge();
		gauge.getValue();
		metricRegistry.register("Certificate expiration days left", gauge);

		ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry).build();
		reporter.start(1, TimeUnit.DAYS);
		reporter.report();
	}

	@Bean
	JvmThreadMetrics threadMetrics(){
		return new JvmThreadMetrics();
	}
}

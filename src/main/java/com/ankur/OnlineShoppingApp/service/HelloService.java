package com.ankur.OnlineShoppingApp.service;
import com.ankur.OnlineShoppingApp.exception.ServiceNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    @CircuitBreaker(name = "helloService", fallbackMethod = "fallbackMethod")
    public String callUnreliableService() {
        System.out.println("Calling unreliable service...");
        throw new ServiceNotFoundException("💥 Simulated failure from FailureService");
    }

    public String fallbackMethod(Throwable t) {
        return " Fallback response: " + t.getMessage();
    }

}


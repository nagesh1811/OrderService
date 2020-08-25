package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {

	@Autowired
	private LoadBalancerClient loadBalancerClient;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/callOrderService")
	public ResponseEntity<String> callOrderService() throws Exception {
		String response = "Order Received";
		return new ResponseEntity<String>(response,HttpStatus.OK);
		
	}
	
	@GetMapping("/callPaymentServiceThroughOrderService")
	public ResponseEntity<String> callPaymentService() throws Exception {
		try {
		return new ResponseEntity<String>(restTemplate.getForObject(getBaseUrl()+"/callClienttwo", String.class),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(restTemplate.getForObject(getBaseUrl()+"/callClienttwo", String.class),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	private String getBaseUrl() {
		ServiceInstance instance = loadBalancerClient.choose("CLIENT2"); 
		return instance.getUri().toString();
	}
	
	
	
}

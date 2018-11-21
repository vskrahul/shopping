/**
 * 
 */
package com.github.vskrahul.account.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * @author Rahul Vishvakarma
 *
 */
@RestController
@RequestMapping(path="account")
public class AccountController {
	
	@Autowired
    private DiscoveryClient discoveryClient;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private AtomicBoolean eurekaServiceIsDown = new AtomicBoolean(true);
	
	@GetMapping(path="profile")
	@HystrixCommand(fallbackMethod = "acountIsDown", commandKey = "userProfile"
						,groupKey="accountController")
	public String userProfile() {
		return "You are a guest user.";
	}
	
	@HystrixCommand(fallbackMethod = "serviceInstancesByApplicationNameIdDown", commandKey = "serviceInstancesByApplicationName", groupKey="accountController")
	@RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(
            @PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }
	
	@HystrixCommand(fallbackMethod = "eurekaServicesAreDown", commandKey = "services", groupKey="accountController")
	@RequestMapping("/services")
    public List<String> services() {
		logger.info("call to services...");
		if(eurekaServiceIsDown.get()) {
			logger.error("Eureka service is down.");
			throw new RuntimeException("Eureka Service is down.");
		}
        return this.discoveryClient.getServices();
    }
	
	@RequestMapping(path="/eurekadown", method={RequestMethod.PUT})
    public void downEureka() {
		eurekaServiceIsDown.set(true);
    }
	
	@RequestMapping(path="/eurekaup", method={RequestMethod.PUT})
    public void upEureka() {
		eurekaServiceIsDown.set(false);
    }
	
	@HystrixCommand(fallbackMethod = "acountIsDown", commandKey = "serviceMetaDataByApplicationName", groupKey="accountController")
	@RequestMapping("/service-urls/{applicationName}")
    public String serviceMetaDataByApplicationName(
            @PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName)
        			.stream()
        			.map(s -> String.format("["
        					+ "host=%s,"
        					+ "meta-data=%s,"
        					+ "port=%s,"
        					+ "scheme=%s,"
        					+ "service id=%s,"
        					+ "uri=%s"
        					+ "]", s.getHost(), s.getMetadata(), s.getPort(), s.getScheme(), s.getServiceId(), s.getUri()))
        			.collect(Collectors.joining(", "));
    }
	
	@HystrixCommand(fallbackMethod = "acountIsDown", commandKey = "working", groupKey="accountController")
	@GetMapping("/working")
	public String working() {
		if(RandomUtils.nextBoolean()) {
			throw new RuntimeException("Something is went wrong with account service.");
		}
		return "Account service is working...!";
	}
	
	@HystrixCommand(fallbackMethod = "acountIsDown", commandKey = "ping", groupKey="accountController")
	@GetMapping("/ping")
	public String ping() {
		if(RandomUtils.nextBoolean()) {
			throw new RuntimeException("Something is went wrong with account service.");
		}
		return "Account service is working...!";
	}
	
	public List<String> eurekaServicesAreDown() {
		List<String> errors = new ArrayList<>();
		logger.warn("Fallback is triggered.");
		errors.add("Fallback is triggered!!!");
		return errors;
	}
	
	public List<ServiceInstance> serviceInstancesByApplicationNameIdDown(String appName) {
		List<ServiceInstance> list = new ArrayList<>();
		list.add(null);
		return list;
	}
	
	public String acountIsDown() {
		return "Fallback is triggered!!!";
	}
	
	public String acountIsDown(String applicationName) {
		return "Fallback is triggered!!!" + applicationName;
	}
}
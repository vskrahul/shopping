/**
 * 
 */
package com.github.vskrahul.cart.controller;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * @author Rahul Vishvakarma
 *
 */
@RestController
@RequestMapping(path="cart")
public class CartController {
	
	@HystrixCommand(fallbackMethod="cartIsDown", commandKey="show", groupKey="cart")
	@GetMapping(path="show")
	public String show() {
		return "Your cart is empty.";
	}
	
	@HystrixCommand(fallbackMethod="cartIsDown", commandKey="working", groupKey="cart")
	@GetMapping("/working")
	public String working() {
		if(RandomUtils.nextBoolean()) {
			throw new RuntimeException("Something is went wrong with cart service.");
		}
		return "Cart service is working...!";
	}
	
	@HystrixCommand(fallbackMethod="cartIsDown", commandKey="ping", groupKey="cart")
	@GetMapping("/ping")
	public String ping() {
		if(RandomUtils.nextBoolean()) {
			throw new RuntimeException("Something is went wrong with cart service.");
		}
		return "Cart service is working...!";
	}
	
	public String cartIsDown() {
		return "Fallback is triggered!!!";
	}
	
}
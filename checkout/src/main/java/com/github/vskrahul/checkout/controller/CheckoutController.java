/**
 * 
 */
package com.github.vskrahul.checkout.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rahul Vishvakarma
 *
 */
@RestController
@RequestMapping(path="checkout")
public class CheckoutController {
	
	@GetMapping(path="payment")
	public String payment() {
		return "Please confirm payment details.";
	}
	
}
/**
 * 
 */
package com.github.vskrahul.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * @author vskra
 *
 */
@SpringBootApplication
@EnableHystrix
public class CartApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartApplication.class);
	}
}
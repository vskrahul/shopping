/**
 * 
 */
package com.github.vskrahul.productcatalog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rahul Vishvakarma
 *
 */
@RestController
@RequestMapping(path="catalog")
public class ProductcatalogController {
	
	@GetMapping(path="list")
	public String catalog() {
		return "Here is the catalog.";
	}
	
}
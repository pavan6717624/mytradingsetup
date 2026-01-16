package com.mytradingsetup.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "AMAZON")
public class AmazonController {
	
	static RestTemplate template = new RestTemplate();
	static HttpHeaders headers = new HttpHeaders();
	static HttpEntity<String> entity = null;
	
	@RequestMapping(value = "demo")
	public String demo() {
		
		headers.set("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		headers.set("Accept", "text/html;q=0.9,*/*;q=0.8");
		headers.set("Accept-Language", "en-US,en;q=0.5");
		headers.set("Accept-Encoding", "utf-8");
		
		entity = new HttpEntity<String>(headers);
		String output = template.exchange("https://www.amazon.in/gp/bestsellers/kitchen/ref=zg_bs_nav_kitchen_0", HttpMethod.GET,entity, String.class)
				.getBody();
		
	System.out.println(output);
		
		return output;

	}
	

}

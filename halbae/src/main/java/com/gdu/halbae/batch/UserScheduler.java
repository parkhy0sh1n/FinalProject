package com.gdu.halbae.batch;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gdu.halbae.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class UserScheduler {

	private final UserService userService;
	
	@Scheduled(cron="0 0 0 1/1 * ?")
	public void excute() {
		userService.removeUnusedProfile();
	}
	
}

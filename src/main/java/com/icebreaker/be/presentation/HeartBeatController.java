package com.icebreaker.be.presentation;

import com.icebreaker.be.global.common.response.ApiResponseFactory;
import com.icebreaker.be.global.common.response.SuccessApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HeartBeatController {

    @GetMapping()
    public ResponseEntity<SuccessApiResponse<Void>> healthCheck() {
        return ResponseEntity
                .ok(ApiResponseFactory.success("OK"));
    }
}
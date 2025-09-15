package com.loanmaker.service;

import com.loanmaker.model.LoanRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class LoanRecommendationService {

    @Autowired
    private RestTemplate restTemplate;

    private final String RECOMMEND_URL = "https://loan-maker-ai-production.up.railway.app/recommend";

    public Map<String, Object> getRecommendations(LoanRequest request) {
        Map<String, Object> response = restTemplate.postForObject(RECOMMEND_URL, request, Map.class);
        return response;
    }
}

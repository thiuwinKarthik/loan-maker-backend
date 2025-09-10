package com.loanmaker.service;

import com.loanmaker.dto.LoanAiRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class LoanAIService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://127.0.0.1:8000";

    // 1️⃣ Prediction API
    public Map<String, Object> getLoanPrediction(LoanAiRequest request) {
        try {
            return restTemplate.postForObject(baseUrl + "/predict", request, Map.class);
        } catch (HttpClientErrorException e) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", "Prediction failed: " + e.getResponseBodyAsString());
            return error;
        } catch (Exception e) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", "Prediction failed: " + e.getMessage());
            return error;
        }
    }


    // 2️⃣ Recommendation API
    public Map<String, Object> getLoanRecommendations(LoanAiRequest request) {
        try {
            return restTemplate.postForObject(baseUrl + "/recommend", request, Map.class);
        } catch (HttpClientErrorException e) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", "Recommendation failed: " + e.getResponseBodyAsString());
            return error;
        } catch (Exception e) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", "Recommendation failed: " + e.getMessage());
            return error;
        }
    }

    // 3️⃣ Predict + Recommend API
    public Map<String, Object> predictAndRecommend(LoanAiRequest request) {
        Map<String, Object> response = new LinkedHashMap<>();

        Map<String, Object> prediction = getLoanPrediction(request);
        Map<String, Object> recommendations = getLoanRecommendations(request);

        response.put("prediction", prediction.getOrDefault("approved", null));
        response.put("recommendations", recommendations.getOrDefault("recommendations", null));

        return response;
    }
}

package com.loanmaker.controller;

import com.loanmaker.dto.LoanAiRequest;
import com.loanmaker.model.LoanRequest;
import com.loanmaker.service.LoanAIService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class LoanAIController {

    @Autowired
    private LoanAIService loanAIService;

    // 1️⃣ Loan Prediction
    @PostMapping("/predict")
    public ResponseEntity<Map<String, Object>> predictLoan(@Valid @RequestBody LoanAiRequest request) {
        Map<String, Object> aiResponse = loanAIService.getLoanPrediction(request);
        return ResponseEntity.ok(aiResponse);
    }

    // 2️⃣ Loan Recommendation
    @PostMapping("/recommend")
    public ResponseEntity<Map<String, Object>> recommendLoan(@Valid @RequestBody LoanAiRequest request) {
        Map<String, Object> recommendations = loanAIService.getLoanRecommendations(request);
        return ResponseEntity.ok(recommendations);
    }

    // 3️⃣ Predict + Recommend
    @PostMapping("/predict-and-recommend")
    public ResponseEntity<Map<String, Object>> predictAndRecommend(@Valid @RequestBody LoanAiRequest request) {
        Map<String, Object> result = loanAIService.predictAndRecommend(request);
        return ResponseEntity.ok(result);
    }
}

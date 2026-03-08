package com.metaminds.aicropdiseasedetection.core.services;

import com.metaminds.aicropdiseasedetection.core.services.GeminiService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GenAiResponseService {
    final GeminiService geminiService;

    public GenAiResponseService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public String getGeminiResponse(
            String prompt
    ){
        return geminiService.generateResponse(prompt);
    }

    public String predictDisease(
            MultipartFile image
    ){
        try{
            byte [] imageByte = image.getBytes();
            return geminiService.predictDisease(imageByte);
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}

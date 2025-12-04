package com.example.DietPlanBackend.controller;

import com.example.DietPlanBackend.api.CoachNotesRequest;
import com.example.DietPlanBackend.api.CoachNotesResponse;
import com.example.DietPlanBackend.rag.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
@CrossOrigin(origins = "http://localhost:4200") // adjust as needed
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;

    @PostMapping("/coach-notes")
    public CoachNotesResponse generateCoachNotes(@RequestBody CoachNotesRequest request) {
        String notes = ragService.generateCoachNotes(request);
        CoachNotesResponse res = new CoachNotesResponse();
        res.setCoachNotes(notes);
        return res;
    }
}

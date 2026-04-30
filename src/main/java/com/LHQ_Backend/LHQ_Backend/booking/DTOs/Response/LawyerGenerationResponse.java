package com.LHQ_Backend.LHQ_Backend.booking.DTOs.Response;

import java.util.List;
import lombok.Builder;

@Builder
public record LawyerGenerationResponse(String lawyerId, String lawyerFullName,
        int templatesProcessed, int slotsCreated, int slotsSkipped, List<String> errors // any
                                                                                        // per-lawyer
                                                                                        // failures,
                                                                                        // empty if
                                                                                        // clean
) {
}

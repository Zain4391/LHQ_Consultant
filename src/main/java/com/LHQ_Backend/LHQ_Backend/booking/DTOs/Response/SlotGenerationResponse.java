package com.LHQ_Backend.LHQ_Backend.booking.DTOs.Response;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

/**
 * Returned by: - The weekly @Scheduled job (logged, not HTTP) - POST /admin/slots/generate (manual
 * trigger for testing / recovery)
 *
 * Gives full visibility into what the scheduler did so issues are diagnosable.
 */
@Builder
public record SlotGenerationResponse(

        // Window that was processed
        Instant weekStart, Instant weekEnd,

        // Aggregate counts
        int templatesProcessed, int slotsCreated, int slotsSkipped, // skipped because slot already
                                                                    // existed for that window

        // Per-lawyer breakdown — useful for admin debugging
        List<LawyerGenerationResponse> lawyerSummaries,

        Instant generatedAt) {
}

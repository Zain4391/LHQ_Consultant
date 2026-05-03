package com.LHQ_Backend.LHQ_Backend.cases.mappers;

import com.LHQ_Backend.LHQ_Backend.cases.DTOs.Response.CaseResponse;
import com.LHQ_Backend.LHQ_Backend.cases.DTOs.Response.ClientLawyerResponse;
import com.LHQ_Backend.LHQ_Backend.cases.entity.Case;
import com.LHQ_Backend.LHQ_Backend.cases.entity.ClientLawyer;

public final class CaseMapper {

    private CaseMapper() {}

    public static CaseResponse toResponse(Case c) {
        ClientLawyer cl = c.getClientLawyer();
        return CaseResponse.builder().id(c.getId()).clientLawyerId(cl.getId())
                .userId(cl.getUser().getId())
                .userFullName(cl.getUser().getFirstName() + " " + cl.getUser().getLastName())
                .lawyerId(cl.getLawyer().getId())
                .lawyerFullName(cl.getLawyer().getUser().getFirstName() + " "
                        + cl.getLawyer().getUser().getLastName())
                .title(c.getTitle()).description(c.getDescription()).caseType(c.getCaseType())
                .status(c.getStatus()).openedAt(c.getOpenedAt()).closedAt(c.getClosedAt()).build();
    }

    public static ClientLawyerResponse toClientLawyerResponse(ClientLawyer cl) {
        return ClientLawyerResponse.builder().id(cl.getId()).userId(cl.getUser().getId())
                .userFullName(cl.getUser().getFirstName() + " " + cl.getUser().getLastName())
                .lawyerId(cl.getLawyer().getId())
                .lawyerFullName(cl.getLawyer().getUser().getFirstName() + " "
                        + cl.getLawyer().getUser().getLastName())
                .status(cl.getStatus()).createdAt(cl.getCreatedAt()).build();
    }
}

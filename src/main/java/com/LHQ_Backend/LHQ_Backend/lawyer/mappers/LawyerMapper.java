package com.LHQ_Backend.LHQ_Backend.lawyer.mappers;

import java.util.Set;
import java.util.stream.Collectors;
import com.LHQ_Backend.LHQ_Backend.lawyer.DTOs.Response.LawyerProfileResponse;
import com.LHQ_Backend.LHQ_Backend.lawyer.DTOs.Response.SpecialtyResponse;
import com.LHQ_Backend.LHQ_Backend.lawyer.entity.LawyerProfile;
import com.LHQ_Backend.LHQ_Backend.lawyer.entity.Specialty;

public final class LawyerMapper {

    private LawyerMapper() {}

    public static LawyerProfileResponse toResponse(LawyerProfile profile) {
        return LawyerProfileResponse.builder().id(profile.getId()).userId(profile.getUser().getId())
                .firstName(profile.getUser().getFirstName()) // denormalized
                .lastName(profile.getUser().getLastName()) // denormalized
                .email(profile.getUser().getEmail()) // denormalized
                .bio(profile.getBio()).about(profile.getAbout()).rate(profile.getRate())
                .barNumber(profile.getBarNumber()).yearsOfExperience(profile.getYearsOfExperience())
                .specialties(mapSpecialties(profile.getSpecialties()))
                .createdAt(profile.getCreatedAt()).build();
    }

    public static SpecialtyResponse toSpecialtyResponse(Specialty specialty) {
        return SpecialtyResponse.builder().id(specialty.getId()).name(specialty.getName()).build();
    }

    private static Set<SpecialtyResponse> mapSpecialties(Set<Specialty> specialties) {
        if (specialties == null || specialties.isEmpty()) {
            return Set.of();
        }

        return specialties.stream().map(LawyerMapper::toSpecialtyResponse)
                .collect(Collectors.toSet());
    }
}

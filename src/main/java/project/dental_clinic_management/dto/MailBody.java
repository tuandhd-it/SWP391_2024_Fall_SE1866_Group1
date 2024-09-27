package project.dental_clinic_management.dto;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {
}

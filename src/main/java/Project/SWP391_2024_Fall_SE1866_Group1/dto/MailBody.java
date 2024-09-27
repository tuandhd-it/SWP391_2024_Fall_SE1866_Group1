package Project.SWP391_2024_Fall_SE1866_Group1.dto;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {
}

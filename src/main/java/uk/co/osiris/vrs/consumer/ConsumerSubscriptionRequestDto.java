package uk.co.osiris.vrs.consumer;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerSubscriptionRequestDto {

    @NotBlank
    private String deliveryMethod;

    private String config;
}

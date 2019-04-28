package by.training.info_system.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class BlackList {
    @NonNull private User user;
    @NonNull private String reason;
    @NonNull private LocalDate lockDate;
    @NonNull private LocalDate unlockDate;
}

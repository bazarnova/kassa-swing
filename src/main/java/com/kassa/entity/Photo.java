package com.kassa.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {
    String id;
    String fileId;
    LocalDate messageDate;
    LocalDate addedDate;
    Boolean processed;

    String path;
}

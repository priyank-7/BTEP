package org.drive.headers;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Metrics {
    private double freeSpace;
    private double usedMemory;
    private int activeThread;
}


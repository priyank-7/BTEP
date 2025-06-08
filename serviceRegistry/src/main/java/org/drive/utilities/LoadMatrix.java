package org.drive.utilities;

import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoadMatrix implements Serializable {
    private int threadCount;
    private long heapMemory;
    private double cpuLoad;
    private long usedSpace;
    private long totalSpace;
    private long freeSpace;
    private long IOBytes;
}

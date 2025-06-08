package org.drive;

import org.drive.utilities.LoadMatrix;
import oshi.SystemInfo;
import oshi.hardware.NetworkIF;

import java.lang.management.*;
import java.io.File;
import java.util.List;

public class AppLoadMatrix {
    public static LoadMatrix getLoadMatrix() {
        return collectMetrics();
    }
    private static LoadMatrix collectMetrics() {
        // Threads
        int threadCount = ManagementFactory.getThreadMXBean().getThreadCount();

        // Memory
        long heapMemory = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();

        // CPU Load
        double cpuLoad = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
        long IOBytes = getNetworkBytes();

        // Disk Usage
        File disk = new File("/");
        long totalSpace = disk.getTotalSpace();
        long freeSpace = disk.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        //        disk.getUsableSpace()

        return LoadMatrix.builder()
                .threadCount(threadCount)
                .heapMemory(heapMemory)
                .cpuLoad(cpuLoad)
                .IOBytes(IOBytes)
                .totalSpace(totalSpace)
                .usedSpace(usedSpace)
                .freeSpace(freeSpace)
                .build();
    }
    private static long getNetworkBytes() {
        SystemInfo systemInfo = new SystemInfo();
        List<NetworkIF> networkIFs = systemInfo.getHardware().getNetworkIFs();
        long totalBytes = 0;

        for (NetworkIF net : networkIFs) {
            totalBytes += net.getBytesRecv() + net.getBytesSent();
        }
        return totalBytes;
    }

}

package org.drive;

import org.drive.utilities.LoadMatrix;

import java.lang.management.*;
import java.io.File;

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
                .totalSpace(totalSpace)
                .usedSpace(usedSpace)
                .freeSpace(freeSpace)
                .build();

        // Network I/O
//        long networkBytes = 0;

        // Calculate load matrix


//        double loadMatrix = (threadCount * 0.4) +
//                ((heapMemory / (1024 * 1024)) * 0.2) +
//                ((usedSpace / (1024 * 1024 * 1024)) * 0.2) +
//                (cpuLoad * 0.1) +
//                ((networkBytes / (1024 * 1024)) * 0.1);

        // Print metrics
//        System.out.println("🧵 Threads: " + threadCount);
//        System.out.println("🖥️ Heap Memory (MB): " + heapMemory / (1024 * 1024));
//        System.out.println("💾 Disk Usage (GB): " + usedSpace / (1024 * 1024 * 1024));
//        System.out.println("⚙️ CPU Load: " + String.format("%.2f", cpuLoad));
//        System.out.println("📡 Network I/O (MB): " + networkBytes / (1024 * 1024));
//        System.out.println("📊 Load Matrix: " + String.format("%.2f", loadMatrix));
//        System.out.println("---------------------------------------------------");
    }
//    private static long getNetworkBytes() {
//        long totalBytes = 0;
//        try {
//            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//            while (interfaces.hasMoreElements()) {
//                NetworkInterface networkInterface = interfaces.nextElement();
//                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
//                    totalBytes += networkInterface.getRxBytes() + networkInterface.getTxBytes();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return totalBytes;
//    }
}

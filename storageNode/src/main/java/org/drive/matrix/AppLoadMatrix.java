package org.drive.matrix;

import java.lang.management.*;
import java.io.File;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class AppLoadMatrix {

    public static void main(String[] args) {
        while (true) {
            collectMetrics();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void collectMetrics() {
        // Threads
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        int threadCount = threadBean.getThreadCount();

        // Memory
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long heapMemory = memoryBean.getHeapMemoryUsage().getUsed();

        // CPU Load
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        double cpuLoad = osBean.getSystemLoadAverage();

        // Disk Usage
        File disk = new File("/");
        long totalSpace = disk.getTotalSpace();
        long freeSpace = disk.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;

        // Network I/O
        long networkBytes = getNetworkBytes();

        // Calculate load matrix
        double loadMatrix = (threadCount * 0.4) +
                ((heapMemory / (1024 * 1024)) * 0.2) +
                ((usedSpace / (1024 * 1024 * 1024)) * 0.2) +
                (cpuLoad * 0.1) +
                ((networkBytes / (1024 * 1024)) * 0.1);

        // Print metrics
        System.out.println("🧵 Threads: " + threadCount);
        System.out.println("🖥️ Heap Memory (MB): " + heapMemory / (1024 * 1024));
        System.out.println("💾 Disk Usage (GB): " + usedSpace / (1024 * 1024 * 1024));
        System.out.println("⚙️ CPU Load: " + String.format("%.2f", cpuLoad));
        System.out.println("📡 Network I/O (MB): " + networkBytes / (1024 * 1024));
        System.out.println("📊 Load Matrix: " + String.format("%.2f", loadMatrix));
        System.out.println("---------------------------------------------------");
    }

    private static long getNetworkBytes() {
        long totalBytes = 0;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
//                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
//                    totalBytes += networkInterface.getRxBytes() + networkInterface.getTxBytes();
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalBytes;
    }
}

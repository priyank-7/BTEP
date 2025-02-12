package org.drive.utilities;

import java.io.Serializable;
import java.net.InetSocketAddress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.drive.headers.RequestType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReplicateRequest implements Serializable {

    private String replicationId;
    private String filePath;
    private InetSocketAddress address;
    private RequestType requestType;
    private String NodeId;

    public ReplicateRequest(String replicationId, String filePath) {
        this.replicationId = replicationId;
        this.filePath = filePath;
    }

    public ReplicateRequest(String replicationId, String filePath, String nodeId) {
        this.replicationId = replicationId;
        this.filePath = filePath;
        this.NodeId = nodeId;
    }
}
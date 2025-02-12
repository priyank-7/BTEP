package org.drive.utilities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.InetSocketAddress;
import java.util.Date;

@Getter
@Setter
@Builder
@ToString
public class NodeInfo implements java.io.Serializable {

    private String nodeId;
    private NodeType nodetype;
    private InetSocketAddress nodeAddress;
    private NodeStatus status;
    private Date registrationTime;
    private Date lastResponse;
    private int failedAttempts;
    private int totalAttempts;
}
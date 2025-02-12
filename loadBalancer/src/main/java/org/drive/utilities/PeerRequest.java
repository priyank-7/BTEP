package org.drive.utilities;

import lombok.*;
import org.drive.headers.RequestType;

import java.net.InetSocketAddress;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PeerRequest implements java.io.Serializable {

    private RequestType requestType;
    private NodeType nodeType;
    private InetSocketAddress socketAddress;
    private Object payload;

    public PeerRequest(RequestType requestType, InetSocketAddress socketAddress, NodeType nodeType, Object payload) {
        this.requestType = requestType;
        this.socketAddress = socketAddress;
        this.nodeType = nodeType;
        this.payload = payload;
    }

    public PeerRequest(RequestType requestType, InetSocketAddress socketAddress, NodeType nodeType) {
        this.requestType = requestType;
        this.socketAddress = socketAddress;
        this.nodeType = nodeType;
    }

    public PeerRequest(RequestType requestType) {
        this.requestType = requestType;
    }
}

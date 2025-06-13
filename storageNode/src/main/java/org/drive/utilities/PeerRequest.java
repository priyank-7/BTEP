package org.drive.utilities;

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

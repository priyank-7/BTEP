package org.drive.headers;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Request implements java.io.Serializable {

    private RequestType requestType;
    private String token;
    private Object payload;

    public Request(RequestType requestType, String token) {
        this.requestType = requestType;
        this.token = token;
    }

    public Request(RequestType requestType, Object payload) {
        this.requestType = requestType;
        this.payload = payload;
    }

    public Request(RequestType requestType) {
        this.requestType = requestType;
    }
}


package org.drive.headers;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Response implements java.io.Serializable {
    private StatusCode statusCode;
    private Object payload;
    private byte[] data;
    private int dataSize;

    public Response(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public Response(StatusCode statusCode, Object payload) {
        this.statusCode = statusCode;
        this.payload = payload;
    }
}

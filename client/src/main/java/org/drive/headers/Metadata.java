package org.drive.headers;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Metadata implements Serializable {
    private String metadataId; // Primary Key
    private String name;  // notnull
    private long size;  // notnull
    private String path;
    private boolean isFolder;
    private Date createdDate;  // notnull
    private Date modifiedDate; // notnull
    private String owner; // notnull
    private List<String> sharedWith;
}
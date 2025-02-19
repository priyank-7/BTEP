package org.drive.headers;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

//import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
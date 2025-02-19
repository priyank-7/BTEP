package org.drive.db;

import org.drive.headers.Metadata;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.sql.*;
import java.util.*;

public class MetadataDao {

    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    // Save metadata
    public boolean saveMetadata(Metadata metadata) {
        String sql = "INSERT INTO Metadata (metadata_id, name, size, path, is_folder, created_date, modified_date, owner) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, metadata.getMetadataId());
            stmt.setString(2, metadata.getName());
            stmt.setLong(3, metadata.getSize());
            stmt.setString(4, metadata.getPath());
            stmt.setBoolean(5, metadata.isFolder());
            stmt.setTimestamp(6, new Timestamp(metadata.getCreatedDate().getTime()));
            stmt.setTimestamp(7, new Timestamp(metadata.getModifiedDate().getTime()));
            stmt.setString(8, metadata.getOwner());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update metadata
    public boolean updateMetadata(String fileName, String ownerId, Metadata newMetadata) {
        String sql = "UPDATE Metadata SET name = ?, size = ?, path = ?, is_folder = ?, created_date = ?, modified_date = ? WHERE name = ? AND owner = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newMetadata.getName());
            stmt.setLong(2, newMetadata.getSize());
            stmt.setString(3, newMetadata.getPath());
            stmt.setBoolean(4, newMetadata.isFolder());
            stmt.setTimestamp(5, new Timestamp(newMetadata.getCreatedDate().getTime()));
            stmt.setTimestamp(6, new Timestamp(newMetadata.getModifiedDate().getTime()));
            stmt.setString(7, fileName);
            stmt.setString(8, ownerId);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete metadata by name and ownerId
    public boolean deleteMetadata(String fileName, String ownerId) {
        String sql = "DELETE FROM Metadata WHERE name = ? AND owner = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fileName);
            stmt.setString(2, ownerId);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get metadata by file name and ownerId
    public Metadata getMetadata(String fileName, String ownerId) {
        String sql = "SELECT * FROM Metadata WHERE name = ? AND owner = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fileName);
            stmt.setString(2, ownerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Metadata metadata = new Metadata();
                    metadata.setMetadataId(rs.getString("metadata_id"));
                    metadata.setName(rs.getString("name"));
                    metadata.setSize(rs.getLong("size"));
                    metadata.setPath(rs.getString("path"));
                    metadata.setFolder(rs.getBoolean("is_folder"));
                    metadata.setCreatedDate(rs.getTimestamp("created_date"));
                    metadata.setModifiedDate(rs.getTimestamp("modified_date"));
                    metadata.setOwner(rs.getString("owner"));
                    return metadata;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Delete metadata by Metadata object
    public boolean deleteMetadata(Metadata tempMetadata) {
        return deleteMetadata(tempMetadata.getName(), tempMetadata.getOwner());
    }
}

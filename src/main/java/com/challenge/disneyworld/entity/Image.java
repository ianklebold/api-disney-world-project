package com.challenge.disneyworld.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Image {
    
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="uuid",strategy="uuid2")
    private String id;

    @Column(name = "name", nullable = false, updatable = true)
    private String name;

    @Column(name = "fileType", nullable = false, updatable = true)
    private String fileType;

    @Lob
    @Column(name = "fileData", nullable = false, updatable = true)
    private byte[] fileData;

    public Image() {
    }

    public Image(String name, String fileType, byte[] fileData) {
        this.name = name;
        this.fileType = fileType;
        this.fileData = fileData;
    }

    /**
     * @return String return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return byte[] return the fileData
     */
    public byte[] getFileData() {
        return fileData;
    }

    /**
     * @param fileData the fileData to set
     */
    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

}

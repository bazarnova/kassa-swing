package com.kassa.entity;

import java.time.LocalDate;

public class Photo {
    String id;
    String fileId;
    LocalDate messageDate;
    LocalDate addedDate;
    Boolean processed;

    String path;

    public Photo() {
    }

    public Photo(Builder builder) {
        this.fileId = builder.fileId;
        this.messageDate = builder.messageDate;
        this.addedDate = builder.addedDate;
        this.processed = builder.processed;
        this.path = builder.path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public LocalDate getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(LocalDate messageDate) {
        this.messageDate = messageDate;
    }

    public LocalDate getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static class Builder {
        String id;
        String fileId;
        LocalDate messageDate;
        LocalDate addedDate;
        Boolean processed;
        String path;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setFileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder setMessageDate(LocalDate messageDate) {
            this.messageDate = messageDate;
            return this;
        }

        public Builder setAddedDate(LocalDate addedDate) {
            this.addedDate = addedDate;
            return this;
        }

        public Builder setProcessed(Boolean processed) {
            this.processed = processed;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }
        public Photo build(){
            return new Photo(this);
        }
    }

}

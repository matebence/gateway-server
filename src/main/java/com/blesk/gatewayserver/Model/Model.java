package com.blesk.gatewayserver.Model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Model {

    public static class AccessToken {
        private String token;

        public AccessToken() {
        }

        public String getToken() {
            return this.token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class Communications {

        private String communicationId;

        private Long version;

        private String userName;

        private Long sender;

        private String content;

        private Conversations conversations;

        private Date date = new Date();

        private Boolean isDeleted = false;

        private Date createdAt = new Date();

        private Date updatedAt = null;

        private Date deletedAt = null;

        public Communications() {
        }

        public String getCommunicationId() {
            return this.communicationId;
        }

        public void setCommunicationId(String communicationId) {
            this.communicationId = communicationId;
        }

        public String getUserName() {
            return this.userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Long getSender() {
            return this.sender;
        }

        public void setSender(Long sender) {
            this.sender = sender;
        }

        public String getContent() {
            return this.content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Conversations getConversations() {
            return this.conversations;
        }

        public void setConversations(Conversations conversations) {
            this.conversations = conversations;
        }

        public Date getDate() {
            return this.date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Boolean getDeleted() {
            return this.isDeleted;
        }

        public void setDeleted(Boolean deleted) {
            this.isDeleted = deleted;
        }

        public Date getCreatedAt() {
            return this.createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public Date getUpdatedAt() {
            return this.updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Date getDeletedAt() {
            return this.deletedAt;
        }

        public void setDeletedAt(Date deletedAt) {
            this.deletedAt = deletedAt;
        }
    }

    public static class Conversations {

        private String conversationId;

        private Long version;

        private Set<Users> participants = new HashSet<>();

        private Boolean isDeleted = false;

        private Date createdAt = new Date();

        private Date updatedAt = null;

        private Date deletedAt = null;

        public Conversations() {
        }

        public String getConversationId() {
            return this.conversationId;
        }

        public void setConversationId(String conversationId) {
            this.conversationId = conversationId;
        }

        public Set<Users> getParticipants() {
            return this.participants;
        }

        public void setParticipants(Set<Users> participants) {
            this.participants = participants;
        }

        public Boolean getDeleted() {
            return this.isDeleted;
        }

        public void setDeleted(Boolean deleted) {
            this.isDeleted = deleted;
        }

        public Date getCreatedAt() {
            return this.createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public Date getUpdatedAt() {
            return this.updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Date getDeletedAt() {
            return this.deletedAt;
        }

        public void setDeletedAt(Date deletedAt) {
            this.deletedAt = deletedAt;
        }
    }

    public static class Users {

        private Long accountId;

        private Status status;

        private String userName;

        private String lastConversionId = null;

        private String lastReadedConversionId = null;

        public Users() {
        }

        public Long getAccountId() {
            return this.accountId;
        }

        public void setAccountId(Long accountId) {
            this.accountId = accountId;
        }

        public Status getStatus() {
            return this.status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public String getUserName() {
            return this.userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getLastConversionId() {
            return this.lastConversionId;
        }

        public void setLastConversionId(String lastConversionId) {
            this.lastConversionId = lastConversionId;
        }

        public String getLastReadedConversionId() {
            return this.lastReadedConversionId;
        }

        public void setLastReadedConversionId(String lastReadedConversionId) {
            this.lastReadedConversionId = lastReadedConversionId;
        }
    }

    public static enum State {
        ONLINE, OFFLINE
    }

    public static class Status {

        private String statusId;

        private Long version;

        private String userName;

        private String token;

        private String state;

        private Boolean isDeleted = false;

        private Date createdAt = new Date();

        private Date updatedAt = null;

        private Date deletedAt = null;

        public Status() {
        }

        public String getStatusId() {
            return this.statusId;
        }

        public void setStatusId(String statusId) {
            this.statusId = statusId;
        }

        public String getUserName() {
            return this.userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getToken() {
            return this.token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getState() {
            return this.state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Boolean getDeleted() {
            return this.isDeleted;
        }

        public void setDeleted(Boolean deleted) {
            this.isDeleted = deleted;
        }

        public Date getCreatedAt() {
            return this.createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public Date getUpdatedAt() {
            return this.updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Date getDeletedAt() {
            return this.deletedAt;
        }

        public void setDeletedAt(Date deletedAt) {
            this.deletedAt = deletedAt;
        }
    }
}
package com.blesk.gatewayserver.DTO;

import com.blesk.gatewayserver.Model.Model;

public class Websocket {

    public static class Status {

        private Model.Status status;

        private Model.AccessToken accessToken;

        public Status() {
        }

        public Model.Status getStatus() {
            return this.status;
        }

        public void setStatus(Model.Status status) {
            this.status = status;
        }

        public Model.AccessToken getAccessToken() {
            return this.accessToken;
        }

        public void setAccessToken(Model.AccessToken accessToken) {
            this.accessToken = accessToken;
        }
    }

    public static class Communications {

        private Model.Communications communications;

        private Model.AccessToken accessToken;

        public Communications() {
        }

        public Model.Communications getCommunications() {
            return this.communications;
        }

        public void setCommunications(Model.Communications communications) {
            this.communications = communications;
        }

        public Model.AccessToken getAccessToken() {
            return this.accessToken;
        }

        public void setAccessToken(Model.AccessToken accessToken) {
            this.accessToken = accessToken;
        }
    }
}
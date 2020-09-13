package com.blesk.gatewayserver.Proxy;

import feign.Headers;
import com.blesk.gatewayserver.Model.WebSocket;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Repository
@FeignClient(name = "messaging-service")
@RibbonClient(name = "messaging-service")
public interface MessagingServiceProxy {

    @PostMapping("api/status")
    @Headers("Content-Type: application/json")
    EntityModel<WebSocket.Status> createStatus(@Valid @RequestBody WebSocket.Status statuses);

    @GetMapping("api/status/{statusId}")
    @Headers("Content-Type: application/json")
    EntityModel<WebSocket.Status> retrieveStatus(@PathVariable("statusId") String statusId);

    @PostMapping("api/communications")
    @Headers("Content-Type: application/json")
    EntityModel<WebSocket.Communications> createCommunications(@Valid @RequestBody WebSocket.Communications communications);
}
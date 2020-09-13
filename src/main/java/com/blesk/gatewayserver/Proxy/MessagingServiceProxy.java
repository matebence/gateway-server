package com.blesk.gatewayserver.Proxy;

import feign.Headers;
import com.blesk.gatewayserver.Model.WebSocket;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.validation.Valid;

@Repository
@FeignClient(name = "messaging-service", fallback = MessagingServiceProxyFallback.class)
@RibbonClient(name = "messaging-service")
public interface MessagingServiceProxy {

    @PostMapping("/status")
    @Headers("Content-Type: application/json")
    EntityModel<WebSocket.Status> createStatus(@Valid @RequestBody WebSocket.Status statuses);

    @GetMapping("/status/{statusId}")
    @Headers("Content-Type: application/json")
    EntityModel<WebSocket.Status> retrieveStatus(@PathVariable("statusId") String statusId);

    @PostMapping("/communications")
    @Headers("Content-Type: application/json")
    EntityModel<WebSocket.Communications> createCommunications(@Valid @RequestBody WebSocket.Communications communications);
}

@Component
class MessagingServiceProxyFallback implements MessagingServiceProxy {

    @Override
    public EntityModel<WebSocket.Status> createStatus(@Valid @RequestBody WebSocket.Status statuses) {
        throw new NotImplementedException();
    }

    @Override
    public EntityModel<WebSocket.Status> retrieveStatus(@PathVariable("statusId") String statusId) {
        throw new NotImplementedException();
    }

    @Override
    public EntityModel<WebSocket.Communications> createCommunications(@Valid @RequestBody WebSocket.Communications communications) {
        throw new NotImplementedException();
    }
}
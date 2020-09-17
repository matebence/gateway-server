package com.blesk.gatewayserver.Proxy;

import feign.Headers;
import com.blesk.gatewayserver.Model.Model;
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
    EntityModel<Model.Status> createStatus(@Valid @RequestBody Model.Status statuses);

    @GetMapping("api/status/{statusId}")
    @Headers("Content-Type: application/json")
    EntityModel<Model.Status> retrieveStatus(@PathVariable("statusId") String statusId);

    @PostMapping("api/communications")
    @Headers("Content-Type: application/json")
    EntityModel<Model.Communications> createCommunications(@Valid @RequestBody Model.Communications communications);
}
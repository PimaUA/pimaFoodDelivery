package com.pimaua.payment.client;

import com.pimaua.payment.config.OrderServiceClientConfig;
import com.pimaua.payment.dto.OrderForPaymentDto;
import com.pimaua.payment.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", configuration = OrderServiceClientConfig.class)
public interface OrderServiceClient {

    @GetMapping(value = "/api/orders/{orderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<OrderForPaymentDto> fetchOrderDetails(@PathVariable("orderId") Integer id);
}

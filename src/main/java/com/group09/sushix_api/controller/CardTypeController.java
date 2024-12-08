package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.request.CardTypeRequest;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.CardTypeResponse;
import com.group09.sushix_api.service.CardTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card-type")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CardTypeController {

    CardTypeService cardTypeService;

    @GetMapping
    public ApiResponse<List<CardTypeResponse>> getCardTypes() {
        return ApiResponse.<List<CardTypeResponse>>builder()
                .result(cardTypeService.getAllCardTypes())
                .build();
    }

    @GetMapping("/{cardTypeId}")
    public ApiResponse<CardTypeResponse> getCardType(@PathVariable("cardTypeId") Integer cardTypeId) {
        return ApiResponse.<CardTypeResponse>builder()
                .result(cardTypeService.getCardType(cardTypeId))
                .build();
    }

    @PostMapping
    public ApiResponse<CardTypeResponse> createCardType(@RequestBody @Valid CardTypeRequest request) {
        return ApiResponse.<CardTypeResponse>builder()
                .result(cardTypeService.createCardType(request))
                .build();
    }

    @PutMapping("/{cardTypeId}")
    public ApiResponse<CardTypeResponse> updateCardType(@PathVariable("cardTypeId") Integer cardTypeId,
                                                        @RequestBody @Valid CardTypeRequest request) {
        return ApiResponse.<CardTypeResponse>builder()
                .result(cardTypeService.updateCardType(cardTypeId, request))
                .build();
    }

    @DeleteMapping("/{cardTypeId}")
    public ApiResponse<String> deleteCardType(@PathVariable Integer cardTypeId) {
        cardTypeService.deleteCardType(cardTypeId);
        return ApiResponse.<String>builder().result("Card type has been deleted").build();
    }
}

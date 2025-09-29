package com.shu.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class APIResponse {
    String message;
}

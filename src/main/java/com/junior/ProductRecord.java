package com.junior;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductRecord(
        @JsonProperty Integer id,
        @JsonProperty String name,
        @JsonProperty int status) {
}

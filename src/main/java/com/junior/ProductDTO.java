package com.junior;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record ProductDTO(@NotNull @NotEmpty String name, @Max(1) @Min(0)int status) {

    public Product toEntity() {
        return new Product(null, this.name, this.status);
    }
}

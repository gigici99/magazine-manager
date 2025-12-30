package com.demo.manager.model;

import com.demo.manager.model.utils.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("movements")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Movement {
    @Id
    private String id;

    @Indexed
    private String productId;

    // the quantity to applicated at stock: positive or negative
    private int delta;

    // the motive of variation using enum type
    private Reason reason;

    private String note;

    // audit
    private Instant createdAt;
    private String createdBy;
}

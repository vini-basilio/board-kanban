package br.com.dio.persistence.entity;

import java.time.OffsetDateTime;

@Data
public class BlockEntity {
    private Long id;
    private OffsetDateTime blocked_at;
}

package sanity.nil.account.infrastructure.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts", schema = "public")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountModel {

    public final static String FIELD_ID = "id";
    public final static String FIELD_BALANCE = "balance";

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "active")
    private boolean active;

    @Column(name = "created_at", columnDefinition = "timestamptz")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public AccountModel(UUID id, String username, BigDecimal balance, boolean active) {
        this.id = id;
        this.username = username;
        this.balance = balance;
        this.active = active;
    }
}

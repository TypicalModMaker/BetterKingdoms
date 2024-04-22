package dev.isnow.betterkingdoms.kingdoms.impl.model.base;

import io.ebean.Model;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseKingdom extends Model {
    @Id
    long id;
}

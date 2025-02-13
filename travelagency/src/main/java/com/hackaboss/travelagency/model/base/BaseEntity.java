package com.hackaboss.travelagency.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.type.descriptor.java.BooleanJavaType;

@MappedSuperclass
@FilterDef(name = "activeFilter", parameters = @ParamDef(name = "active", type = BooleanJavaType.class))
public abstract class BaseEntity {
    // Puedes incluir campos comunes, por ejemplo, 'active' si lo deseas
    @Column(name = "active")
    protected Boolean active = true;

    // Getter y setter para active
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
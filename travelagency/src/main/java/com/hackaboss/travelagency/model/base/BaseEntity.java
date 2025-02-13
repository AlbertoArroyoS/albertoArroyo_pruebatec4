package com.hackaboss.travelagency.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.type.descriptor.java.BooleanJavaType;

@Setter
@Getter
@MappedSuperclass
@FilterDef(name = "activeFilter", parameters = @ParamDef(name = "active", type = BooleanJavaType.class))
public abstract class BaseEntity {

    @Column(name = "active")
    protected Boolean active = true;

}
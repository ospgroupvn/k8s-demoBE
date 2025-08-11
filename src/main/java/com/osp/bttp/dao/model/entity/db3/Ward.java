package com.osp.bttp.dao.model.entity.db3;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "WARD")
public class Ward {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "WARD_CODE", length = 6)
    private String wardCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PROVINCE_CODE", length = 2)
    private String provinceCode;
}

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
@Table(name = "PLACE_OF_ISSUE")
public class PlaceOfIssue {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CODE", length = 10)
    private String code;

    @Column(name = "NAME", length = 150)
    private String name;
}

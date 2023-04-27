package com.amasy.gtbackend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SchemeCat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer schemeId;
    private String schemeName;
    @OneToMany(mappedBy = "schemeCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonBackReference
    private List<TpUser> tpUsers = new ArrayList<>();
    @OneToMany(mappedBy = "schemeCat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonBackReference
    private List<SrcUser> srcUsers = new ArrayList<>();
}

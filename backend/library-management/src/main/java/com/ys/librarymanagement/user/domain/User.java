package com.ys.librarymanagement.user.domain;

import com.ys.librarymanagement.common.base.AbstractTimeColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class User extends AbstractTimeColumn {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}

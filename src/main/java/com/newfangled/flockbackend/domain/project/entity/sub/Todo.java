package com.newfangled.flockbackend.domain.project.entity.sub;

import com.newfangled.flockbackend.domain.project.embed.TodoId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Todo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TodoId todoId;

}

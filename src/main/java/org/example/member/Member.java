package org.example.member;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Member implements Serializable {
    private String id;
    private String name;
    private Integer age;
}


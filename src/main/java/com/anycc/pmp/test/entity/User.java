package com.anycc.pmp.test.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="test_userinfo")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
    @GenericGenerator(name="uuid", strategy = "uuid")
    private String id;

    /**
     * 名称
     */
    @Column(name="userName",nullable=false, length=255)
    private String userName;

    /**
     * age
     */
    @Column(name="age",nullable=false, length=16)
    private String age;

    /**
     * age
     */
    @Column(name="ddr",nullable=false, length=255)
    private String drr;

}

package com.eeit45team2.lungspringbootversion.backend.member.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class ConfirmationToken {


        @Id   //PK值
        @GeneratedValue(strategy = GenerationType.IDENTITY)  //SQL自動新增
        @Column(name="token_id")
        private long tokenid;

        @Column(name="confirmation_token")
        private String confirmationToken;

        @Temporal(TemporalType.TIMESTAMP)
        private Date createdDate;

        @OneToOne(targetEntity = MemberBean.class, fetch = FetchType.EAGER)
        @JoinColumn(nullable = false, name = "miNo")
        private MemberBean member;

        public ConfirmationToken(MemberBean member) {
            this.member = member;
            createdDate = new Date();
            confirmationToken = UUID.randomUUID().toString();
        }

        public ConfirmationToken() {
        }
}

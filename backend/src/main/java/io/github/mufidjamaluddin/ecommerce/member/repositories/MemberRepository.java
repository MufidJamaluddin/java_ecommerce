package io.github.mufidjamaluddin.ecommerce.member.repositories;

import io.github.mufidjamaluddin.ecommerce.member.models.Member;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface MemberRepository extends ReactiveCrudRepository<Member, UUID> {

    @Query("SELECT * FROM member where mobile_number = :mobileNumber LIMIT 1")
    Mono<Member> findMemberByMobileNumber(String mobileNumber);

    @Query("SELECT * FROM member WHERE email = :email LIMIT 1")
    Mono<Member> findMemberByEmail(String email);

    @Query("SELECT * FROM member WHERE email = :email OR mobile_number = :mobileNumber LIMIT 1")
    Mono<Member> findMemberByEmailOrMobileNumber(String email, String mobileNumber);

    @Query("SELECT EXISTS (SELECT id FROM member WHERE email = :email OR mobile_number = :mobileNumber)")
    Mono<Boolean> isAccountExist(String email, String mobileNumber);

}

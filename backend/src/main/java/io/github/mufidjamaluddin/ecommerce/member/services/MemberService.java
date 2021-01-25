package io.github.mufidjamaluddin.ecommerce.member.services;

import io.github.mufidjamaluddin.ecommerce.member.exceptions.AccountExistException;
import io.github.mufidjamaluddin.ecommerce.member.exceptions.AccountIsNotExistException;
import io.github.mufidjamaluddin.ecommerce.member.models.Member;
import io.github.mufidjamaluddin.ecommerce.member.repositories.MemberRepository;
import io.github.mufidjamaluddin.ecommerce.utils.EmailValidator;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class MemberService implements ReactiveUserDetailsService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Mono<Object> register(Member member) {

        return this.memberRepository.
                isAccountExist(
                    member.getEmail(),
                    member.getMobileNumber()
                )
                .map(exist -> {
                    if(!exist) {
                        member.setRoles("member");
                        return this.memberRepository.save(member);
                    }
                    return Mono.error(new AccountExistException(
                        String.format("(%s - %s)",
                            member.getMobileNumber(),
                            member.getEmail())
                    ));
                }).
                flatMap(m -> m);
    }

    public Mono<Object> update(Member member) {
        return this.memberRepository.
                findMemberByEmailOrMobileNumber(
                    member.getEmail(),
                    member.getMobileNumber()
                )
                .map(oldData -> {
                    if(oldData != null) {

                        oldData.setFirstName(member.getFirstName());
                        oldData.setLastName(member.getLastName());
                        oldData.setGender(member.getGender());
                        oldData.setDateOfBirth(member.getDateOfBirth());

                        return this.memberRepository.save(oldData);
                    }
                    return Mono.error(new AccountIsNotExistException(
                        String.format("(%s - %s)",
                            member.getMobileNumber(),
                            member.getEmail())
                    ));
                }).
                flatMap(m -> m);
    }

    public Mono<Member> findByEmailOrMobileNumber(String key) {
        Mono<Member> member;

        if(EmailValidator.isEmail(key)) {
            member = this.memberRepository.findMemberByEmail(key);
        } else {
            member = this.memberRepository.findMemberByMobileNumber(key);
        }

        return member
                .filter(Objects::nonNull)
                .switchIfEmpty(
                    Mono.error(new BadCredentialsException(
                        String.format("User %s not found in database", key))
                    )
                );
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return this.findByEmailOrMobileNumber(username).map(m -> m);
    }
}

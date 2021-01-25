package io.github.mufidjamaluddin.ecommerce.member.handlers;

import io.github.mufidjamaluddin.ecommerce.member.models.Member;
import io.github.mufidjamaluddin.ecommerce.member.services.MemberService;
import io.github.mufidjamaluddin.ecommerce.member.viewmodels.MemberViewModel;
import io.github.mufidjamaluddin.ecommerce.member.viewmodels.RegisterViewModel;
import io.github.mufidjamaluddin.ecommerce.security.SecurityUtils;
import io.github.mufidjamaluddin.ecommerce.shared.Gender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/member")
public class MemberAccountHandler {

    private final MemberService memberService;
    private final Validator validator;

    public MemberAccountHandler(MemberService memberService, Validator validator) {
        this.memberService = memberService;
        this.validator = validator;
    }

    @GetMapping("/profile")
    public Mono<ResponseEntity<MemberViewModel>> getProfile(
            ServerWebExchange serverWebExchange) {

        return SecurityUtils.getUserFromRequest(serverWebExchange)
                .switchIfEmpty(
                    Mono.create(e -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("{\"status\":\"unauthorized\"}"))
                )
                .map(this.memberService::findByEmailOrMobileNumber)
                .flatMap(m -> m)
                .map(Mapper::fromServiceModel)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/profile")
    public Mono<ResponseEntity<Object>> updateProfile(
            ServerWebExchange serverWebExchange,
            @RequestBody MemberViewModel data
    ) {
        var violations = this.validator.validate(data);
        if(!violations.isEmpty()) {
            return Mono.create(e -> ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            violations.stream()
                                    .map(ConstraintViolation::getMessage)
                                    .collect(Collectors.toList())
                    )
            );
        }

        return SecurityUtils.getUserFromRequest(serverWebExchange)
                .switchIfEmpty(
                    Mono.create(e -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("{\"status\":\"unauthorized\"}"))
                )
                .map(user -> {
                    if(
                        Objects.equals(user, data.getEmail())
                        || Objects.equals(user, data.getMobileNumber())
                    ) {
                        return this.memberService
                                .update(Mapper.toServiceModel(data));
                    }
                        return Mono.create(e -> ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED)
                            .body("{\"status\":\"unauthorized\"}"));
                })
                .flatMap(m -> m)
                .switchIfEmpty(
                    Mono.create(e -> ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("{\"status\":\"account already registered\"}"))
                )
                .map(ResponseEntity::ok);
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Object>> register(
        @RequestBody RegisterViewModel data
    ) {
        var violations = this.validator.validate(data);
        if(!violations.isEmpty()) {
            return Mono.create(e -> ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                        violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.toList())
                    )
            );
        }

        return this.memberService
            .register(Mapper.toRegisterServiceModel(data))
            .switchIfEmpty(
                Mono.create(e -> ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("{\"status\":\"account already registered\"}"))
            )
            .map(ResponseEntity::ok);
    }

    private static class Mapper {

        private static MemberViewModel fromServiceModel(Member member) {
            return MemberViewModel.builder()
                    .id(member.getId().toString())
                    .mobileNumber(member.getMobileNumber())
                    .email(member.getEmail())
                    .firstName(member.getFirstName())
                    .lastName(member.getLastName())
                    .gender(member.getGender().name())
                    .dateOfBirth(member.getDateOfBirth())
                    .build();
        }

        private static Member toServiceModel(MemberViewModel member) {
            return Member.builder()
                    .id(UUID.randomUUID())
                    .mobileNumber(member.getMobileNumber())
                    .email(member.getEmail())
                    .firstName(member.getFirstName())
                    .lastName(member.getLastName())
                    .gender(Gender.valueOf(member.getGender()))
                    .dateOfBirth(member.getDateOfBirth())
                    .build();
        }

        private static Member toRegisterServiceModel(RegisterViewModel member) {
            return Member.builder()
                    .id(UUID.randomUUID())
                    .mobileNumber(member.getMobileNumber())
                    .email(member.getEmail())
                    .password(member.getPassword())
                    .firstName(member.getFirstName())
                    .lastName(member.getLastName())
                    .gender(Gender.valueOf(member.getGender()))
                    .dateOfBirth(member.getDateOfBirth())
                    .build();
        }
    }
}

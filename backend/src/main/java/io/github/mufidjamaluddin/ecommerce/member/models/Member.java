package io.github.mufidjamaluddin.ecommerce.member.models;

import io.github.mufidjamaluddin.ecommerce.shared.Gender;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("Member")
public class Member implements UserDetails, CredentialsContainer {

    @Id
    @Column("id")
    private UUID id;

    @Column("mobile_number")
    private String mobileNumber;

    @Column("email")
    private String email;

    @Column("password")
    private String password;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("gender")
    private Gender gender;

    @Column("date_of_birth")
    private Date dateOfBirth;

    @Column("roles")
    private String roles;

    @Override
    public String toString() {
        return String.format(
                "Member(id='%s', mobileNumber='%s', email='%s')",
                this.getId(), this.getMobileNumber(), this.getEmail());
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    public String getRoles() {
        if(StringUtil.isNullOrEmpty(roles)) {
            return "";
        }
        return roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(this.getRoles().split(","))
                .map(Role::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.getMobileNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

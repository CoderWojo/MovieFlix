package com.movieflix.auth.entities;

import com.movieflix.entities.ForgotPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "The username field cannot be blank!")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "The email field cannot be blank!")
    @Email(message = "Please enter email in proper format!")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "The password field cannot be blank!")
    @Size(min = 5, message = "The password must have at least 5 characters!")
    private String password;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<RefreshToken> refreshTokens;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole role;

    private boolean isEnabled = true;

    private boolean isCredentialsNonExpired = true;

    private boolean isAccountNonLocked = true;

    private boolean isAccountNonExpired = true;

    @OneToOne(mappedBy = "user")
    private ForgotPassword forgotPassword;

//    Spring Security używa tej metody do sprawdzenia @PreAuthorized()
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override   // unique email
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }
    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }
    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}

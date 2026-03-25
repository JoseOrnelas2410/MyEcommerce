package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)//Genera una sola tabla para User ya sean Customer o Admin
@DiscriminatorColumn(name = "user_type_id", discriminatorType = DiscriminatorType.INTEGER)//Aqui radica el cambio entre Customer y Admin
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "user_id", updatable = false)
    private Long userId;

    @Column(name = "password")
    private String password;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_firstname")
    private String firstName;

    @Column(name = "user_email", unique = true)//Anotacion unique true evita que dos users tengan el mismo correo
    private String email;

    @Column(name = "user_phone", unique = true)
    private long phone;

    @Column(name = "user_address")
    private String userAddress;

    protected User(
            String password,
            String name,
            String firstName,
            String email,
            long phone,
            String address
    ){
        this.password = password;
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.phone = phone;
        this.userAddress = address;
    }

    /**
     *La interfaz UserDetails tiene como metodos abstractos, por lo cual se deben sobreescribir
     * java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities();
     * java.lang.@org.jspecify.annotations.Nullable String getPassword() {}
     * java.lang.String getUsername() {}
     * default boolean isAccountNonExpired() {}
     * default boolean isAccountNonLocked() {}
     * default boolean isCredentialsNonExpired() {}
     * default boolean isEnabled() {}
     */

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities(){
        if(this instanceof Admin) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else return List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    }

    @Override
    @NonNull
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }
}

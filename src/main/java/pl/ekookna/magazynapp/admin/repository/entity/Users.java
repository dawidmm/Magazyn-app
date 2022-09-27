package pl.ekookna.magazynapp.admin.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.ekookna.magazynapp.warehouse.repository.entity.Warehouse;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Users implements UserDetails {

    @Id
    @SequenceGenerator(name = "users_id_sequence",
            sequenceName = "users_id_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "users_id_sequence")
    @Column(updatable = false)
    private Long id;

    @Column(unique = true)
    private String login;

    private String password;

    private String role;

    private Boolean active;

    @JsonIgnore
    @ManyToMany()
    @JoinTable(
            name = "warehouse_users_ids",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "warehouse_id")}
    )
    private Set<Warehouse> warehouses = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role);
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}

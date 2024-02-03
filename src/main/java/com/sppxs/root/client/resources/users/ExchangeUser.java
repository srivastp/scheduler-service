package com.sppxs.root.client.resources.users;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sppxs.root.client.resources.jobs.MyAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ExchangeUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;

    //https://stackoverflow.com/questions/47693110/could-not-write-json-infinite-recursion-stackoverflowerror-nested-exception
    @JsonManagedReference
    @JoinTable(name = "exchange_user_action",
            joinColumns = @JoinColumn(name = "exchange_user_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id"))
    @ManyToMany
    private List<MyAction> myActions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<MyAction> getMyActions() {
        return myActions;
    }

    public void setMyActions(List<MyAction> myActions) {
        this.myActions = myActions;
    }
}

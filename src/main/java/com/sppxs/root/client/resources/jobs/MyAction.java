package com.sppxs.root.client.resources.jobs;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sppxs.root.client.resources.users.ExchangeUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MyAction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String category;
    @JsonIgnore
    private String frequency = "0/50 * * * *"; //"${every-50-sec-cron}"

    //https://stackoverflow.com/questions/47693110/could-not-write-json-infinite-recursion-stackoverflowerror-nested-exception
    @JsonBackReference
    @ManyToMany(mappedBy = "myActions", fetch = FetchType.EAGER)
    private List<ExchangeUser> exchangeUsers = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public List<ExchangeUser> getExchangeUsers() {
        return exchangeUsers;
    }

    public void setExchangeUsers(List<ExchangeUser> exchangeUsers) {
        this.exchangeUsers = exchangeUsers;
    }
}

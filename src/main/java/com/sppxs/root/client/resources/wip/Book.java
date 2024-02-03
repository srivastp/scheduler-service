package com.sppxs.root.client.resources.wip;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity(name = "Book")
@DiscriminatorValue("Book")
@NoArgsConstructor
@AllArgsConstructor
public class Book extends Publication {
    @Column
    private int pages;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
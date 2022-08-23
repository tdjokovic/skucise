package com.example.skucise.models;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class Comment {
    @Min(1)
    @Max(Integer.MAX_VALUE)
    private int id;

    @Min(0)
    @Max(Integer.MAX_VALUE)
    private int parentId;

    private String authorName;

    @NotBlank
    @Size(min = 10, max = 1000, message = "The length of comment must be between 10 and 1000 characters")
    private String text;

    @PastOrPresent
    private LocalDateTime postDate;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getParentId()
    {
        return parentId;
    }

    public void setParentId(int parentId)
    {
        this.parentId = parentId;
    }

    public String getAuthorName()
    {
        return authorName;
    }

    public void setAuthorName(String authorName)
    {
        this.authorName = authorName;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public LocalDateTime getPostDate()
    {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate)
    {
        this.postDate = postDate;
    }
}

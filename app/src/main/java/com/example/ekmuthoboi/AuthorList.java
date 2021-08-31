package com.example.ekmuthoboi;

public class AuthorList {
    private String authorName,sub_catId;

    public AuthorList(String authorName, String sub_catId) {
        this.authorName = authorName;
        this.sub_catId = sub_catId;
    }


    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getSub_catId() {
        return sub_catId;
    }

    public void setSub_catId(String sub_catId) {
        this.sub_catId = sub_catId;
    }
}

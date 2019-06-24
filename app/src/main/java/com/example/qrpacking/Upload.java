package com.example.qrpacking;

public class Upload {

    private String id;
    private String name;
    private String imageUrl;


    public Upload(){
        //empty constructor needed
    }

    public Upload(String id, String name, String imageUrl){
        if(name.trim().equals("")){
            name = "No Name";
        }
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isSelected(){
        return
    }
}

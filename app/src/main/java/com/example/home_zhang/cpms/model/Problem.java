package com.example.home_zhang.cpms.model;

/**
 * Created by czhang on 3/5/2017.
 */

public class Problem {
    private String id;
    private String no;
    private String title;
    private String difficultyLevel;
    private String description;
    private String tags;
    private String companies;
    private String specialTags;

    public Problem(String no, String title, String difficultyLevel, String topics, String id) {
        this.no = no;
        this.title = title;
        this.difficultyLevel = difficultyLevel;
        this.tags = topics;
        this.id = id;
    }

    public Problem(String no, String title, String difficultyLevel, String tags, String companies, String specialTags) {
        this.no = no;
        this.title = title;
        this.difficultyLevel = difficultyLevel;
        String[] tagsIndexes = tags.split(",");
        StringBuilder tagString = new StringBuilder();
        String prefix = "";
        for(String tagInd : tagsIndexes){
            if(tagInd.length() == 0) continue;
            try {
                int ind = Integer.parseInt(tagInd.trim());
                if(ind < Constants.tags.length){
                    tagString.append(prefix);
                    prefix = ", ";
                    tagString.append(Constants.tags[ind]);
                }
            } catch (NumberFormatException e) {
                System.out.println(e);
            }
        }
        this.tags = tagString.toString();
        this.companies = companies;
        this.specialTags = specialTags;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCompanies() {
        return companies;
    }

    public void setCompanies(String companies) {
        this.companies = companies;
    }

    public String getSpecialTags() {
        return specialTags;
    }

    public void setSpecialTags(String specialTags) {
        this.specialTags = specialTags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package com.li.penguin.display;

public class PenguinBean {
    private String name;
    private String band_color1;
    private String band_color2;
    private String penguinid;
    private String gender;
    private Integer hatch_year;
    private String fun_fact;
    private String personality;
    private String species;
    private String appendix;

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPenguinId() {
        return penguinid;
    }

    public void setId(String penguinid) {
        this.penguinid = penguinid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getHatch_year() {
        return hatch_year;
    }

    public void setHatch_year(Integer hatch_year) {
        this.hatch_year = hatch_year;
    }

    public String getFun_fact() {
        return fun_fact;
    }

    public void setFun_fact(String fun_fact) {
        this.fun_fact = fun_fact;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {

        this.personality = personality;
    }

    public String getAppendix() {
        return appendix;
    }

    public void setAppendix(String appendix) {
        this.appendix = appendix;
    }

    public String getBand_color1() {
        return band_color1;
    }

    public void setBand_color1(String band_color1) {
        this.band_color1 = band_color1;
    }

    public String getBand_color2() {
        return band_color2;
    }

    public void setBand_color2(String band_color2) {
        this.band_color2 = band_color2;
    }

}

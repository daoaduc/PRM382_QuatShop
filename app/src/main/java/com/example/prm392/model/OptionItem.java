package com.example.prm392.model;

public class OptionItem {
    private String optionName;
    private int optionIcon; // Resource ID for icon

    public OptionItem(String optionName, int optionIcon) {
        this.optionName = optionName;
        this.optionIcon = optionIcon;
    }

    public String getOptionName() {
        return optionName;
    }

    public int getOptionIcon() {
        return optionIcon;
    }
}


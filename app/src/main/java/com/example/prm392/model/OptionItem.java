package com.example.prm392.model;

public class OptionItem {
    private String optionName;
    private int optionIcon;
    private String action;

    public OptionItem(String optionName, int optionIcon, String action) {
        this.optionName = optionName;
        this.optionIcon = optionIcon;
        this.action = action;
    }
    public String getOptionName() {
        return optionName;
    }

    public int getOptionIcon() {
        return optionIcon;
    }

    public String getAction() { return action; }
}


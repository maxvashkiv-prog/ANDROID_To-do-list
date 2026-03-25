package com.example.to_dolist;

public class ChecklistItem {
    public String text;
    public boolean checked;

    public ChecklistItem(String text) {
        this.text = text;
        this.checked = false;
    }
}
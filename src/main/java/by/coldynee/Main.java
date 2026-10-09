package by.coldynee;

import by.coldynee.io.loaders.TxtFileInput;

public class Main {
    public static void main(String[] args) {
        TxtFileInput txtFileInput = new TxtFileInput("");
        txtFileInput.load(1);
        System.out.println(1);
    }
}
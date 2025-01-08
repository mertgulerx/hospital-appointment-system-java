package mertguler;


import javafx.application.Application;
import mertguler.CRS.CRS;
import mertguler.GUI.Gui;
import mertguler.TextUI.TextUI;

import java.util.Scanner;


public class Main {
    public static boolean gui_mode = false;

    public static void main(String[] args) {
        // launch();
        Scanner scanner = new Scanner(System.in);

        System.out.println("GUI or Text User Interface? (1/0)");

        try {
            int input = Integer.valueOf(scanner.nextLine());
            if (input == 1) {
                gui_mode = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gui_mode) {
            Application.launch(Gui.class);
        } else {
            CRS crs = new CRS();
            TextUI textUI = new TextUI(scanner, crs);
            textUI.start();
        }


    }
}

package mertguler;


import mertguler.CRS.CRS;
import mertguler.TextUI.TextUI;

import java.util.Scanner;


public class Main {
    public static boolean gui_mode = false;

    public static void main(String[] args) {
        // launch();
        Scanner scanner = new Scanner(System.in);

        CRS crs = new CRS();
        System.out.println("GUI or Text User Interface? (1/0)");

        try {
            int input = Integer.valueOf(scanner.nextLine());
            if (input == 1) {
                gui_mode = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!gui_mode) {
            TextUI textUI = new TextUI(scanner, crs);
            textUI.start();
        }


    }
}

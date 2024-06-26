package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class GUI_projekt extends JFrame {
    private JButton dodajButton;
    private JButton wyswietlButton;
    private JButton usunButton;
    private JTextArea textArea1;
    private JPanel mainPanel;
    private JTextField textField_tytul;
    private JTextField textField_gatunek;
    private JTextField textField_dataPremiery;
    private JTextField textField_Rate;
    private JTextField textField_metacritic;
    private JTextField textField_opis;
    private JTextField textFieldiD_usun;
    private JLabel JLabel_bledy;

    public GUI_projekt() {
        setContentPane(mainPanel);
        setTitle("Problem plecakowy");
        setSize(800, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        wyswietlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Gra> gry = Gra.getAllGames();
                textArea1.setText("");
                for (Gra gra : gry) {
                    textArea1.append("Id: " + gra.id + "\n");
                    textArea1.append("Tytuł: " + gra.Tytul + "\n");
                    textArea1.append("Gatunek: " + gra.Gatunek + "\n");
                    textArea1.append("Data Premiery: " + gra.DataPremiery + "\n");
                    textArea1.append("Metacritic: " + gra.Metacritic + "\n");
                    textArea1.append("Opis: " + gra.Opis.orElse(null) + "\n");
                    // textArea1.append("Image URL: " + gra.ImageUrl.orElse(null) + "\n");
                    textArea1.append("Average Rate: " + gra.Rate + "\n");
                    textArea1.append("-------------------------------\n");
                }
            }
        });

        dodajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pobierz dane z pól JTextField
                String tytul = textField_tytul.getText();
                String gatunek = textField_gatunek.getText();
                String dataPremieryText = textField_dataPremiery.getText();
                String metacriticText = textField_metacritic.getText();
                String rateText = textField_Rate.getText();
                String opis = textField_opis.getText();

                // Sprawdź, czy wszystkie obowiązkowe dane zostały podane
                if (tytul.isEmpty() || gatunek.isEmpty() || dataPremieryText.isEmpty() || metacriticText.isEmpty() || rateText.isEmpty()) {
                    JLabel_bledy.setText("Proszę wypełnić wszystkie obowiązkowe pola.");
                    return;
                }

                Date dataPremiery = null;
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dataPremiery = dateFormat.parse(dataPremieryText);
                } catch (ParseException ex) {
                    JLabel_bledy.setText("Nieprawidłowy format daty. Użyj formatu yyyy-MM-dd.");
                    return;
                }

                float metacritic;
                float rate;
                try {
                    metacritic = Float.parseFloat(metacriticText);
                    rate = Float.parseFloat(rateText);
                } catch (NumberFormatException ex) {
                    JLabel_bledy.setText("Ocena Metacritic i Ocena gry muszą być liczbami.");
                    return;
                }

                Gra nowaGra = new Gra(0, tytul, gatunek, dataPremiery, metacritic, Optional.of(opis), Optional.empty(), rate);

                nowaGra.addToDatabase();

                ArrayList<Gra> gry = Gra.getAllGames();
                textArea1.setText("");
                for (Gra gra : gry) {
                    textArea1.append("Id: " + gra.id + "\n");
                    textArea1.append("Tytuł: " + gra.Tytul + "\n");
                    textArea1.append("Gatunek: " + gra.Gatunek + "\n");
                    textArea1.append("Data Premiery: " + gra.DataPremiery + "\n");
                    textArea1.append("Metacritic: " + gra.Metacritic + "\n");
                    textArea1.append("Opis: " + gra.Opis.orElse(null) + "\n");
                    // textArea1.append("Image URL: " + gra.ImageUrl.orElse(null) + "\n");
                    textArea1.append("Average Rate: " + gra.Rate + "\n");
                    textArea1.append("-------------------------------\n");
                }
                JLabel_bledy.setText("");
            }
        });

        usunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = textFieldiD_usun.getText();
                if (idText.isEmpty()) {
                    JLabel_bledy.setText("Proszę podać ID gry do usunięcia.");
                } else {
                    int ID = Integer.parseInt(idText);
                    // Sprawdź, czy gra o podanym ID istnieje
                    Gra graDoUsuniecia = getGameById(ID);
                    if (graDoUsuniecia == null) {
                        JLabel_bledy.setText("Gra o podanym ID nie istnieje.");
                    } else {
                        // Usuń grę z bazy danych
                        Gra.deleteGameById(ID);

                        // Aktualizuj widok, aby pokazać, że rekordy zostały usunięte
                        textArea1.setText("Wybrany rekord został usunięty.");

                        ArrayList<Gra> gry = Gra.getAllGames();
                        textArea1.setText("");
                        for (Gra gra : gry) {
                            textArea1.append("Id: " + gra.id + "\n");
                            textArea1.append("Tytuł: " + gra.Tytul + "\n");
                            textArea1.append("Gatunek: " + gra.Gatunek + "\n");
                            textArea1.append("Data Premiery: " + gra.DataPremiery + "\n");
                            textArea1.append("Metacritic: " + gra.Metacritic + "\n");
                            textArea1.append("Opis: " + gra.Opis.orElse(null) + "\n");
                            // textArea1.append("Image URL: " + gra.ImageUrl.orElse(null) + "\n");
                            textArea1.append("Average Rate: " + gra.Rate + "\n");
                            textArea1.append("-------------------------------\n");
                        }
                        JLabel_bledy.setText("");
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        GUI_projekt myFrame = new GUI_projekt();
    }

    private Gra getGameById(int id) {
        ArrayList<Gra> gry = Gra.getAllGames();
        for (Gra gra : gry) {
            if (gra.id == id) {
                return gra;
            }
        }
        return null;
    }
}

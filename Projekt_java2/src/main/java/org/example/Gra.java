package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class Gra {
    public int id;
    public String Tytul;
    public String Gatunek;
    public Date DataPremiery;
    public Float Metacritic;
    public Optional<String> Opis;
    public Optional<String> ImageUrl;
    public double Rate;


    public Gra(int id, String Tytul, String Gatunek, Date DataPremiery, Float Metacritic, Optional<String> Opis, Optional<String> ImageUrl, double Rate) {
        this.id = id;
        this.Tytul = Tytul;
        this.Gatunek = Gatunek;
        this.DataPremiery = DataPremiery;
        this.Metacritic = Metacritic;
        this.Opis = Opis;
        this.ImageUrl = ImageUrl;
        this.Rate = Rate;
    }

    public void addToDatabase() {
        String url = "jdbc:sqlite:games.db";
        String sql = "INSERT INTO games(tytul, gatunek, dataPremiery, metacritic, opis, imageUrl, Rate) VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Tytul);
            pstmt.setString(2, Gatunek);

            if (DataPremiery != null) {
                pstmt.setDate(3, new java.sql.Date(DataPremiery.getTime()));
            } else {
                pstmt.setNull(3, java.sql.Types.DATE);
            }

            pstmt.setFloat(4, Metacritic);

            pstmt.setString(5, Opis.orElse(null));
            pstmt.setString(6, ImageUrl.orElse(null));
            pstmt.setDouble(7, Rate);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<Gra> getAllGames() {
        String url = "jdbc:sqlite:games.db";
        String sql = "SELECT * FROM games";
        ArrayList<Gra> games = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String tytul = rs.getString("tytul");
                String gatunek = rs.getString("gatunek");
                Date dataPremiery = rs.getDate("dataPremiery");
                Float metacritic = rs.getFloat("metacritic");
                Optional<String> opis = Optional.ofNullable(rs.getString("opis"));
                Optional<String> imageUrl = Optional.ofNullable(rs.getString("imageUrl"));
                double Rate = rs.getDouble("Rate");

                Gra gra = new Gra(id, tytul, gatunek, dataPremiery, metacritic, opis, imageUrl, Rate);
                games.add(gra);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return games;
    }

    public static void deleteAllGames() {
        String url = "jdbc:sqlite:games.db";
        String sql = "DELETE FROM games";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteGameById(int gameId) {
        String url = "jdbc:sqlite:games.db";
        String sql = "DELETE FROM games WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gameId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

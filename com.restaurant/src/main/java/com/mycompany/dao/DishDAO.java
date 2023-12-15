/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dao;

import com.mycompany.model.Dish;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class DishDAO {
    public DishDAO(){
        super();
    }
    public static List<Dish> getAll() {
        List<Dish> dishs = new ArrayList<Dish>();
        try {

            Connection connect = JDBCConnection.getJDBCConnection();
            String sql = "select * from dish";
            Statement statment = connect.createStatement();
            ResultSet rs = statment.executeQuery(sql);
            while (rs.next()) {
                Dish dish = new Dish();
                dish.setId(rs.getInt("id"));
                dish.setName(rs.getString("name"));
                dish.setPrice(rs.getBigDecimal("price"));
                dish.setCategory(rs.getString("category"));
                Blob blob = rs.getBlob("image");
                if (blob != null) {
                    int blobLength = (int) blob.length();
                    byte[] imageBytes = blob.getBytes(1, blobLength);
                    dish.setImage(imageBytes);
                }
                dishs.add(dish);
            }
            return dishs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishs;
    }

    public static void create(Dish dish) {
        try {
            Connection connect = JDBCConnection.getJDBCConnection();
            String sql = "INSERT INTO dish (name, price, category ,image) VALUES (?, ?, ?, ?);";
            PreparedStatement preparedStatment = connect.prepareStatement(sql);
            preparedStatment.setString(1, dish.getName());
            preparedStatment.setBigDecimal(2, dish.getPrice());
            preparedStatment.setString(3, dish.getCategory());
            if (dish.getImage() != null) {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(dish.getImage());
                preparedStatment.setBinaryStream(4, imageStream);

            } else {
                System.out.print("Image is null");
                preparedStatment.setNull(4, Types.BLOB);
            }
            int rs = preparedStatment.executeUpdate();
            System.out.print(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
      public static void delete(int id){
         try {
            Connection connect = JDBCConnection.getJDBCConnection();
            String sql = "delete from dish where id = ?";
            PreparedStatement preparedStatment = connect.prepareStatement(sql);
            preparedStatment.setInt(1, id);
             preparedStatment.executeUpdate();
            System.out.println("Delete dish " + id);

          
   
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Dish getById(int id){
        
        Dish dish = null;
        try {

            Connection connect = JDBCConnection.getJDBCConnection();
            String sql = "select * from dish where id = ?";
            PreparedStatement preparedStatment = connect.prepareStatement(sql);
            preparedStatment.setInt(1,id);
            ResultSet rs = preparedStatment.executeQuery();
            while (rs.next()) {
                dish = new Dish();
                dish.setId(rs.getInt("id"));
                dish.setName(rs.getString("name"));
                dish.setPrice(rs.getBigDecimal("price"));
                dish.setCategory(rs.getString("category"));
                Blob blob = rs.getBlob("image");
                if (blob != null) {
                    int blobLength = (int) blob.length();
                    byte[] imageBytes = blob.getBytes(1, blobLength);
                    dish.setImage(imageBytes);
                }
                
            }
            return dish;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dish;
    }
    public static void update(Dish dish){

        try {

            Connection connect = JDBCConnection.getJDBCConnection();
            String sql = "update dish set name = ?, price = ?, category = ? ,image = ? where id = ?";
            PreparedStatement preparedStatment = connect.prepareStatement(sql);
           
            preparedStatment.setString(1, dish.getName());
            preparedStatment.setBigDecimal(2, dish.getPrice());
            preparedStatment.setString(3, dish.getCategory());
            if (dish.getImage() != null) {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(dish.getImage());
                preparedStatment.setBinaryStream(4, imageStream);

            } else {
                System.out.print("Image is null");
                preparedStatment.setNull(4, Types.BLOB);
            }
            preparedStatment.setInt(5,dish.getId());
            System.out.println(dish.toString());
            preparedStatment.executeUpdate();
            
   
        } catch (SQLException e) {
            e.printStackTrace();
        }
  
    }
}

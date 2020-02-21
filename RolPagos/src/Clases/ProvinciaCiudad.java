/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author Fabricio Mero
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import Clases.Provincia;
import Clases.ConectorDB;
import java.util.ArrayList;


public class ProvinciaCiudad {
    
    public Provincia[] provincias = new Provincia[24];
    ArrayList<Ciudad> ciudades = new ArrayList();
    
    public ProvinciaCiudad(){
        cargarProvincias();
    }
    
    private void cargarProvincias(){
        
        // Obtenemos data de DB
        ConectorDB conexion = new ConectorDB();
        
        //Consulta
        String query = "SELECT * FROM Provincia";
        
        ResultSet rs = conexion.consultar(query);
        
        if (rs == null){
            System.out.println("error en cargar cmbprovincia");
            return;
        }
        
        int i = 0;
        
        try{
            while(rs.next()){
                // anadimos cada provincia y su id a la lista.
                this.provincias[i] = new Provincia(rs.getInt("Id_Provincia"),rs.getString("Provincia"));
                i++;
            }
        }
        catch( SQLException ex)
        {
            System.out.println("error en cargar cmbprovincia");
            return;
        }
    }
    
    // Agrega items a comboBox.
    public void cargarComboProvincias(JComboBox cmbBox){
        
        for (int i = 0; i < this.provincias.length; i ++){
            cmbBox.addItem(provincias[i].nombre);
        }
    }
    
    
    public void cargarCiudades(String provincia){
        
    }
    
    public void cargarComboCiudades(JComboBox cmbBox, String provincia){
        int indiceProvincia = 0;
        
        // Si el item seleccionado es "SELECCIONE" limpie combo Ciudades y salga.
        if (provincia == "SELECCIONE"){
            cmbBox.removeAllItems();
            return;
        }
        
        for (int i = 0; i < this.provincias.length; i ++){
            if (provincia == this.provincias[i].nombre)
            {
                indiceProvincia = this.provincias[i].id;
            }
        }
        
        // Consultamos que ciudad segun provincia.
        ConectorDB con = new ConectorDB();
        
        //Consulta
        String query = "SELECT * FROM Ciudad where Id_Provincia=" + String.valueOf(indiceProvincia);
        
        ResultSet rs = con.consultar(query);
        
        if (rs == null){
            System.out.println("error en cargar cmbprovincia");
            return;
        }
        
        // Vaciamos lista de ciudades.
        this.ciudades.clear();
        
        // Vaciamos comboBox.
        cmbBox.removeAllItems();
        
        // Anadimos primera opcion a combo
        cmbBox.addItem("SELECCIONE");
        
        try{
            while(rs.next()){
                // anadimos cada provincia y su id a la lista.
                this.ciudades.add(new Ciudad(rs.getInt("Id_Ciudad"),rs.getString("Ciudad")));
                // anadimos items a combo.
                cmbBox.addItem(rs.getString("Ciudad"));
            }
        }
        catch( SQLException ex)
        {
            System.out.println("error en cargar cmbprovincia");
            return;
        }
    } 
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import javax.swing.JTextField;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author Fabricio Mero
 */
public class LimpiadorAsiris {
        
        // Evalua largo de string cada vez que usuario ingresa un caracter.
        // Si se pasa del limite definido, se hecha a perder ese evento 
        public void limitarLargo(java.awt.event.KeyEvent evt, JTextField txtBox,  int n){
            if(txtBox.getText().length()>=n) {
                evt.consume();
            }
        }
        
        public void soloEnteros(java.awt.event.KeyEvent evt, JTextField txtBox, int largo){
            try{
                // probamos si es numero. Si no es, dispara excepcion.
                Long.parseLong(txtBox.getText());
                
                // verificamos largo.
                if(txtBox.getText().length()>=largo) {
                    evt.consume();
                }
                
            }
            catch(NumberFormatException ex) 
            {
                evt.consume();
            }
        }
        
        public boolean soloEnteros(String txtBox){
            // Texto ingresado.
            String texto = txtBox;
            
            try{
                Long.parseLong(txtBox);
                return true;
            }
            catch(NumberFormatException ex){
                return false;
            }
        }
        
        public boolean soloDecimales(String txtBox){
            // Valor ingresado.
            String texto = txtBox;
            
            try{
                // probamos si es numero. Si no es, dispara excepcion.
                Double.parseDouble(texto);
                return true;
            }
            catch(NumberFormatException ex) 
            {
                return false;
            }
        }
        
        // Valida formato de fecha valido.
        public boolean fecha(String txt){
            try{
                Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(txt);
                return true;
            }
            catch(Exception ex){
                return false;
            }
        }
        
        public void soloLetras(java.awt.event.KeyEvent evt, JTextField txtb, int largo){
            String texto = txtb.getText();
            char caract = evt.getKeyChar();
            
            if (!Character.isLetter(caract) || texto.length() >= largo){
                evt.consume();
            }
        }
        
        public void soloLetrasEspacios(java.awt.event.KeyEvent evt, JTextField txtb, int largo){
            String texto = txtb.getText();
            char caract = evt.getKeyChar();
            
            if ((!Character.isLetter(caract) && !Character.isSpaceChar(caract)) || texto.length() >= largo){
                evt.consume();
            }
        }
        
        public void soloNumeros(java.awt.event.KeyEvent evt, JTextField txtb, int largo){
            String texto = txtb.getText();
            char caract = evt.getKeyChar();
            
            if (!Character.isDigit(caract) || texto.length() >= largo){
                evt.consume();
            }
        }
        
        public void NumerosLetrasEspacios(java.awt.event.KeyEvent evt, JTextField txtb, int largo){
            String texto = txtb.getText();
            char caract = evt.getKeyChar();
            
            if ((!Character.isDigit(caract) && !Character.isLetter(caract) && !Character.isSpaceChar(caract)) || texto.length() >= largo){
                evt.consume();
            }
        }
        
        public boolean ValidarCedula(String txtBox){
            
            Cedula cedula = new Cedula();
            String valorIngresado = txtBox;
            boolean cedulaValida = false;
            
            try{
                // probamos si es numero. Si no es, dispara excepcion. Tambien verifica que sean 10 caracteres.
                Integer.parseInt(txtBox);
                
                if(valorIngresado.length() != 10){
                    throw new NumberFormatException();
                }
                
                // validamos cedula
                cedulaValida = cedula.validacion_cedula(valorIngresado);
                
                // Si es valida, se retorna true.
                return cedulaValida;
            }
            catch(NumberFormatException ex) 
            {
                return cedulaValida;
            }
        }
}

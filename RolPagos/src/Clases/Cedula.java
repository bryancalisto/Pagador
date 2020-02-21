/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author Daro
 */
public class Cedula {

    /**
     * @param ced cedula 
     * @return 
     */
    public boolean validacion_cedula(String ced) {
   {
        
        //String ced = JOptionPane.showInputDialog(null, "Digite Numero de cedula");
        
         boolean confirmacion = false;
         int digito_cedula = 0, suma = 0, validar; 
         int digito_verificador = Integer.parseInt(ced.substring(9,10));
         int provincia = Integer.parseInt(ced.substring(0,2));
         int tercer_digito = Integer.parseInt(ced.substring(2,3));
        
//sumade digitos 
         for (int i = 0; i < ced.length() - 1; i++) {
             
             digito_cedula = Integer.parseInt(ced.substring(i, i+1));
             
             if (i % 2 == 0){
              
                 digito_cedula= digito_cedula *2;
             }
             if(digito_cedula >9){
                 
                 digito_cedula = digito_cedula-9;
             }
             suma += digito_cedula;
            }

         validar = (suma-(suma%10)+10)-suma;
         
         //validacion en el caso que ultimo digito sea 0
         if(digito_verificador == 0){
             if(suma % 10 == 0 && suma % 10 == digito_verificador && tercer_digito <= 6 && provincia <= 24){
                 confirmacion = true;
                 return confirmacion;
                 //JOptionPane.showMessageDialog(null, "Cedula Correcta");
             }
             else
            {
                return confirmacion;
                //JOptionPane.showMessageDialog(null, "Numero de cedula erroneo");
             }
         }
         
         // sentencia para validacion 
         if(validar == digito_verificador && tercer_digito <= 6 && provincia <= 24 ){
             
             confirmacion = true;
             return confirmacion;
             
             //JOptionPane.showMessageDialog(null, "Cedula Correcta");
         }
         else
         {
             return confirmacion;
             //JOptionPane.showMessageDialog(null, "Numero de cedula erroneo");
         }
        }
    }
}

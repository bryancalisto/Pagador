/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

/**
 *
 * @author Fabricio Mero
 */


public class RolDePagos {
    
    // Inicializador.
    public RolDePagos(){
        this.identificacion = identificacion;
        //this.obtenerDatosUsuario();
    }
    
    public String identificacion;
    public String nombres;
    public String apellidos;
    public double sueldo;
    public int horasExtra100;
    public int horasExtra50;
    public double totalHorasExtra100;
    public double totalHorasExtra50;
    public boolean decimoTerceroMensual;
    public boolean decimoCuartoMensual;
    public boolean fondoReservaMensual;
    public double DecimoTercero;
    public double DecimoCuarto;
    public double FondoReserva;
    public double multas;
    public double comisiones;
    public double anticipos;
    public double nMeses; // Numero de meses que esta el empleado en la empresa.
    public boolean estado; // Usuario esta activo o se ha eliminado logicamente.
    public double iess;
    public double impuestoRenta;
    public double totalIngreso;
    public double totalDescuento;
    public double totalRecibir;
    public String fechaDeGeneracion;
    public ConectorDB con = new ConectorDB();
    
    
    // toma un numero de cedula y con tal, define la cedula de esta clase.
    public void definirIndentificacion(String identificacion){
        this.identificacion = identificacion;
    }
    
    // Verifica que no se haya registrado ya el rol de pagos del mes y persona seleccionados.
    public boolean verificarRepetidos(){
        String query = "SELECT cedula from RolDePagosMensual where cedula = '" + identificacion +
                "' AND Month(Mes) = Month('" + fechaDeGeneracion + "') AND YEAR(Mes) = YEAR(Convert(Date,'" + 
                fechaDeGeneracion + "'))" ;
        
        ResultSet rs = null;

        rs = con.consultar(query);

        if (rs == null){
            System.out.println("error al consultar sueldo.");
            return false;
        }
        
        try{
            if(rs.next()){
                return true; // Registro repetido.
            }
            return false;
        }
        catch(Exception ex){
            return true;
        }
    }
    
    
    //Obtenemos sueldo y si mensualiza o no los bonos de ley.
    public double obtenerDatosUsuario(){
        String query = "SELECT Apellidos,Nombres,Sueldo,DecimoTerceroMensualiz,"
                + " DecimoCuartoMensualiz, MesesEnEmpresa,"
                + "FondosReservaMensualiz FROM Trabajador where Identificacion =" + identificacion;
        
        ResultSet rs = null;

        rs = con.consultar(query);

        if (rs == null){
            System.out.println("error al consultar sueldo.");
            return 0;
        }

        try{
            if(rs.next()){
                nombres = rs.getString("Nombres");
                apellidos = rs.getString("Apellidos");
                sueldo = rs.getDouble("Sueldo");
                decimoTerceroMensual = rs.getBoolean("DecimoTerceroMensualiz");
                decimoCuartoMensual = rs.getBoolean("DecimoTerceroMensualiz");
                fondoReservaMensual = rs.getBoolean("FondosReservaMensualiz");
                nMeses = rs.getInt("MesesEnEmpresa");
                //estado = rs.getBoolean("Estado");
                // FALTA ASIGNAR COMISIONES, ANTICIPOS, MULTAS, NHORAS50, NHORAS100.
            }
            else{
                JOptionPane.showMessageDialog(null, "No existen registros correspondientes a ese usuario");
                return 0;
            }
        }
        catch( SQLException ex)
        {
            System.out.println("error en ejecutarConsulta");
            return 0;
        }
            return 0;
    }
    
    public void calcularRolPagos(){
        obtenerDatosUsuario();
        obtenerFechaGeneracion();
        calcularAporteIess();
        calcularDTercero();
        calcularDCuarto();
        calcularFReserva();
        calcularHExtra100();
        calcularHExtra50();
        calcularTotalIngresos();
        calcularImpuestoRenta();
        calcularTotalDescuentos();
        calcularValorRecibir();
    }
    
    public boolean cargarValoresMensuales(String hExtra50, String hExtra100, String multas, String anticipos, String comisiones){
        
        try{
            this.horasExtra50 = Integer.parseInt(hExtra50.equals("")? "0": hExtra50);
            this.horasExtra100 = Integer.parseInt(hExtra100.equals("")? "0": hExtra100);
            this.multas = Double.valueOf(multas.equals("")? "0": multas);
            this.anticipos = Double.valueOf(anticipos.equals("")? "0": anticipos);
            this.comisiones = Double.valueOf(comisiones.equals("")? "0": comisiones);
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }
            
    private void obtenerFechaGeneracion(){
        // Obtenemos fecha y hora de registro.
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime fechaActual = LocalDateTime.now();
        this.fechaDeGeneracion = dtf.format(fechaActual);
    }
    
    private void calcularDTercero() {    
        double temp;
        
        if (decimoTerceroMensual){
            temp = sueldo / 12;
        }
        else{
            temp = sueldo;
        }
        
        this.DecimoTercero = temp;
    }
    
    private void calcularDCuarto() {  
        double temp;
        
        if (decimoCuartoMensual){
            temp = 33.33;  // 400 / 12 meses
        }
        else{
            temp = 400;  // S.B.U.
        }
        this.DecimoCuarto = temp;
    }
    
    private void calcularFReserva() {    
        double temp;
        
        if (nMeses > 12){      // Si ya tiene un ano en la empresa
            if (fondoReservaMensual){
                temp = (sueldo / 12);
            }
            else{
                temp = sueldo;
            }
        }
        else            // Si tiene menos de un ano.
            temp = 0;
        
        this.FondoReserva = temp;
    }
    
    private void calcularAporteIess(){
        this.iess = sueldo * 0.0945;
    }
    
    private void  calcularImpuestoRenta(){
        double temp;
        double baseGravada = (totalIngreso - iess) * 12;
        double IRSobreFraccBasica = 0;   // 
        double porcentSobreFraccExced = 0;
        double fraccionBasica = 0;
        
        if(baseGravada <= 11315){
            temp = 0;
        }
        
        if (baseGravada > 11315 && baseGravada <= 14416){
            IRSobreFraccBasica = 0;
            porcentSobreFraccExced = 0.05;
            fraccionBasica = 11315;
        }
        
        if (baseGravada > 14416 && baseGravada <= 18018){
            IRSobreFraccBasica = 155;
            porcentSobreFraccExced = 0.10;
            fraccionBasica = 14416;
        }
        
        if (baseGravada > 18018 && baseGravada <= 21639){
            IRSobreFraccBasica = 515;
            porcentSobreFraccExced = 0.12;
            fraccionBasica = 18018;
        }
        
        if (baseGravada > 21639 && baseGravada <= 43268){
            IRSobreFraccBasica = 950;
            porcentSobreFraccExced = 0.15;
            fraccionBasica = 21639;
        }
        
        if (baseGravada > 43268 && baseGravada <= 64887){
            IRSobreFraccBasica = 4194;
            porcentSobreFraccExced = 0.20;
            fraccionBasica = 43268;
        }
        
        if (baseGravada > 64887 && baseGravada <= 86516){
            IRSobreFraccBasica = 8518;
            porcentSobreFraccExced = 0.25;
            fraccionBasica = 64887;
        }
        
        if (baseGravada > 86516 && baseGravada <= 115338){
            IRSobreFraccBasica = 13925;
            porcentSobreFraccExced = 0.30;
            fraccionBasica = 86516;
        }
        
        if (baseGravada > 115338){
            IRSobreFraccBasica = 22572;
            porcentSobreFraccExced = 0.35;
            fraccionBasica = 115338;
        }
        
        this.impuestoRenta = ((baseGravada - fraccionBasica) * porcentSobreFraccExced + IRSobreFraccBasica) / 12;
    }
    
    private void calcularTotalIngresos(){
        this.totalIngreso = sueldo + this.horasExtra100 + this.horasExtra50 + comisiones;
    }
    
    private void  calcularTotalDescuentos(){
        this.totalDescuento = iess + impuestoRenta + multas + anticipos;
    }
    
    private void  calcularValorRecibir(){
        this.totalRecibir =  totalIngreso - totalDescuento;
    }
    
    private void  calcularHExtra100(){
        this.totalHorasExtra100 = sueldo / 240 * 2 * horasExtra100;  
    }
    
    private void  calcularHExtra50(){
        this.totalHorasExtra50 = sueldo / 240 * 1.5 * horasExtra50;  
    }
    
    public void debugAtributos(){
        String query = "INSERT INTO RolDePagosMensual (Sueldo, HorasExtras50, HorasExtra100," +
                        "valorHorasExtra50, valorHorasExtra100, DecimoTercero, DecimoCuarto," +
                        "FondosReserva, Iess, ImpuestoRenta, Comisiones, Multas, Anticipos," +
                        "Mes, TotalIngresos, TotalDescuento, ValorRecibir) values" +
                        "(" + sueldo + ", " + horasExtra50 + ", " + horasExtra100 + ", " +
                         totalHorasExtra50 + ", " + totalHorasExtra100 + ", " + DecimoTercero + ", " +
                        DecimoCuarto + ", " + FondoReserva + ", " + iess + ", " + impuestoRenta + ", " +
                        comisiones + ", " + multas + ", " + anticipos + ", " + fechaDeGeneracion + ", " + 
                        totalIngreso + ", " + totalDescuento + ", " + totalRecibir +")";
        return;
    }
    
    public void almacenarDB(){
        
        String loque = String.format("%.2f",sueldo);
        
        String query = "INSERT INTO RolDePagosMensual (Sueldo,cedula, HorasExtras50, HorasExtra100," +
                        "valorHorasExtra50, valorHorasExtra100, DecimoTercero, DecimoCuarto," +
                        "FondosReserva, Iess, ImpuestoRenta, Comisiones, Multas, Anticipos," +
                        "Mes, TotalIngresos, TotalDescuento, ValorRecibir) values" +
                        "(" + String.format("%.2f",sueldo).replace(',', '.') + ", '" + identificacion + "', " + horasExtra50 + 
                        ", " + horasExtra100 + ", " + String.format("%.2f",totalHorasExtra50).replace(',', '.') + ", " + 
                        String.format("%.2f",totalHorasExtra100).replace(',', '.') + ", " + String.format("%.2f", DecimoTercero).replace(',', '.') +
                        ", " +String.format("%.2f",DecimoCuarto).replace(',', '.') + ", " + String.format("%.2f",FondoReserva).replace(',', '.') +
                        ", " + String.format("%.2f",iess).replace(',', '.') + ", " + String.format("%.2f",impuestoRenta).replace(',', '.') + 
                        ", " +String.format("%.2f",comisiones).replace(',', '.') + ", " + String.format("%.2f",multas).replace(',', '.') +
                        ", " + String.format("%.2f",anticipos).replace(',', '.') + ", '" + fechaDeGeneracion +
                        "', " + String.format("%.2f",totalIngreso).replace(',', '.') + ", " + String.format("%.2f",totalDescuento).replace(',', '.')
                        + ", " + String.format("%.2f",totalRecibir).replace(',', '.') +")";
        if (con.modificar(query)){
            JOptionPane.showMessageDialog(null, "Registro exitoso");
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Hubo un error inesperado. Reinténtelo después, por favor");
        }
    }
    
    private void calcRolPagos(){
        
    }
}

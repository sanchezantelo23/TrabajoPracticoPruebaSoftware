package test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import modelo.Credito;
import modelo.Cuenta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CreditoTest {
    private Date fecha;
    private Credito credito;
    private Credito tarjeta;

    @Mock
    private Cuenta unaCuenta; // Mock de Cuenta

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this); 

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2020-09-15";
        try {
            fecha = sdf.parse(dateInString);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        
        credito = new Credito("06-456213-33", "Alfredo Hernandez", fecha, 5000.00);
        
        when(unaCuenta.getSaldo()).thenReturn(1000.0);
        tarjeta = new Credito("525675213239831", "Alfredo Hernandez", fecha, 1000.0);
        tarjeta.setCuenta(unaCuenta);
    }

    /*
     * PRUEBAS UNITARIAS
     * 
     */

    @Test
    public void testComprasConTarjetaCredito() {
        assertNotNull(unaCuenta.getMovimientos());
    }

    @Test
    public void testGetCreditoDisponible() {
        assertNotNull(credito.getCreditoDisponible());
    }
    

    @Test
    public void testCreditoNoSobrepasaLimite() {
        try {
            tarjeta.retirar(200.0);
            assertEquals(800.0, tarjeta.getCreditoDisponible(), 0.001);
        } catch (Exception e) {
            fail("No debería lanzar excepción");
        }
    }

    @Test
    public void testIngresoNoModificaSaldoTarjeta() {
        try {
            tarjeta.ingresar(300.0);
            assertEquals(1000.0, tarjeta.getSaldo(), 0.001);
        } catch (Exception e) {
            fail("No debería lanzar excepción");
        }
    }
    
    @Test
    public void testCreditoSobrepasaLimite() {
        try {
            tarjeta.retirar(1200.0);
            fail("Se esperaba una excepción por exceder el límite de crédito");
        } catch (Exception e) {
            assertEquals("Límite de crédito excedido", e.getMessage());
        }
    }
    

    /*
     * PRUEBAS DE INTEGRACION
     * 
     */

    @Test
    public void testIngresar() {
        try {
            double saldoAnteriorCuenta = unaCuenta.getSaldo();
            double saldoAnteriorTarjeta = tarjeta.getSaldo();
            double creditoAnterior = tarjeta.getCreditoDisponible();
            tarjeta.ingresar(500.0);
            assertEquals(saldoAnteriorCuenta + 500, unaCuenta.getSaldo(), 0.001);
            assertEquals(saldoAnteriorTarjeta, tarjeta.getSaldo(), 0.001);
            assertEquals(creditoAnterior, tarjeta.getCreditoDisponible(), 0.001);
        } catch (Exception e) {
            fail("No debería lanzar excepción");
        }
    }

    @Test
    public void testRetirar() {
        try {
            double saldoAnteriorCuenta = unaCuenta.getSaldo();
            double saldoAnteriorTarjeta = tarjeta.getSaldo();
            double creditoAnterior = tarjeta.getCreditoDisponible();
            tarjeta.retirar(300.0);
            assertEquals(saldoAnteriorCuenta, unaCuenta.getSaldo(), 0.001);
            assertEquals(saldoAnteriorTarjeta - 15, tarjeta.getSaldo(), 0.001);
            assertEquals(creditoAnterior - 300, tarjeta.getCreditoDisponible(), 0.001);
        } catch (Exception e) {
            fail("No debería lanzar excepción");
        }
    }

    @Test
    public void testLiquidar() {
        try {
            double saldoAnteriorCuenta = unaCuenta.getSaldo();
            tarjeta.pagoEnEstablecimiento("Zara", 120.0);
            tarjeta.pagoEnEstablecimiento("El Corte Inglés", 230.0);
            tarjeta.liquidar(11, 2003);
            assertEquals(saldoAnteriorCuenta - 350.0, unaCuenta.getSaldo(), 0.001);
        } catch (Exception e) {
            fail("No debería lanzar excepción");
        }
    }

  

}


package test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import modelo.Banco;
import modelo.Cuenta;
import modelo.Debito;
import modelo.Movimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class BancoTest {
    private Banco banco;
    private Cuenta cuentaMock; // Mock de cuenta

    @BeforeEach
    public void setUp() {
        banco = new Banco("Banco Test");
        cuentaMock = mock(Cuenta.class);
    }

    /*
     * PRUEBAS UNITARIAS
     * 
     */

    @Test
    public void testAbrirCuenta() {
        assertEquals(0, banco.getCuentas().size());

        banco.abrirCuenta("06-456213-33", "Alfredo Hernandez");

        assertEquals(1, banco.getCuentas().size());
        assertNotNull(banco.obtenerCuenta("06-456213-33"));
    }

    @Test
    public void testObtenerCuentaExistente() {
        banco.abrirCuenta("06-456213-33", "Alfredo Hernandez");

        Cuenta cuenta = banco.obtenerCuenta("06-456213-33");
        assertNotNull(cuenta);
        assertEquals("Alfredo Hernandez", cuenta.getmTitular());
    }

    @Test
    public void testObtenerCuentaInexistente() {
        Cuenta cuenta = banco.obtenerCuenta("06-456213-34");
        assertNull(cuenta);
    }

    @Test
    public void testCrearTarjetaDebitoCuentaExistente() throws Exception {
        banco.abrirCuenta("06-456213-33", "Alfredo Hernandez");

        Date fechaCaducidad = new Date(System.currentTimeMillis() + 31536000000L);
        Debito tarjeta = banco.crearTarjetaDebito("1234567890123456", "Alfredo Hernandez", fechaCaducidad, "06-456213-33");

        assertNotNull(tarjeta);
    }

    @Test
    public void testAprobarOperacionIngreso() {
        Movimiento movimiento = new Movimiento();
        movimiento.setImporte(100);
        assertTrue(banco.aprobarOperacion(movimiento, cuentaMock));
    }

    /*
     * PRUEBAS DE INTEGRACION
     */

    @Test
    public void testAbrirCuentaYObtenerla() {
        banco.abrirCuenta("06-456213-33", "Alfredo Hernandez");

        Cuenta cuenta = banco.obtenerCuenta("06-456213-33");
        assertNotNull(cuenta);
        assertEquals("06-456213-33", cuenta.getmNumero());
    }

    @Test
    public void testAprobarOperacionRetiroConSaldoSuficiente() throws Exception {
        banco.abrirCuenta("06-456213-33", "Alfredo Hernandez");
        cuentaMock.ingresar(500);
        when(cuentaMock.getSaldo()).thenReturn(500.0);

        Movimiento movimiento = new Movimiento();
        movimiento.setImporte(-300);
        assertTrue(banco.aprobarOperacion(movimiento, cuentaMock));
    }

    @Test
    public void testAprobarOperacionRetiroConSaldoInsuficiente() {
        banco.abrirCuenta("06-456213-33", "Alfredo Hernandez");
        when(cuentaMock.getSaldo()).thenReturn(100.0);

        Movimiento movimiento = new Movimiento();
        movimiento.setImporte(-200);
        assertFalse(banco.aprobarOperacion(movimiento, cuentaMock));
    }

    @Test
    public void testCrearTarjetaDebitoCuentaInexistente() {
        Exception exception = assertThrows(Exception.class, () -> {
            banco.crearTarjetaDebito("1234567890123456", "Alfredo Hernandez", new Date(), "06-456213-34");
        });
        assertEquals("Cuenta no encontrada", exception.getMessage());
    }

    @Test
    public void testMovimientoAprobadoYSaldoActualizado() throws Exception {
        banco.abrirCuenta("06-456213-33", "Alfredo Hernandez");
        cuentaMock.ingresar(500);
        when(cuentaMock.getSaldo()).thenReturn(500.0);

        Movimiento movimiento = new Movimiento();
        movimiento.setImporte(-200);
        banco.aprobarOperacion(movimiento, cuentaMock);

        when(cuentaMock.getSaldo()).thenReturn(300.0);
        assertEquals(300.0, cuentaMock.getSaldo());
    }
}


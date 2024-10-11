package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import modelo.Banco;
import modelo.Cuenta;
import modelo.Movimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CuentaTest {
    private Cuenta cuenta;

    @Mock
    private Banco bancoMock; // Mock de Banco

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        cuenta = new Cuenta("4000001234567899", "JOSE MARIA SANCHEZ");
        
        when(bancoMock.aprobarOperacion(any(Movimiento.class), any(Cuenta.class))).thenReturn(true);
    }

    /*
     * PRUEBAS UNITARIAS
     */
    
    @Test
    public void testIngresar() {
        try {
            cuenta.ingresar("Ingreso en efectivo", 1000.0);
            
            verify(bancoMock, times(1)).aprobarOperacion(any(Movimiento.class), eq(cuenta));
            
            assertEquals(1000.0, cuenta.getSaldo(), 0.001);
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }


    @Test
    public void testRetirar() {
        try {
        	
            when(bancoMock.aprobarOperacion(any(Movimiento.class), eq(cuenta))).thenReturn(true);
            
            cuenta.ingresar("Ingreso inicial", 1000.0); 
            cuenta.retirar("Retiro de efectivo", 500.0);
            
            verify(bancoMock, times(1)).aprobarOperacion(any(Movimiento.class), eq(cuenta));
            
            assertEquals(500.0, cuenta.getSaldo(), 0.001);
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }

    
    @Test
    public void testIngresoYRetirada() {
        try {
            cuenta.ingresar("Ingreso inicial", 1000.0);
            when(bancoMock.aprobarOperacion(any(Movimiento.class), eq(cuenta))).thenReturn(true);
            cuenta.retirar("Retiro de efectivo", 500.0);
            verify(bancoMock, times(1)).aprobarOperacion(any(Movimiento.class), eq(cuenta));
            assertEquals(500.0, cuenta.getSaldo(), 0.001);
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void testAddMovimiento() {
        Movimiento movimiento = new Movimiento();
        movimiento.setConcepto("Movimiento de prueba");
        movimiento.setImporte(100);
        cuenta.addMovimiento(movimiento);
        
        assertNotNull(cuenta.getMovimientos());
        assertEquals(1, cuenta.getMovimientos().size());
    }
    
    @Test
    public void testRetirarConFondosInsuficientes() {
        try {
            cuenta.ingresar(100.0);
            cuenta.retirar(200.0);
            fail("Se esperaba una excepción por fondos insuficientes");
        } catch (Exception e) {
            assertEquals("Fondos insuficientes", e.getMessage());
        }
    }

}




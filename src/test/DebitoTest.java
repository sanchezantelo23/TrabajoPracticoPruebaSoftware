package test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import modelo.Debito;
import modelo.Cuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DebitoTest {
	@Mock
	private Cuenta cuentaMock; // Mock de Cuenta

	private Debito tarjetaDebito;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		cuentaMock = mock(Cuenta.class);
		when(cuentaMock.getSaldo()).thenReturn(1000.0);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = "2025-12-31";
		Date fecha = sdf.parse(dateInString);

		tarjetaDebito = new Debito("1234567890123456", "Juan Perez", fecha, cuentaMock); // Asociar cuentaMock
	}

	/*
	 * PRUEBAS UNITARIAS
	 * 
	 */

	@Test
	public void testIngresar() {
		try {
			tarjetaDebito.ingresar(1000.0);
			verify(cuentaMock, times(1)).ingresar("Ingreso en cajero automatico", 1000.0);
			assertEquals(1000.0, tarjetaDebito.getSaldo(), 0.001);
		} catch (Exception e) {
			fail("No debería lanzar excepción: " + e.getMessage());
		}
	}

	@Test
	public void testRetirar() throws Exception {
		when(cuentaMock.getSaldo()).thenReturn(1000.0);
		tarjetaDebito.retirar("Retiro", 500.0);
		when(cuentaMock.getSaldo()).thenReturn(500.0);
		assertEquals(500.0, cuentaMock.getSaldo(), 0.001);
	}

	@Test
	public void testRetirarSaldoInsuficiente() {
		try {
			when(cuentaMock.getSaldo()).thenReturn(100.0);
			tarjetaDebito.retirar(200.0);
			fail("Se esperaba una excepción por saldo insuficiente");
		} catch (Exception e) {
			assertEquals("Saldo insuficiente", e.getMessage());
		}
	}

	@Test
	public void testIngresarCantidadNegativa() {
		try {
			tarjetaDebito.ingresar(-500.0);
			fail("Se esperaba una excepción por cantidad negativa");
		} catch (Exception e) {
			assertEquals("No se puede ingresar una cantidad negativa", e.getMessage());
		}
	}

	@Test
	public void testRetirarCantidadNegativa() {
		try {
			tarjetaDebito.retirar(-100.0);
			fail("Se esperaba una excepción por cantidad negativa");
		} catch (Exception e) {
			assertEquals("No se puede retirar una cantidad negativa", e.getMessage());
		}
	}
}

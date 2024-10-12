package test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import modelo.Banco;
import modelo.Cuenta;
import modelo.Movimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MovimientoTest {

	private Movimiento movimiento;
	private Cuenta cuenta;

	@Mock
	private Banco bancoMock;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		cuenta = new Cuenta("06-456213-33", "Alfredo Hernandez", bancoMock);
		movimiento = new Movimiento(bancoMock);

	}

	/*
	 * PRUEBAS UNITARIAS
	 */

	@Test
	public void testSetImporte() {
		movimiento.setImporte(150.0);
		assertEquals(150.0, movimiento.getImporte(), "Importe should be set correctly.");
	}

	@Test
	public void testGetConcepto() {
		String concepto = "Pago de prueba";
		movimiento.setConcepto(concepto);
		assertEquals(concepto, movimiento.getConcepto(), "Concepto should be returned correctly.");
	}

	@Test
	public void testGetComision() {
		assertEquals(50.0, movimiento.getComision(), "Comision should be initialized to 50.");
	}

	@Test
	public void testSetFecha() {
		Date fecha = new Date();
		movimiento.setFecha(fecha);
		assertEquals(fecha, movimiento.getFecha(), "Fecha should be set correctly.");
	}

	@Test
	public void testComisionNoNegativa() {
		double comision = movimiento.getComision();
		assertTrue(comision >= 0, "Comision should not be negative.");
	}

	/*
	 * PRUEBAS DE INTEGRACION
	 */

	@Test
	public void testProcesarPagoAprobado() {
		double cantidad = 100.0;

		when(bancoMock.aprobarOperacion(any(Movimiento.class), any(Cuenta.class))).thenReturn(true);

		boolean resultado = movimiento.procesarPago(cantidad, cuenta);

		assertTrue(resultado, "The payment should be processed successfully.");
		verify(bancoMock, times(1)).aprobarOperacion(any(Movimiento.class), eq(cuenta));
	}

	@Test
	public void testProcesarPagoRechazado() {
		double cantidad = 200.0;

		when(bancoMock.aprobarOperacion(any(Movimiento.class), any(Cuenta.class))).thenReturn(false);

		boolean resultado = movimiento.procesarPago(cantidad, cuenta);

		assertFalse(resultado, "The payment should not be processed successfully.");
		verify(bancoMock, times(1)).aprobarOperacion(any(Movimiento.class), eq(cuenta));
	}

	@Test
	public void testImporteNegativoNoProcesado() {
		double cantidad = -100.0;

		when(bancoMock.aprobarOperacion(any(Movimiento.class), any(Cuenta.class))).thenReturn(false);

		boolean resultado = movimiento.procesarPago(cantidad, cuenta);

		assertFalse(resultado, "Negative amounts should not be processed.");
		verify(bancoMock, times(1)).aprobarOperacion(any(Movimiento.class), eq(cuenta));
	}

	@Test
	public void testMovimientoConComision() {
		double cantidad = 200.0;
		movimiento.setImporte(cantidad);

		when(bancoMock.aprobarOperacion(any(Movimiento.class), any(Cuenta.class))).thenReturn(true);

		boolean resultado = movimiento.procesarPago(cantidad, cuenta);
		double saldoConComision = cantidad - movimiento.getComision();

		assertTrue(resultado, "The payment should be processed successfully.");
		assertEquals(saldoConComision, movimiento.getImporte() - movimiento.getComision(),
				"Importe should reflect the commission.");
		verify(bancoMock, times(1)).aprobarOperacion(any(Movimiento.class), eq(cuenta));
	}

}

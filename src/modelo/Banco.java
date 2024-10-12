package modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Banco {
	private String nombre;
	private List<Cuenta> cuentas; // Lista de cuentas

	public Banco(String nombre) {
		this.nombre = nombre;
		this.cuentas = new ArrayList<>();
	}

	public void abrirCuenta(String numeroCuenta, String titular) {
		cuentas.add(new Cuenta(numeroCuenta, titular, this));
	}

	// Método para obtener una cuenta
	public Cuenta obtenerCuenta(String numeroCuenta) {
		for (Cuenta cuenta : cuentas) {
			if (cuenta.getmNumero().equals(numeroCuenta)) {
				return cuenta;
			}
		}
		return null;
	}

	// Método para crear una tarjeta de débito
	public Debito crearTarjetaDebito(String numeroTarjeta, String titular, Date fechaCaducidad, String numeroCuenta)
			throws Exception {
		Cuenta cuenta = obtenerCuenta(numeroCuenta);
		if (cuenta == null) {
			throw new Exception("Cuenta no encontrada");
		}
		// Crea una nueva tarjeta de débito asociada a la cuenta encontrada
		return new Debito(numeroTarjeta, titular, fechaCaducidad, cuenta);
	}

	// Método para aprobar una operación (simula la aprobación de una operación
	// bancaria)
	public boolean aprobarOperacion(Movimiento movimiento, Cuenta cuenta) {
		if (movimiento.getImporte() > 0) {
			// Para ingresos, siempre se aprueba la operación
			return true;
		} else if (movimiento.getImporte() < 0) {
			// Para retiros, se aprueba solo si el saldo es suficiente
			double saldoActual = cuenta.getSaldo();
			if (saldoActual + movimiento.getImporte() >= 0) {
				return true; // Saldo suficiente para retirar
			} else {
				return false; // Saldo insuficiente para la operación
			}
		}
		return false; // Operación no reconocida
	}

	// Getters
	public String getNombre() {
		return nombre;
	}

	public List<Cuenta> getCuentas() {
		return cuentas;
	}
}

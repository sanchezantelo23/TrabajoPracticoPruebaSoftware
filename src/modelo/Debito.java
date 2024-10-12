package modelo;

import java.util.Date;

public class Debito extends Tarjeta {
	private Cuenta mCuentaAsociada;

	public Debito(String numero, String titular, Date fechaCaducidad, Cuenta cuentaAsociada) {
		super(numero, titular, fechaCaducidad);
		this.mCuentaAsociada = cuentaAsociada;
	}

	public void retirar(double cantidad) throws Exception {
		if (cantidad < 0) {
			throw new Exception("No se puede retirar una cantidad negativa");
		}

		if (mCuentaAsociada.getSaldo() < cantidad) {
			throw new Exception("Saldo insuficiente");
		}

		mCuentaAsociada.retirar("Retirada con tarjeta de débito", cantidad);
	}

	public void retirar(String concepto, double cantidad) throws Exception {
		if (cantidad < 0) {
			throw new Exception("No se puede retirar una cantidad negativa");
		}

		if (mCuentaAsociada.getSaldo() < cantidad) {
			throw new Exception("Saldo insuficiente");
		}

		mCuentaAsociada.retirar(concepto, cantidad);
	}

	public void ingresar(double cantidad) throws Exception {
		if (cantidad < 0) {
			throw new Exception("No se puede ingresar una cantidad negativa");
		}
		this.mCuentaAsociada.ingresar("Ingreso en cajero automatico", cantidad);
	}

	public void pagoEnEstablecimiento(String datos, double cantidad) throws Exception {
		if (cantidad < 0) {
			throw new Exception("No se puede pagar una cantidad negativa");
		}
		this.mCuentaAsociada.retirar("Compra en: " + datos, cantidad);
	}

	public double getSaldo() {
		return mCuentaAsociada.getSaldo();
	}

	public void setCuentaAsociada(Cuenta cuenta) {
		this.mCuentaAsociada = cuenta;
	}
}

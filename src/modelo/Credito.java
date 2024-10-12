package modelo;

import java.util.Vector;
import java.util.Date;

public class Credito extends Tarjeta {
	protected double mCredito;
	protected Vector<Movimiento> mMovimientos;

	public Credito(String numero, String titular, Date fechaCaducidad, double credito) {
		super(numero, titular, fechaCaducidad);
		mCredito = credito;
		mMovimientos = new Vector<>();
	}

	public void retirar(double x) throws Exception {
		if (x <= 0) {
			throw new Exception("No se puede retirar una cantidad negativa");
		}

		double comision = Math.max(3.0, x * 0.05);
		if (comision + x > getCreditoDisponible()) {
			throw new Exception("Crédito insuficiente");
		}

		Movimiento m = new Movimiento();
		m.setConcepto("Retirada en cajero automático (comisión aplicada)");
		m.setImporte(comision + x);
		mMovimientos.addElement(m);
	}

	public void ingresar(double x) throws Exception {
		if (x <= 0) {
			throw new Exception("No se puede ingresar una cantidad negativa");
		}

		Movimiento m = new Movimiento();
		m.setConcepto("Ingreso en cuenta asociada (cajero automático)");
		m.setImporte(x);
		mMovimientos.addElement(m);
		mCuentaAsociada.ingresar(x);
	}

	public void pagoEnEstablecimiento(String datos, double x) throws Exception {
		if (x <= 0) {
			throw new Exception("No se puede pagar una cantidad negativa");
		}

		Movimiento m = new Movimiento();
		m.setConcepto("Compra a crédito en: " + datos);
		m.setImporte(x);
		mMovimientos.addElement(m);
	}

	public double getSaldo() {
		double r = 0.0;
		for (Movimiento m : mMovimientos) {
			r += m.getImporte();
		}
		return r;
	}

	public double getCreditoDisponible() {
		return mCredito - getSaldo();
	}

	public void liquidar(int mes, int anio) {
		Movimiento liq = new Movimiento();
		liq.setConcepto("Liquidación de operaciones tarjeta crédito, " + (mes + 1) + " de " + (anio + 2000));
		double r = 0.0;

		for (Movimiento m : mMovimientos) {
			if (m.getFecha().getMonth() + 1 == mes && m.getFecha().getYear() + 1900 == anio) {
				r += m.getImporte();
			}
		}

		liq.setImporte(r);
		if (r != 0) {
			mCuentaAsociada.addMovimiento(liq);
		}
	}
}

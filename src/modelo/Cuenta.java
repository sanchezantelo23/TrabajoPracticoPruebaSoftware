package modelo;

import java.util.Vector;

public class Cuenta {
	protected String mNumero;
	protected String mTitular;
	protected Vector<Movimiento> mMovimientos;
	private Banco banco;

	public Cuenta(String numero, String titular, Banco banco) {
		mNumero = numero;
		mTitular = titular;
		mMovimientos = new Vector<>();
		this.banco = banco;
	}

	public String getmNumero() {
		return mNumero;
	}

	public void setmNumero(String mNumero) {
		this.mNumero = mNumero;
	}

	public String getmTitular() {
		return mTitular;
	}

	public void setmTitular(String mTitular) {
		this.mTitular = mTitular;
	}

	public Vector<Movimiento> getmMovimientos() {
		return mMovimientos;
	}

	public void setmMovimientos(Vector<Movimiento> mMovimientos) {
		this.mMovimientos = mMovimientos;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public void ingresar(double x) throws Exception {
		if (x <= 0)
			throw new Exception("No se puede ingresar una cantidad negativa");
		Movimiento m = new Movimiento();
		m.setConcepto("Ingreso en efectivo");
		m.setImporte(x);
		this.mMovimientos.addElement(m);
	}

	public void retirar(double x) throws Exception {
		if (x <= 0)
			throw new Exception("No se puede retirar una cantidad negativa");
		if (getSaldo() < x)
			throw new Exception("Saldo insuficiente");
		Movimiento m = new Movimiento();
		m.setConcepto("Retirada de efectivo");
		m.setImporte(-x);
		this.mMovimientos.addElement(m);
	}

	public void ingresar(String concepto, double x) throws Exception {
		if (x <= 0) {
			throw new Exception("No se puede ingresar una cantidad negativa");
		}

		Movimiento m = new Movimiento();
		m.setConcepto(concepto);
		m.setImporte(x);

		if (banco.aprobarOperacion(m, this)) {
			this.mMovimientos.addElement(m);
		} else {
			throw new Exception("Operación no aprobada por el banco");
		}
	}

	public void retirar(String concepto, double cantidad) throws Exception {
		if (cantidad < 0) {
			throw new Exception("No se puede retirar una cantidad negativa");
		}

		if (this.getSaldo() < cantidad) {
			throw new Exception("Saldo insuficiente");
		}

		Movimiento m = new Movimiento();
		m.setConcepto(concepto);
		m.setImporte(-cantidad);
		if (banco.aprobarOperacion(m, this)) {
			this.mMovimientos.addElement(m);
		} else {
			throw new Exception("Operación no aprobada por el banco");
		}
	}

	public double getSaldo() {
		double r = 0.0;
		for (Movimiento m : mMovimientos) {
			r += m.getImporte();
		}
		return r;
	}

	public Vector<Movimiento> getMovimientos() {
		return mMovimientos;
	}

	public void addMovimiento(Movimiento m) {
		mMovimientos.addElement(m);
	}
}

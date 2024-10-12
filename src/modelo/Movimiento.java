package modelo;

import java.util.Date;

public class Movimiento {
	protected String mConcepto;
	protected Date mFecha;
	protected double mImporte;
	private double comision;

	private Banco bancoDestino;

	public Movimiento(Banco bancoDestino) {
		this.bancoDestino = bancoDestino;
		this.comision = 50;
		mFecha = new Date();
	}

	public Movimiento() {
		mFecha = new Date();
	}

	public double getImporte() {
		return mImporte;
	}

	public String getConcepto() {
		return mConcepto;
	}

	public void setConcepto(String newMConcepto) {
		mConcepto = newMConcepto;
	}

	public Date getFecha() {
		return mFecha;
	}

	public void setFecha(Date newMFecha) {
		mFecha = newMFecha;
	}

	public void setImporte(double newMImporte) {
		mImporte = newMImporte;
	}

	public double getComision() {
		return this.comision;
	}

	public boolean procesarPago(double cantidad, Cuenta cuenta) {
		Movimiento movimiento = new Movimiento();
		movimiento.setImporte(cantidad);
		return bancoDestino.aprobarOperacion(movimiento, cuenta);
	}
}

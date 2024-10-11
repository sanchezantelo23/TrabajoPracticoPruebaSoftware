package modelo;

import java.util.Vector;

public class Cuenta 
{
	protected String mNumero;
	protected String mTitular;
	protected Vector mMovimientos;
	private Banco banco;

	public Cuenta(String numero, String titular)
	{
		mNumero=numero;
		mTitular=titular;
		mMovimientos=new Vector();
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

	public Vector getmMovimientos() {
		return mMovimientos;
	}

	public void setmMovimientos(Vector mMovimientos) {
		this.mMovimientos = mMovimientos;
	}
	
	
	
	public void ingresar(double x) throws Exception
	{
		if (x<=0)
			throw new Exception("No se puede ingresar una cantidad negativa");
		Movimiento m=new Movimiento();
		m.setConcepto("Ingreso en efectivo");
		m.setImporte(x);
		this.mMovimientos.addElement(m);
	}
	
	public void retirar(double x) throws Exception 
	{
		if (x<=0)
			throw new Exception("No se puede retirar una cantidad negativa");	
		if (getSaldo()<x)
			throw new Exception("Saldo insuficiente");
		Movimiento m=new Movimiento();
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

	    // Llama al método aprobarOperacion del banco para validar el ingreso
	    if (banco.aprobarOperacion(m, this)) {
	        this.mMovimientos.addElement(m); 
	    } else {
	        throw new Exception("Operación no aprobada por el banco");
	    }
	}

	
	public void retirar(String concepto, double x) throws Exception {
        if (x <= 0) {
            throw new Exception("No se puede retirar una cantidad negativa");
        }
        if (getSaldo() < x) {
            throw new Exception("Saldo insuficiente");
        }

        Movimiento m = new Movimiento();
        m.setConcepto(concepto);
        m.setImporte(-x);

        // Llama al método aprobarOperacion del banco
        if (banco.aprobarOperacion(m, this)) {
            this.mMovimientos.addElement(m);
        } else {
            throw new Exception("Operación no aprobada por el banco");
        }
    }

	
	public double getSaldo() 
	{
		double r=0.0;
		for (int i=0; i<this.mMovimientos.size(); i++) 
		{
			Movimiento m=(Movimiento) mMovimientos.elementAt(i);
			r+=m.getImporte();
		}
		return r;
	}
	public Vector getMovimientos(){
		return mMovimientos;
	}
	
	public void addMovimiento(Movimiento m) 
	{
		mMovimientos.addElement(m);
	}

	
	
}
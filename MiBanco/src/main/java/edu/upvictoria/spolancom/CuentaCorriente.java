package edu.upvictoria.spolancom;

public class CuentaCorriente extends CuentaBase {

    private final float limiteSobreGiro;
    public CuentaCorriente(Usuario tit, String numCuenta, float limiteSobregiro) {
        super(tit, numCuenta);
        this.limiteSobreGiro = limiteSobregiro;
    }

    @Override
    public boolean retirar(float monto) {
        float nuevoSaldoProyectado = this.saldo - monto;

        if (nuevoSaldoProyectado < (this.limiteSobreGiro * -1.0f)) {
            return false;
        } else {
            this.saldo = nuevoSaldoProyectado;
            return true;
        }
    }
}

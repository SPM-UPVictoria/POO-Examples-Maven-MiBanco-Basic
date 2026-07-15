package edu.upvictoria.spolancom;

public class CuentaAhorro extends CuentaBase{

    private final float tasaInteres;

    public CuentaAhorro(Usuario tit, String numCuenta, float tasaint) {
        super(tit, numCuenta);
        this.tasaInteres = tasaint;
    }

    public void aplicarInteresMensual() {
        this.saldo = this.saldo + (this.saldo*this.tasaInteres);
    }

    @Override
    public boolean retirar(float monto) {
        float tmp_monto = this.saldo - monto;
        if (tmp_monto < 0) {
            return false;
        } else {
            this.saldo = tmp_monto;
            return true;
        }
    }
}

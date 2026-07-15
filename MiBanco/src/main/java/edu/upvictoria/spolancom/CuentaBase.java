package edu.upvictoria.spolancom;

public abstract class CuentaBase {

    private static int totalCuentasCreadas = 0;

    protected float saldo;
    private Usuario titular;
    private String numeroCuenta;

    public CuentaBase(Usuario tit, String numCuenta) {
        totalCuentasCreadas++;
        this.titular = tit;
        this.numeroCuenta = numCuenta;
        this.saldo = 0.0f;
    }

    public void depositar(float monto) {
        this.saldo = this.saldo + monto;
    }

    public void depositar(float cantidad, Divisas div){
        this.saldo = this.saldo + convertir_divisas(cantidad, div);
    }

    public float obtenerSaldo() {
        return this.saldo;
    }

    public static int obtenerTotalCuentas() {
        return totalCuentasCreadas;
    }


    private float convertir_divisas(float monto, Divisas div) {
        float monto_en_MXN=0;

        switch (div) {
            case USD:
                monto_en_MXN = monto*17.48f;
                break;
            case EUR:
                monto_en_MXN = monto*19.95f;
                break;
            case CAD:
                monto_en_MXN = monto*12.34f;
                break;
            case CHF:
                monto_en_MXN = monto*21.60f;
                break;
            default:
                break;
        }
        return monto_en_MXN;
    }

    public abstract boolean retirar(float monto);
}

package upvictoria.spolancom;

public class CuentaCorriente extends CuentaBase {

    private final float limiteSobreGiro;
    public CuentaCorriente(Usuario tit, String numCuenta, float limiteSobregiro) {
        super(tit, numCuenta);
        this.limiteSobreGiro = limiteSobregiro*-1.0f;
    }

    @Override
    public boolean retirar(float monto) {
        float tmp_monto = this.saldo - monto;

        if (tmp_monto < this.limiteSobreGiro) {
            return false;
        } else {
            this.saldo = tmp_monto;

            if (this.saldo < 0) {
                System.err.println("El saldo actual esta en sobregiro. El monto actual es: " + this.saldo);
            }

            return true;
        }
    }
}

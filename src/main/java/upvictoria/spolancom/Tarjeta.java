package upvictoria.spolancom;

public class Tarjeta {
    private final String numeroTarjeta;
    private final Integer pinSeguridad;
    private CuentaBase cuentaAsociada;

    public Tarjeta(String nt, Integer ps, CuentaBase ca) {
        this.numeroTarjeta = nt;
        this.pinSeguridad = ps;
        this.cuentaAsociada = ca;
    }

    public boolean realizarPago(float monto, String nt,  int pinIngresado) {
        boolean ban = false;

        if (this.numeroTarjeta.equals(nt) && pinIngresado == this.pinSeguridad) {
            this.cuentaAsociada.retirar(monto);
        }

        return ban;
    }
}

package dds.monedero.model.movimientos;

import dds.monedero.model.Cuenta;
import java.time.LocalDate;

public abstract class Movimiento {

  private LocalDate fecha;
  private double monto;

  public Movimiento(LocalDate fecha, double monto) {
    this.fecha = fecha;
    this.monto = monto;
  }

  public double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public boolean fueOperadaEn(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }

  public abstract double calcularValor(Cuenta cuenta);

  public void agregateA(Cuenta cuenta) {
    cuenta.setSaldo(calcularValor(cuenta));
    cuenta.agregarMovimiento(this);
  }

  public abstract double getMontoExtraccion();
}

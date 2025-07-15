package dds.monedero.model.movimientos;

import dds.monedero.model.Cuenta;
import java.time.LocalDate;

public class Deposito extends Movimiento {

  public Deposito(LocalDate fecha, double monto) {
    super(fecha, monto);
  }

  @Override
  public double calcularValor(Cuenta cuenta) {
    return cuenta.getSaldo() + getMonto();
  }

  @Override
  public double getMontoExtraccion() {
    return 0;
  }
}

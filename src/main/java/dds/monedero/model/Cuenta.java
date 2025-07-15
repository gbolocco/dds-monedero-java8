package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import dds.monedero.model.movimientos.Deposito;
import dds.monedero.model.movimientos.Extraccion;
import dds.monedero.model.movimientos.Movimiento;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

// ========================

  public void depositar(double cuanto) {
    validarDeposito(cuanto);
    new Deposito(LocalDate.now(), cuanto).agregateA(this);
  }

  private void validarDeposito(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }

    if (getMovimientos().stream()
        .filter(movimiento -> movimiento.fueOperadaEn(LocalDate.now()))
        .count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  // ========================

  public void extraer(double cuanto) {
    validarExtraccion(cuanto);
    new Extraccion(LocalDate.now(), cuanto).agregateA(this);
  }

  private void validarExtraccion(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede extraer mas de " + getSaldo() + " $");
    }
    var montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    var limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException(
          "No puede extraer mas de $ " + 1000 + " diarios, " + "límite: " + limite);
    }
  }

  public void agregarMovimiento(Movimiento movimiento) {
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMontoExtraccion)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}

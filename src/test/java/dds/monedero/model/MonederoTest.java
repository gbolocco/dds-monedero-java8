package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  @DisplayName("Es posible depositar $1500 en una cuenta vacía")
  void Poner() {
    double saldoAntes = cuenta.getSaldo();
    cuenta.depositar(1500);
    assertTrue( saldoAntes < cuenta.getSaldo() );
  }

  @Test
  @DisplayName("No es posible depositar montos negativos")
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.depositar(-1500));
  }

  @Test
  @DisplayName("Es posible realizar múltiples depósitos consecutivos")
  void TresDepositos() {
    assertDoesNotThrow(() -> {
      cuenta.depositar(1500);
      cuenta.depositar(456);
      cuenta.depositar(1900);
    });
  }

  @Test
  @DisplayName("No es posible superar la máxima cantidad de depositos diarios")
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
      cuenta.depositar(1500);
      cuenta.depositar(456);
      cuenta.depositar(1900);
      cuenta.depositar(245);
    });
  }

  @Test
  @DisplayName("No es posible extraer más que el saldo disponible")
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
      cuenta.setSaldo(90);
      cuenta.extraer(1001);
    });
  }

  @Test
  @DisplayName("No es posible extraer más que el límite diario")
  void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.extraer(1001);
    });
  }

  @Test
  @DisplayName("No es posible extraer un monto negativo")
  void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.extraer(-500));
  }

}
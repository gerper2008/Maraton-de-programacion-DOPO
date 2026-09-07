# Pruebas de aceptación — Ciclo 2

Estas pruebas se ejecutan con el simulador VISIBLE (llamando a
`makeVisible()`), invocando los métodos uno a uno desde el banco de
objetos de BlueJ, para mostrar en la sustentación que el sistema hace
lo que el profesor observa en pantalla.

---

## Prueba de aceptación 1 — Bloquear una rueda y girar la máquina

**Objetivo:** demostrar los requisitos 10 (fijar/soltar) y 11 (rotar
un número de pasos), y que una rueda bloqueada no gira.

**Precondición:** máquina con 3 ruedas, cada una con los símbolos
"cherry", "bell" y "bar".

**Pasos:**

1. Crear la máquina: `SlotMachine machine = new SlotMachine();`
2. `machine.addWheel(1)`, `machine.addWheel(2)`, `machine.addWheel(3)`
3. Agregar los símbolos "cherry" (color 1), "bell" (color 3) y "bar"
   (color 4) a cada una de las 3 ruedas.
4. `machine.makeVisible()` → se deben ver los 3 círculos de color en
   el canvas.
5. `machine.spin(new String[] {"cherry", "cherry", "cherry"})` → las
   3 ruedas deben mostrar el símbolo "cherry".
6. `machine.lock(2)` → la rueda 2 queda fijada.
7. `machine.spin(2, 3)` → **resultado esperado:** la rueda 2 sigue
   mostrando "cherry" (no se movió) y `machine.ok()` es `true` (la
   operación en sí fue válida, simplemente la rueda no gira).
8. `machine.spin(1, 1)` → **resultado esperado:** se ve, paso a paso
   (con una breve pausa visible), cómo la rueda 1 cambia de "cherry"
   a "bell".
9. `machine.unlock(2)` seguido de `machine.spin(2, 1)` → **resultado
   esperado:** ahora sí la rueda 2 cambia de "cherry" a "bell".

**Criterio de aceptación:** la rueda bloqueada nunca cambia de
símbolo mientras está fija, y vuelve a girar apenas se libera; el
giro con pasos se ve animado símbolo por símbolo, no de un salto.

---

## Prueba de aceptación 2 — Intercambiar ruedas y fijar una configuración

**Objetivo:** demostrar los requisitos 9 (intercambiar dos ruedas) y
12 (dejar la máquina en una configuración dada), y su relación con
`isJackpot()`.

**Precondición:** misma máquina de 3 ruedas y 3 símbolos del caso
anterior, visible.

**Pasos:**

1. `machine.spin(new String[] {"cherry", "bell", "bar"})` →
   **resultado esperado:** las ruedas muestran, en orden,
   "cherry", "bell", "bar". `machine.isJackpot()` debe ser `false`
   (los símbolos son distintos).
2. `machine.swap(1, 3)` → **resultado esperado:** ahora la rueda 1
   muestra "bar" y la rueda 3 muestra "cherry" (se intercambiaron),
   la rueda 2 sigue en "bell".
3. `machine.spin(new String[] {"bar", "bar", "bar"})` →
   **resultado esperado:** las 3 ruedas muestran "bar" y
   `machine.isJackpot()` ahora es `true`.
4. `machine.spin(new String[] {"cherry", "bell"})` (arreglo de tamaño
   incorrecto) → **resultado esperado:** aparece el mensaje de error
   (la máquina es visible), `machine.ok()` es `false`, y la
   configuración de las ruedas **no cambia** (sigue en "bar","bar","bar").

**Criterio de aceptación:** `swap` intercambia exactamente las dos
ruedas indicadas sin afectar las demás; fijar una configuración válida
cambia todas las ruedas a la vez; una configuración inválida no deja
la máquina a medio cambiar y se refleja en `ok()` y en el mensaje de
error visible.

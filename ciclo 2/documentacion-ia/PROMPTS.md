# Trazabilidad de prompts usados con IA — Ciclo 2

Por cada pieza de código generada con ayuda de IA, se indica muy
brevemente el prompt (petición) que se usó.

| Código generado | Prompt usado (resumen) |
|---|---|
| `SlotMachine.swap(wheel1, wheel2)` | "Implementa el requisito de intercambiar dos ruedas de la máquina." |
| `SlotMachine.lock(wheel)` / `unlock(wheel)` y `Wheel.locked` | "Agrega fijar y soltar una rueda para que no gire." |
| `Wheel.spin(int steps)` con pausa por paso | "Rota una rueda N pasos y que se vea el movimiento paso a paso si es visible." |
| `SlotMachine.spin(String[] setSymbols)` | "Deja la máquina en una configuración dada, un símbolo por rueda." |
| Cambio de `Wheel.makeVisible()` a mostrar solo el símbolo actual | "El giro no se ve porque se muestran todos los símbolos a la vez; corrígelo." |
| Corrección atómica de `spin(String[])` | "Diseña las pruebas de qué no debería hacer spin(setSymbols) y corrige lo que encuentres." |
| `SlotMachineC2Test.java` (18 casos) | "Ayúdame con las pruebas unitarias del ciclo 2, en modo invisible, con casos de qué debería y qué no debería hacer." |
| `SlotMachineCC2Test.java` (stub compartido) | "Dame el stub de la clase de pruebas compartida por wiki con el formato de nombre que pide el enunciado." |
| `ACCEPTANCE_TESTS.md` | "Dime cómo organizar las dos pruebas de aceptación para la sustentación." |
| Estructura de carpetas del entregable | "Organiza todo lo del ciclo 2 (código y pruebas) en una estructura clara para el repo." |
| Javadoc por método de prueba | "Revisa la documentación y agrégala donde falte, siguiendo el estándar del proyecto." |

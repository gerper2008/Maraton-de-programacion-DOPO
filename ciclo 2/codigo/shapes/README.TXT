Project "shapes"
Authors: Michael Kolling and David J. Barnes

This project is part of the material for the book

   Objects First with Java - A Practical Introduction using BlueJ
   David J. Barnes and Michael Kolling
   Pearson Education, 2002

It is discussed in chapter 1.

This is a very simple project to demonstrate some characteristics of
objects.

You can create various shapes, and you will see, if you do, that those
shapes are drawn on screen (in a window that we call the "canvas").

You can then manipulate these objects: change their position, size and 
colour. Try it out: create a few different squares, triangles and circles.

This project is designed as a first example of object-oriented programming.
It illustrates a number of concepts:

 - a Java project (application) is a collection of classes
 - objects can be created from classes
 - from any one class, many objects may be created
 - objects have operations (methods)
 - operations can have parameters
 - parameters have types (at least String and int)
 - objects hold data (fields)
 - the operations and fields are common to all objects
 - the values stored in the fields can be different for each object

The project also demonstrates

 - BlueJ object creation
 - interactive method invocation
 - parameter passing

A good second project to look at after this is "picture", which adds a class
to those ones in this project. That class (named "Picture") uses the shapes
to draw a picture. It can be used to experiment with coding.

Michael Kolling, July 2000

--------------------------------------------------------------------
ESCUELA COLOMBIANA DE INGENIERÍA - DESARROLLO ORIENTADO POR OBJETOS
Proyecto inicial 2026-2 - Simulador de Slot Machine (Problem I, 2025)

CICLO 1: SlotMachine, Wheel, Symbol - operaciones básicas de creación,
gestión de ruedas y símbolos, giro simple, consulta de símbolos y
verificación de jackpot.

CICLO 2 (Refactoring y Extensión) - agregado sobre Wheel y SlotMachine:
 - swap(wheel1, wheel2): intercambia dos ruedas de posición.
 - lock(wheel) / unlock(wheel): fija/suelta una rueda para que no gire.
 - spin(wheel, steps): rota una rueda un número de pasos determinado;
   si el simulador está visible, el movimiento se visualiza paso a paso.
 - spin(String[] setSymbols): deja la máquina en una configuración dada,
   colocando en cada rueda el símbolo indicado en el arreglo.

Pendiente de este ciclo (no incluido en este .zip):
 - Diagrama de clases actualizado en Astah.
 - SlotMachineC2Test (pruebas unitarias individuales, en modo invisible).
 - SlotMachineCC2Test (pruebas compartidas del curso, vía wiki).
 - Pruebas de aceptación para la sustentación.
 - Documento de retrospectiva (ciclo 1 y 2, por mini-ciclos).

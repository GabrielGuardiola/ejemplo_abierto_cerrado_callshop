# Sistema de gestion de locutorios telefónicos

## El ejemplo

Se trata de un ejemplo inspirado en una aplicación real. Se trata de un controlador para locutorios telefónicos sobre VoIP

La aplicación original controla los routers, lleva un plan de numeración, gestiona los diferentes puestos de llamadas, etc. 
No obstante, el ejemplo se limita al subsistema de tarificación

## Subsistema de tarificación (Rater)
El plan de numeración (subsistema que excede a nuestro ejemplo) define los prefijos de los números telefónicos (nacionales, locales, etc)
y es capaz de, dada una llamada, determinar a que nodo del plan de numeración corresponde.

Por ejemplo, si nuestro cliente llama al número 34911234567, el plan de numeración nos dirá que es un numero del nodo 349
(España fijo) si llama al 34611234567 correspondrá al nodo 346 (España móvil) y si llama al 54111234567 corresponderá al nodo 54 (Argentina)

Para poder tarificar una llamada (es decir, determinar el precio de la llamada y cobrarsela al cliente) necesitamos tarifas.
Cada nodo del plan de numeración tiene asociado una tarifa. Por ejemplo, el nodo 349 (España fijo) puede tener una tarifa de 0.02 euros 
por minuto + 0.10 de establecimiento de llamada.

El subsistema de tarificación (Rater) se encarga de crear dichas tarifas y de tarificar las llamadas, esto es, dada una llamada y una 
tarifa, determinar el precio de la llamada aplicando esa tarifa (recordemos que es el plan de numeración el que nos dice tarifa corresponde)

## RaterEngine
En nuestro caso, hemos generado una pequeña aplicación de consola encargada de hacer llamadas a Rater para demostrar el funcionamiento del
modulo Rater. En un sistema real, tendríamos una aplicacion (o incluso varias), mucho más complejas. El modulo RaterEngine, simplemente crea
tarifas usando el submodulo Rater y luego testea la tarifas creadas.

## El problema
Necesitamos que los modelos de tarifa sean flexibles. Inicialmente tenemos un modelo de tarifas basado en precio por minuto + establecimiento
pero queremos que en el futuro podamos incorporar otros modelos de tarifa aún por determinar.

RATER debe ser capaz de proveer un mecanismo para crear dichas tarifas (en el ejemplo se hace mediante una aplicacion de consola) teniendo en cuenta
que cada modelo de tarifa puede tener sus propias reglas de tarificación y por tanto parámetros diferentes.

Incorporar nuevos modelos de tarifa no debe requerir modificar el código de la aplicación (Ni Rater ni RaterEngine)

## La solución

Para abordar esta situación se ha definido la Interfaz *Rate* que representa una tarifa. Dicha tiene un unico método *CalculatePrice*
que recibe un objeto de la clase *RateableCall* (estructura de datos con la información relevante para tarificar una llamada) y devuelve el 
precio de la llamada.

Por cada modelo de tarifa tendremos una clase que implementa la interfaz *Rate* y tendrá su propia lógica de tarificación. Una tarifa concreta es una 
instancia de alguna de estas clases

Ahora bien, cada una de estas clases puede tener sus propios parámetros. Por ello, también se ha definido la interfaz *RateFactory* que representa
una factoria de tarifas. Por cada clase *Rate* tendremos una clase *RateFactory* que se encargará de crear instancias de la clase *Rate* con los 
parametros adecuados. *RateFactory* define un metodo *makeRate* que recibe un objeto *ParameterReader* y devuelve una instancia de *Rate*.

Por último, *ParameterReader* es un interfaz que representa un lector de parametros. Cada lector de parámetros lee los parametros de una manera
concreta (de un archivo, de la linea de comandos -como es nuestro caso-, etc). *ParameterReader* tiene un metodo readParameters de recibe
una lista de parametros (nuestro parametros son objetos que tienen todo lo necesario para poder leer y validar el valor) y devuelve un objeto
*ParametersMapper* que establece una relación entre el nombre del parametro y su valor asignado. Con este mapper la factoria crea la tarifa con los
parametros adecuados.



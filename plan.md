## Plan: Detección y corrección de errores léxicos y sintácticos

Tomaría un enfoque incremental: primero estabilizar la detección y propagación de errores en el flujo activo del compilador, después agregar recuperación sintáctica con sincronización basada en `Firsts`/`Following`, y por último encapsular heurísticas de corrección para casos frecuentes. La ruta más segura es partir de `MainSintactico` + `SyntacticAnalyzerLateStage`, porque ese es el flujo conectado al parser de etapa final; el lexer ya tiene mensajes detallados y puede servir como base para normalizar la recolección de errores.

**Steps**
1. Alinear el flujo real de compilación y definir el contrato de error.
   - Confirmar que el punto de entrada activo es [src/main/MainSintactico.java](src/main/MainSintactico.java) y que el parser objetivo es [src/syntacticAnalyzer/SyntacticAnalyzerLateStage.java](src/syntacticAnalyzer/SyntacticAnalyzerLateStage.java).
   - Definir qué debe pasar cuando haya error: acumular, reportar y continuar si es recuperable; abortar solo si se pierde la sincronización global.
   - Unificar el formato mínimo de error léxico y sintáctico para que el front-end pueda mostrar ambos sin romper el flujo.

2. Implementar un contenedor de diagnóstico común para lexer y parser.
   - Reusar el formato ya existente en [src/utils/messages/LexicalErrorMessage.java](src/utils/messages/LexicalErrorMessage.java) y [src/utils/messages/SyntacticErrorMessage.java](src/utils/messages/SyntacticErrorMessage.java).
   - Centralizar la acumulación de mensajes en el nivel de ejecución, probablemente en [src/main/MainSintactico.java](src/main/MainSintactico.java) y, si conviene, en [src/main/Main.java](src/main/Main.java).
   - Evitar que una excepción individual corte todo el análisis si el problema es local y recuperable.

3. Diseñar recuperación léxica por heurísticas simples.
   - Mantener la detección existente en [src/lexicalAnalyzer/LexicalAnalyzer.java](src/lexicalAnalyzer/LexicalAnalyzer.java) y agregar recuperación solo para errores frecuentes y de bajo costo.
   - Priorizar casos como símbolo inválido aislado, literal de cadena/carácter sin cierre, comentario multilínea sin cierre y uso incorrecto de `&` o `|`.
   - Definir cómo se avanza el puntero después del error para no entrar en bucles: consumir hasta un delimitador seguro o hasta el final de la construcción afectada.

4. Agregar recuperación sintáctica basada en sincronización.
   - Modificar el contrato de [src/syntacticAnalyzer/SyntacticAnalyzerLateStage.java](src/syntacticAnalyzer/SyntacticAnalyzerLateStage.java), empezando por `match()`.
   - Usar `Firsts` y `Following` desde [src/model/Firsts.java](src/model/Firsts.java) y [src/model/Following.java](src/model/Following.java) para decidir cuándo descartar tokens y cuándo reanudar.
   - Definir puntos de sincronización por nivel: inicio de clase, miembros, sentencias, expresiones y cierres de bloque.
   - La primera meta no es “corregir todo”, sino seguir parseando el archivo y registrar más de un error por ejecución.

5. Encapsular heurísticas de corrección para errores sintácticos recurrentes.
   - Empezar con heurísticas locales y deterministas: token faltante esperado, token extra aislado, delimitador omitido y paréntesis/llaves desbalanceados.
   - Aplicar heurísticas solo cuando el costo de decidir sea bajo y el contexto sea suficientemente claro.
   - Si una heurística propone una corrección, validar que el siguiente estado del parser siga siendo consistente con `Firsts`/`Following`; si no, retroceder a recuperación por sincronización.

   - Matriz inicial de heurísticas por tipo de fallo:

     | Tipo de fallo | Heurística | Cuándo aplicarla | Acción |
     |---|---|---|---|
     | Omisión de token de cierre | Inserción implícita de `)`, `]`, `}`, `;` | Solo si el contexto inmediato y `Following` la hacen inequívoca | Completar el token faltante y seguir |
     | Inserción espuria | Borrado de token aislado | Cuando aparece un token inesperado entre símbolos estructurales válidos | Descartar un token y reintentar |
     | Sustitución de token | Reemplazo local | Solo en errores muy acotados, como separadores u operadores simples | Probar el token cercano más probable |
     | Borrado de token | Sin corrección material, con sincronización | Cuando la construcción quedó incompleta pero el siguiente token cae en `FOLLOW` | Reanudar desde el punto seguro |
     | Desbalance de paréntesis/llaves | Conteo superficial + cierre más cercano | Cuando el cierre esperado es claro y no hay ambigüedad fuerte | Insertar cierre si es seguro; si no, sincronizar |
6. Preparar observabilidad para validar la recuperación.
   - Registrar el token encontrado, el token esperado y el punto de reanudación elegido.
   - Diferenciar en la salida entre error detectado, corrección aplicada y recuperación fallida.
   - Mantener un modo de ejecución que siga siendo útil para comparar contra los casos de `resources/`.

7. Validar contra casos mínimos antes de ampliar cobertura.
   - Probar un archivo válido sin regresiones.
   - Probar un error léxico aislado y confirmar que el análisis continúa.
   - Probar un error sintáctico simple dentro de una clase, un miembro y una sentencia.
   - Probar un caso con múltiples errores para verificar que el parser no se detiene en el primero.

**Relevant files**
- `src/main/MainSintactico.java` — flujo activo de análisis sintáctico y punto de integración para acumulación de diagnósticos.
- `src/syntacticAnalyzer/SyntacticAnalyzerLateStage.java` — parser principal a adaptar para recuperación y heurísticas.
- `src/lexicalAnalyzer/LexicalAnalyzer.java` — detección y posible recuperación léxica.
- `src/model/Firsts.java` — conjuntos `FIRST` para decidir arranque y validación de tokens.
- `src/model/Following.java` — conjuntos `FOLLOW` para sincronización y reanudación.
- `src/utils/messages/LexicalErrorMessage.java` — formato actual de errores léxicos.
- `src/utils/messages/SyntacticErrorMessage.java` — formato actual de errores sintácticos.
- `src/main/Main.java` — referencia del flujo completo de compilación si conviene unificar manejo de excepciones.

**Verification**
1. Ejecutar el flujo sintáctico con un archivo correcto y verificar que no se rompe la salida esperada.
2. Ejecutar casos mínimos de error léxico y confirmar que se reporta el error y el análisis sigue donde corresponde.
3. Ejecutar casos mínimos de error sintáctico en estructura de clase, miembro y sentencia para comprobar sincronización.
4. Comparar la salida contra archivos de `resources/sinErrores/` y `resources/Etapa*/conErrores/` para detectar regresiones.
5. Si existe un comando de build o test del proyecto, correrlo sobre el módulo tocado después de cada fase.

**Decisions**
- Asumo que “agentes” significa módulos internos de recuperación/corrección y no un agente externo de IA en tiempo de ejecución.
- El foco inicial debe estar en el parser de etapa final, porque es el flujo conectado al punto de entrada activo.
- La primera versión debe privilegiar continuidad del análisis y calidad del diagnóstico antes que corrección agresiva.
- No se intenta reescribir toda la gramática; la meta es recuperar en sitios críticos con el menor cambio posible.

**Further Considerations**
1. Si quieres, el siguiente paso puede ser diseñar una matriz de heurísticas por tipo de error: omisión, inserción, sustitución y borrado.
2. También conviene decidir si la corrección debe modificar el AST/símbolos como si el token fuera válido, o solo reportar la corrección sugerida sin mutar la estructura.
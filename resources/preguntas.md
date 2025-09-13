### [ ] Quiero agregar el logro SourceManager Eficiente y corregir los que no pasaron los tests de la Etapa 1. ¿Cómo es la entrega?

---

### [ ] Que el tipo declarado sea consistente con el valor recibido es responsabilidad del semántico, ¿no? 
Ejemplo: String s = 10;

---

### [ ] ¿VarLocal y Variable Local Clásica E2 tienen que coexistir o el logro pisa a esta producción de la gramática original?
En mi código coexisten; para pisarlo --> 
1. Borrar de SyntacticAnalyzer en sentencia() el caso de firsts... de VarLocal
2. Nadie más usa VarLocal asique borrarlo de SyntacticMethod
SIGEDU — Trabajo realizado (rama pruebav2, 04-07-2026)
Qué se logró
Se verificó, conectó y completó el sistema de punta a punta: frontend + 10 microservicios + MySQL en Docker funcionando juntos con datos reales. Antes el frontend mostraba datos falsos (mock en el navegador); ahora todo lo que se ve y se crea pasa por las APIs y queda en MySQL.

1. Infraestructura y arranque
Verificados los 10 microservicios (identidad 8080, reuniones 8082, academica 8083, calendario 8084, convivencia 8085, geografia 8086, gestionacademica 8087, matricula 8088, mensajeria 8089, notas 8090): compilan, levantan y responden con JWT.
docker-compose.yml: MySQL ahora en puerto 3307 del host → funciona aunque tengas MySQL nativo instalado. Workbench: 127.0.0.1:3307, root/root.
Usuario semilla: al arrancar con BD vacía, identidad crea el admin automáticamente. Cualquiera que clone puede entrar de inmediato.
La BD no se toca a mano: el backend crea las tablas (Hibernate ddl-auto=update) y los datos se generan usando la app.
2. Frontend conectado al backend real
7 services reales: usuarios, notas, mensajería, hoja de vida (convivencia), niveles, matrícula y reuniones/calendario. Adaptadores incluidos porque el backend usa otros nombres de campos.
4 services siguen en mock (no hay microservicio para ellos): asistencia, pagos, alertas, configuración — comentado en cada archivo.
Páginas nuevas (rol ADMIN): /admin/matriculas (inscribir/anular) y /admin/reuniones (generales, citaciones a apoderados, calendario).
Imágenes en portada, login y Nosotros (Unsplash con overlay institucional).
3. Mejoras al backend
Docentes pueden leer matrículas (necesario para ver el curso de sus estudiantes).
Mensajería con estado leído/no-leído persistente (columna + endpoint nuevo).
Notas con fecha de evaluación real (las antiguas muestran el periodo).
4. Cuentas de prueba (RUN sin DV / clave)
Admin 12345678/admin123 · Docente 11111111/docente123 · Estudiante 22222222/estudiante123 · Apoderado 33333333/apoderado123

5. Cómo levantar
docker compose up --build        # backend completo
npm install && npm run dev       # frontend → http://localhost:5173
6. Probado con los 4 roles
Admin gestiona usuarios y matrículas reales; docente registró notas por la interfaz; estudiante las ve; apoderado recibe mensajes con badge de no-leídos. Todo persistido en MySQL.

7. Pendientes conocidos
Microservicios futuros: asistencia, pagos, alertas, configuración.
npm run lint requiere migrar a ESLint 9 (eslint.config.js).
Reemplazar fotos de Unsplash por fotos propias si se quiere (solo cambiar las URLs en los .scss).

```markdown
# Configuración de la Base de Datos de CompetenciApp para Devs Inc.

Este repositorio contiene todos los archivos necesarios para crear y poblar la base de datos de CompetenciApp para Devs Inc. en PostgreSQL. La configuración incluye:

- Un archivo `CREAR_BD.txt` con los comandos SQL para crear la base de datos y sus cinco tablas.
- Cinco archivos CSV que contienen los datos iniciales para cada tabla:
  - `usuarios.csv`
  - `especialidades.csv`
  - `usuario_especialidad.csv`
  - `titulacion.csv`
  - `usuario_titulacion.csv`

## Índice
- [Requisitos Previos](#requisitos-previos)
- [Instrucciones de Configuración](#instrucciones-de-configuración)
  - [Paso 1: Instalación de PostgreSQL](#paso-1-instalación-de-postgresql)
  - [Paso 2: Creación de la Base de Datos](#paso-2-creación-de-la-base-de-datos)
  - [Paso 3: Importación de Datos desde los Archivos CSV](#paso-3-importación-de-datos-desde-los-archivos-csv)
  - [Paso 4: Verificación de la Base de Datos](#paso-4-verificación-de-la-base-de-datos)
- [Resolución de Problemas](#resolución-de-problemas)
- [Licencia](#licencia)
- [Contacto](#contacto)

## Requisitos Previos
- PostgreSQL (versión 12 o superior recomendada).
- pgAdmin (opcional, para facilitar la gestión de la base de datos).
- Acceso a una terminal o línea de comandos.
- Los siguientes archivos organizados en un directorio (por ejemplo, `E:\HACKUDC\BD`):
  - `CREAR_BD.txt`
  - `usuarios.csv`
  - `especialidades.csv`
  - `usuario_especialidad.csv`
  - `titulacion.csv`
  - `usuario_titulacion.csv`

## Instrucciones de Configuración

### Paso 1: Instalación de PostgreSQL
Descarga e instala PostgreSQL desde [https://www.postgresql.org/download/](https://www.postgresql.org/download/). Durante la instalación, configura el usuario y la contraseña necesarios.

### Paso 2: Creación de la Base de Datos
1. Abre **pgAdmin** y conéctate al servidor PostgreSQL.
2. Abre una nueva ventana de consultas (Query Tool).
3. Utiliza el contenido del archivo `CREAR_BD.txt` para crear la base de datos y las tablas. Copia y pega el contenido de `CREAR_BD.txt` en la ventana de consultas y ejecútalo.  
   El archivo `CREAR_BD.txt` contiene los siguientes comandos:
   - Crear la base de datos.
   - Crear la tabla `usuarios`.
   - Crear la tabla `especialidades`.
   - Crear la tabla intermedia `usuario_especialidad`.
   - Crear la tabla `titulacion`.
   - Crear la tabla intermedia `usuario_titulacion`.

### Paso 3: Importación de Datos desde los Archivos CSV
Una vez creadas las tablas, importa los datos de los archivos CSV en cada tabla.

#### Opción 1: Usando la herramienta Import/Export de pgAdmin
1. En pgAdmin, haz clic derecho sobre la tabla (por ejemplo, `usuarios`) y selecciona **"Import/Export Data..."**.
2. En el cuadro de diálogo:
   - **Filename:** Especifica la ruta completa al archivo CSV (por ejemplo, `E:/HACKUDC/BD/usuarios.csv`).
   - **Formato:** Selecciona `CSV`.
   - **Encoding:** Selecciona `UTF-8`.
   - Marca la opción **"Include Column Headers"** si el CSV incluye la primera fila con los nombres de las columnas.
3. Haz clic en **"OK"** para importar los datos.
4. Repite el proceso para cada una de las tablas:
   - `usuarios.csv` → Tabla: `usuarios`
   - `especialidades.csv` → Tabla: `especialidades`
   - `usuario_especialidad.csv` → Tabla: `usuario_especialidad`
   - `titulacion.csv` → Tabla: `titulacion`
   - `usuario_titulacion.csv` → Tabla: `usuario_titulacion`

#### Opción 2: Usando el comando SQL COPY
Si el servidor PostgreSQL tiene acceso a los archivos CSV, puedes ejecutar el siguiente comando en una ventana de consultas:
```sql
COPY usuarios FROM 'E:/HACKUDC/BD/usuarios.csv' DELIMITER ',' CSV HEADER;
COPY especialidades FROM 'E:/HACKUDC/BD/especialidades.csv' DELIMITER ',' CSV HEADER;
COPY usuario_especialidad FROM 'E:/HACKUDC/BD/usuario_especialidad.csv' DELIMITER ',' CSV HEADER;
COPY titulacion FROM 'E:/HACKUDC/BD/titulacion.csv' DELIMITER ',' CSV HEADER;
COPY usuario_titulacion FROM 'E:/HACKUDC/BD/usuario_titulacion.csv' DELIMITER ',' CSV HEADER;
```
> **Nota:** El comando `COPY` se ejecuta en el contexto del servidor, por lo que la ruta debe ser accesible desde el servidor PostgreSQL.

### Paso 4: Verificación de la Base de Datos
Para confirmar que la base de datos y los datos se han importado correctamente, ejecuta algunas consultas de verificación en pgAdmin:
```sql
SELECT COUNT(*) FROM usuarios;
SELECT COUNT(*) FROM especialidades;
SELECT COUNT(*) FROM usuario_especialidad;
SELECT COUNT(*) FROM titulacion;
SELECT COUNT(*) FROM usuario_titulacion;
```
Verifica que los conteos devueltos coincidan con los datos esperados de los archivos CSV.

## Resolución de Problemas
- **Permiso denegado:** Si recibes errores de permisos al usar el comando `COPY`, asegúrate de que el servidor PostgreSQL tenga acceso de escritura a la ruta especificada. Alternativamente, utiliza la herramienta de Import/Export de pgAdmin.
- **Problemas con la ruta:** En Windows, utiliza barras diagonales (`/`) o doble barra invertida (`\\`) en las rutas (por ejemplo, `E:/HACKUDC/BD/usuarios.csv` o `E:\\HACKUDC\\BD\\usuarios.csv`).
- **Formato CSV:** Verifica que los archivos CSV estén correctamente formateados y que incluyan encabezados si es necesario.

## Licencia
Este proyecto se distribuye bajo la [Licencia que Elijas Aquí] (reemplaza con los detalles de la licencia correspondiente).

## Contacto
Para cualquier duda o asistencia adicional, por favor contacta a [tu.email@ejemplo.com](mailto:tu.email@ejemplo.com).

---
```
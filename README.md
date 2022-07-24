# Dataflow + Cloud SQL SQL Server
![dataflow_cloudsql](https://user-images.githubusercontent.com/50117113/180669426-077d3935-a8db-4371-b3ce-8cd1d93c33f7.png)

## Objetivo
#### Mediante un job de Dataflow, se migran tablas almacenadas en Cloud SQL SQL Server hacia un bucket en Google Cloud Storage.
<p align="center">
<img width="488" alt="last" src="https://user-images.githubusercontent.com/50117113/180669286-f64552dd-3818-45b7-82fa-aaebad36d60e.png">
</p>

## Dataflow
Dataflow es un servicio administrado para ejecutar una amplia variedad de patrones de procesamiento de datos. El SDK de Apache Beam es un modelo de programación de código abierto que le permite desarrollar canalizaciones por lotes y de streaming. Las canalizaciones se crean con un programa Apache Beam y, a continuación, se ejecutan en el servicio Dataflow. \
Apache Beam es un modelo unificado de código abierto para definir canalizaciones de procesamiento paralelo de datos por lotes y streaming. El modelo de programación Apache Beam simplifica la mecánica del procesamiento de datos a gran escala. Con uno de los SDK de Apache Beam, se crea un programa que define la canalización. A continuación, uno de los backends de procesamiento distribuido compatibles con Apache Beam, como Dataflow, ejecuta la canalización. Este modelo le permite concentrarse en la composición lógica de su trabajo de procesamiento de datos, en lugar de la orquestación física del procesamiento paralelo.\
Algunas caracteristicas de Dataflow:
- Servicio fully managed para el procesamiento de datos
- Escalado automático de recursos y reequilibrio dinámico del trabajo
- IP privadas
- Aprovisionamiento y gestión automatizados de recursos de procesamiento
- Monitoreo en linea
- Dataflow Templates 
- Real-time change data capture

## 1. Instala el SDK de Java de Apache Beam en Cloud Shell
Ejecuta el siguiente comando en Cloud Shell para instalar el SDK de Apache Beam
```
mvn archetype:generate \
    -DarchetypeGroupId=org.apache.beam \
    -DarchetypeArtifactId=beam-sdks-java-maven-archetypes-examples \
    -DarchetypeVersion=2.37.0 \
    -DgroupId=org.example \
    -DartifactId=word-count-beam \
    -Dversion="0.1" \
    -Dpackage=org.apache.beam.examples \
    -DinteractiveMode=false
```
El argumento ``artifactId`` establece el nombre del archivo jar que se creó. Para estas instrucciones, se usa el valor predeterminado de ``word-count-beam``

## 2. Crea tu archivo de configuracion ``pom.xml``
La unidad básica de trabajo en Maven es el llamado Modelo de Objetos de Proyecto conocido simplemente como POM (de sus siglas en inglés: Project Object Model). Por lo tanto debes crear tu propio archivo ``pom.xml`` o simplemente descargar el provisto aqui, el cual ya dispone de las dependencias necesarias para nuestro objetivo.

## 3. Crea tu archivo de codigo fuente
Por defecto, el directorio donde está el código fuente es ``src/main/java`` y alli debes crear o simplemente descargar el archivo con el codigo fuente llamado ``JDBCIOExample.java``. En dicho archivo se encuentra definido mediante lenguaje Java nuestro pipeline encargado de migrar las tablas almacenadas en Cloud SQL SQL Server hacia Cloud Storage. El formato en que se guardan las tablas en Cloud Storage es Avro, aunque puede ser modificado dentro del codigo.

## 4. Ejecuta tu pipeline en Dataflow
Ejecuta el siguiente comando para correr tu pipeline en Dataflow, debes ejecutar el comando en el mismo directorio donde se encuentra tu archivo ``JDBCIOExample.java``
```
mvn -Pdataflow-runner -X -e compile exec:java -Dexec.mainClass=JDBCIOExample -Dexec.args=" \
--project=<MY_PROJECT> \
--region=us-central1 \
--gcpTempLocation=gs://<MY_BUCKET_NAME>/temp/ \
--output=gs://<MY_BUCKET_NAME>/output \
--runner=DataflowRunner" \
-Pdataflow-runner
```
Una vez que el job de Dataflow empiece a ejecutarse, puedes ir a la pestaña de Dataflow y en la parte de Jobs podras verificar el progreso de tu pipeline.

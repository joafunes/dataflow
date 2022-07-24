# Dataflow + Cloud SQL SQL Server
imagen
## Objetivo
#### Mediante un job de Dataflow, se migran tablas almacenadas en Cloud SQL SQL Server hacia un bucket en Google Cloud Storage.
imagen source y sink

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

## 1. Instala el SDK de Apache Beam en Cloud Shell
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

## 2. Crea tu archivo ``pom.xml`` con el mismo contenido que tiene el ``pom.xml`` disponible aqui

## 3. Crea tu archivo ``JDBCIOExample.java`` con el mismo contenido del ``JDBCIOExample.java`` disponible aqui, o modificalo segun tus necesidades

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

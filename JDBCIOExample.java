/*
Resumen: Se lee una tabla de una instancia CloudSQL SQLServer y luego la llevo a GCS

Ejecutar con (desde CloudShell):
mvn -Pdataflow-runner -X -e compile exec:java -Dexec.mainClass=JDBCIOExample -Dexec.args=" \
--project=proyectofinal-gcp \
--region=us-central1 \
--gcpTempLocation=gs://bkt-testeo-jfunes/temp/ \
--output=gs://bucket-destino/dataflow/output \
--runner=DataflowRunner" \
-Pdataflow-runner

NOTA: Los archivos JDBCIOExample.java y el pom.xml debden estar en el mismo directorio
La instancia CloudSQL SQLServer tiene una ip privada y a esa misma me estoy conectando, sin necesidad de un proxy
*/

import org.apache.avro.Schema;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.io.AvroIO;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.jdbc.JdbcIO;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.io.FileSystems;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.beam.sdk.io.LocalFileSystemRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Arrays;



public class JDBCIOExample {

    /*private static final Logger LOG = LoggerFactory.getLogger(JDBCIOExample.class);
    private static final List<String> acceptedTypes = Arrays.asList(
        new String[]{"string", "boolean", "int", "long", "float", "double"});*/
    

    public interface JDBCIOExampleOptions extends PipelineOptions {
        //@Description("Path of the file to read from")
        //@Default.String("gs://beam-samples/shakespeare/kinglear.txt")
        String getInputFile();
    
        void setInputFile(String value);
    
        //Path of the file to write to
        String getOutput();
    
        void setOutput(String value);
      }

    /*public static void checkFieldTypes(Schema schema) throws IllegalArgumentException {
        for (Schema.Field field : schema.getFields()) {
            String fieldType = field.schema().getType().getName().toLowerCase();
            if (!acceptedTypes.contains(fieldType)) {
                LOG.error("Data transformation doesn't support: " + fieldType);
                throw new IllegalArgumentException("Field type " + fieldType + " is not supported.");
            }
        }
    }*/
    public static void main(String[] args) {

        // Get Avro Schema
        //String schemaJson = getSchema(options.getAvroSchema());
        //String schemaJson = "[('Id', int), ('Nombre', str), ('Apellido', str)]";
        //Schema schema = new Schema.Parser().parse(schemaJson);

        // Check schema field types before starting the Dataflow job
        //checkFieldTypes(schema);

        JDBCIOExampleOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(JDBCIOExampleOptions.class);
        Pipeline p = Pipeline.create(options);

        PCollection<String> pOutput = p.apply(JdbcIO.<String>read()
                .withDataSourceConfiguration(JdbcIO.DataSourceConfiguration
                .create("com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver://10.48.176.3:1433;database=negocio;encrypt=false")
                //.create("com.mysql.jdbc.Driver","jdbc:mysql://your_ip_sql:3306/products?useSSL=false")
                .withUsername("sqlserver")
                .withPassword("abc123"))
                .withQuery("SELECT * FROM Personas")
                .withCoder(StringUtf8Coder.of())
                //.withStatementPreparator(new JdbcIO.StatementPreparator() {
                //    @Override
                //    public void setParameters(PreparedStatement preparedStatement) throws Exception {
                //            preparedStatement.setString(1,"Nombre");
                //    }
                //})
                .withRowMapper(new JdbcIO.RowMapper<String>() {

                    @Override
                    public String mapRow(ResultSet resultSet) throws Exception {
                        return resultSet.getString(1)+","+resultSet.getString(2)+","+resultSet.getString(3);
                    }
                })
            );

        // Escribir en formato CSV
        //pOutput.apply(TextIO.write().to(options.getOutput()).withoutSharding().withSuffix(".csv"));

        // Escribir en formato AVRO. Seria write(String.class) porque mi PCollection es de objetos String
        pOutput.apply(AvroIO.write(String.class).to(options.getOutput()).withNumShards(1).withSuffix(".avro"));

        p.run().waitUntilFinish();
    }
}

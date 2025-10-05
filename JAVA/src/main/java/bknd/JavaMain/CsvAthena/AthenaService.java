package bknd.JavaMain.CsvAthena;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.athena.AthenaClient;
import software.amazon.awssdk.services.athena.model.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AthenaService {
    @Autowired
    private AthenaClient athenaClient;

    private final String database = "athena_new";
    public final String tableName = "usermobile";
    public final String studentTable = "mainuserdata";
    private final String outputLocation = "s3://query-bucket-jra-dev";

    public List<Map<String, String>> executeQuery(String query){
        try{
            StartQueryExecutionRequest queryRequest = StartQueryExecutionRequest.builder()
                    .queryString(query)
                    .queryExecutionContext(QueryExecutionContext.builder().database(database).build())
                    .resultConfiguration(ResultConfiguration.builder().outputLocation(outputLocation).build())
                    .build();
            String queryExecutionId = athenaClient.startQueryExecution(queryRequest).queryExecutionId();

            waitForQueryCompletion(queryExecutionId);

            return getQueryResults(queryExecutionId);
        } catch(Exception e){
            throw new RuntimeException("Query execution failed: " + e.getMessage());
        }
    };

    public void waitForQueryCompletion(String queryExecutionId) throws InterruptedException{
        GetQueryExecutionRequest request = GetQueryExecutionRequest.builder()
                .queryExecutionId(queryExecutionId).build();

        while(true){
            QueryExecutionStatus status = athenaClient.getQueryExecution(request)
                    .queryExecution().status();

            if(status.state() == QueryExecutionState.SUCCEEDED) break;
            if(status.state() == QueryExecutionState.FAILED)
                throw new RuntimeException("Query" + status.stateChangeReason());
            if(status.state() == QueryExecutionState.CANCELLED)
                throw new RuntimeException("Query cancelled");

            TimeUnit.SECONDS.sleep(2);
        }
    };

    private List<Map<String, String>> getQueryResults(String queryExecutionId) {
        GetQueryResultsRequest request = GetQueryResultsRequest.builder()
                .queryExecutionId(queryExecutionId).build();

        GetQueryResultsResponse response = athenaClient.getQueryResults(request);

        List<Row> rows = response.resultSet().rows();
        List<Map<String, String>> results = new ArrayList<>();

        if (rows.isEmpty()) return results;

        // Get column names from first row
        List<String> columnNames = new ArrayList<>();
        for (Datum datum : rows.get(0).data()) {
            columnNames.add(datum.varCharValue());
        }

        // Process data rows (skip header)
        for (int i = 1; i < rows.size(); i++) {
            Map<String, String> row = new HashMap<>();
            List<Datum> data = rows.get(i).data();

            for (int j = 0; j < data.size() && j < columnNames.size(); j++) {
                row.put(columnNames.get(j), data.get(j).varCharValue());
            }
            results.add(row);
        }

        return results;
    };

    public String generateUniqueId(){
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder idBuilder = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            idBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return idBuilder.toString();
    }

}

package bknd.Rnd.SQLQueries;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ConnectDB {

    private final JdbcTemplate jdbcTemplate;

    public ConnectDB(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> runQuery(String sql) {

        return jdbcTemplate.queryForList(sql);
    }

}

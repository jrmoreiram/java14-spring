package com.junior;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    //JDBC Puro.
    private final JdbcTemplate template;

    //Java Text Blocks nova funcionalidade do Java 14.
    private final String findByIdSql = """
            SELECT * FROM PRODUCT \
            WHERE id = ?
             """;

    private final RowMapper<ProductRecord> productRowMapper = (resultSet, i) -> new ProductRecord(
            resultSet.getInt("id"),
            resultSet.getString("name"),
            resultSet.getInt("status")
    );

    public ProductService(JdbcTemplate template) {
        this.template = template;
    }

    public ProductRecord findById(Integer id) {
        return this.template.queryForObject(findByIdSql, new Object[]{id}, productRowMapper);
    }
}

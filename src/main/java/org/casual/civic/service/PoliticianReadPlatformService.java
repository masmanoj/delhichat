package org.casual.civic.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.casual.civic.data.PoliticianData;
import org.casual.civic.exception.PoliticianNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class PoliticianReadPlatformService {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private PoliticianReadPlatformService(DataSource dataSource) {
	    this.jdbcTemplate = new JdbcTemplate();
	    this.jdbcTemplate.setDataSource(dataSource);
	}
	
	public List<PoliticianData> retrieveAll() {
		final PoliticianMapper pm = new PoliticianMapper();
        final String sql = "select " + pm.schema() + " order by p.name";

        return this.jdbcTemplate.query(sql, pm, new Object[] {});
	}
	
	public PoliticianData retrieveOne(final Long politicianId) {
		try {
			final PoliticianMapper pm = new PoliticianMapper();
	        final String sql = "select " + pm.schema() + " where p.id=? order by p.name";
	
	        return this.jdbcTemplate.queryForObject(sql, pm, new Object[] {politicianId});
		} catch (EmptyResultDataAccessException exception) {
			throw new PoliticianNotFoundException(politicianId);
		}
	}
	
	private static final class PoliticianMapper implements RowMapper<PoliticianData> {

        public String schema() {
            return " p.id as id, p.name as name from c_politician p ";
        }

        @Override
        public PoliticianData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

            final Long id = rs.getLong("id");
            final String name = rs.getString("name");

            return PoliticianData.instance(id, name);
        }
    }

}

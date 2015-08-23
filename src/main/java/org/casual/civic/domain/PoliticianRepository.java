package org.casual.civic.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PoliticianRepository extends JpaRepository<Politician, Long>, JpaSpecificationExecutor<Politician> {

}

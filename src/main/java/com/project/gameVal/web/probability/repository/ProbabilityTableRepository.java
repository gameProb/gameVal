package com.project.gameVal.web.probability.repository;

import com.project.gameVal.web.probability.domain.ProbabilityTable;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface ProbabilityTableRepository extends CrudRepository<ProbabilityTable, String> {
}

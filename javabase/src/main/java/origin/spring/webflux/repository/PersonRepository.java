package origin.spring.webflux.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import origin.spring.webflux.repository.entity.PersonEntity;

import java.util.UUID;

/**
 * @Author:lmq
 * @Date: 2020/8/8
 * @Desc:
 **/
@Repository
public interface PersonRepository extends ReactiveCassandraRepository<PersonEntity, UUID> {
}

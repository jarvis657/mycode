package origin.spring.webflux.repository;

//import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import origin.spring.webflux.repository.entity.PersonByCountryEntity;
import origin.spring.webflux.repository.entity.PersonByCountryKey;
import reactor.core.publisher.Flux;

/**
 * @Author:lmq
 * @Date: 2020/8/8
 * @Desc:
 **/
@Repository
public interface PersonByCountryRepository {
//        extends ReactiveCassandraRepository<PersonByCountryEntity, PersonByCountryKey> {

    Flux<PersonByCountryEntity> findAllByKeyCountry(final String country);
}

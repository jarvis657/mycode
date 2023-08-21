package origin.spring.webflux;

import origin.spring.webflux.repository.entity.PersonByCountryEntity;
import origin.spring.webflux.repository.entity.PersonByCountryKey;
import origin.spring.webflux.repository.entity.PersonEntity;

/**
 * @Author:lmq
 * @Date: 2020/8/8
 * @Desc:
 **/
class PersonMapper {

    private PersonMapper() {}
    static Person toPerson(PersonByCountryEntity personByCountryEntity) {
        return new Person(
                personByCountryEntity.getKey().getId(),
                personByCountryEntity.getKey().getFirstName(),
                personByCountryEntity.getKey().getLastName(),
                personByCountryEntity.getKey().getCountry(),
                personByCountryEntity.getAge());
    }

    static Person toPerson(PersonEntity personEntity) {
        return new Person(
                personEntity.getId(),
                personEntity.getFirstName(),
                personEntity.getLastName(),
                personEntity.getCountry(),
                personEntity.getAge());
    }

    static PersonEntity toPersonEntity(Person person) {
        return new PersonEntity(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getCountry(),
                person.getAge());
    }

    static PersonByCountryEntity toPersonByCountryEntity(Person person) {
        return new PersonByCountryEntity(
                new PersonByCountryKey(
                        person.getCountry(), person.getFirstName(), person.getLastName(), person.getId()),
                person.getAge());
    }
}

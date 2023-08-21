package origin.spring.db;

import org.apache.ibatis.annotations.Mapper;
import origin.spring.db.Person;

@Mapper
public interface PersonDao {
    int deleteByPrimaryKey(Long id);

    int insert(Person record);

    int insertSelective(Person record);

    Person selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Person record);

    int updateByPrimaryKey(Person record);
}
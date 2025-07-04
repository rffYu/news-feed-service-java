package common.repository;

import common.dao.InformationDao;
import reactor.core.publisher.Mono;

public interface InformationRepository {

    Mono<Void> save(InformationDao dao);

//    InformationDao findById(String redisKey);

//    void delete(String redisKey);
}

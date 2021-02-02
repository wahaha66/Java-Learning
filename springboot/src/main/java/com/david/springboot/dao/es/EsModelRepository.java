package com.david.springboot.dao.es;

import com.david.springboot.bean.model.EsModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EsModelRepository extends ElasticsearchRepository<EsModel,Long> {

    List<EsModel> findByPriceBetween(double start,double end);
}

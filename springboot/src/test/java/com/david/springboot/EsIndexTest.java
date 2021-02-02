package com.david.springboot;

import com.david.framework.util.JsonUtil;
import com.david.springboot.bean.model.EsModel;
import com.david.springboot.dao.es.EsModelRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootApplication.class)
public class EsIndexTest {

    @Resource
    private ElasticsearchTemplate template;
    @Autowired
    private EsModelRepository esModelRepository;

    @Test
    public void testCreate(){
        //创建索引，会根据model累的@Document注解信息来创建
        template.createIndex(EsModel.class);
        //配置映射，会根据model类中的id、Field等字段来自动完成映射
        template.putMapping(EsModel.class);
    }

    @Test
    public void testDelete(){
        // 根据类名删除索引
        template.deleteIndex(EsModel.class);
        // 根据索引名称删除索引
//        template.deleteIndex("es_model");
    }

    @Test
    public void testInsert(){
        EsModel model = new EsModel();
        model.setId(2L);
        model.setTitle("Chanel");
        model.setBrand("Chanel");
        model.setCategory("top1");
        model.setImages("https://image/chanel.png");
        model.setPrice(100000d);
        esModelRepository.save(model);
    }

    @Test
    public void testInsertList(){
        List<EsModel> list = new ArrayList<>();
        list.add(new EsModel(11L, "小米手机7", "手机", "小米", 3299.00, "http://image.zq.com/13123.jpg"));
        list.add(new EsModel(12L, "坚果手机R1", "手机", "锤子", 3699.00, "http://image.zq.com/13123.jpg"));
        list.add(new EsModel(13L, "华为META10", "手机", "华为", 4499.00, "http://image.zq.com/13123.jpg"));
        list.add(new EsModel(14L, "小米Mix2S", "手机", "小米", 4299.00, "http://image.zq.com/13123.jpg"));
        list.add(new EsModel(15L, "荣耀V10", "手机", "华为", 2799.00, "http://image.zq.com/13123.jpg"));
        // 接收对象集合，实现批量新增
        esModelRepository.saveAll(list);
    }

    @Test
    public void testFind(){
        Iterable<EsModel> esModels = esModelRepository.findAll();
        System.out.println("JsonUtil.toJson(esModels); = " + JsonUtil.toJson(esModels));
    }

    @Test
    public void testFindBetween(){
        List<EsModel> list = esModelRepository.findByPriceBetween(120000d,130000d);
        System.out.println("JsonUtil.toJson(esModels); = " + JsonUtil.toJson(list));
    }

    @Test
    public void testQuery(){
        //词条查询
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "小米手机7");
        //执行查询
        Iterable<EsModel> list = esModelRepository.search(queryBuilder);
        System.out.println("JsonUtil.toJson(list); = " + JsonUtil.toJson(list.iterator()));

    }

    @Test
    public void testNativeQuery(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加分词查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("title","小米"));
        //分页
        int page = 0;
        int pageSize = 2;
        queryBuilder.withPageable(PageRequest.of(page,pageSize));
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
        //执行结果
        Page<EsModel> items = esModelRepository.search(queryBuilder.build());
        System.out.println("page.getTotalElements() = " + items.getTotalElements());
        System.out.println("page.getTotalPages() = " + items.getTotalPages());
        items.stream().forEach(System.out::println);
    }

    @Test
    public void testAgg(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //不查询任何结果
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""},null));
        // 1、添加一个新的聚合，聚合类型为terms，聚合名称为brands，聚合字段为brand
        queryBuilder.addAggregation(AggregationBuilders.terms("brands").field("brand")
                .subAggregation(AggregationBuilders.avg("priceAvg").field("price")) // 在品牌聚合桶内进行嵌套聚合，求平均值
        );
        // 2、查询,需要把结果强转为AggregatedPage类型
        AggregatedPage<EsModel> aggPage = (AggregatedPage<EsModel>) esModelRepository.search(queryBuilder.build());
        //3 解析
        //3.1从结果中取出名为brands的聚合，所以结果要强转为StringTerms
        StringTerms agg = (StringTerms) aggPage.getAggregation("brands");
        //3.2获取桶
        List<StringTerms.Bucket> buckets = agg.getBuckets();
        //3.3遍历
        buckets.forEach(bucket -> {
            // 3.4、获取桶中的key，即品牌名称
            System.out.println(bucket.getKeyAsString());
            // 3.5、获取桶中的文档数量
            System.out.println(bucket.getDocCount());
            // 3.6.获取子聚合结果：
            InternalAvg avg = (InternalAvg) bucket.getAggregations().asMap().get("priceAvg");
            System.out.println("平均售价：" + avg.getValue());
        });
    }

    @Test
    public void testSubAgg(){
        NestedAggregationBuilder nestedAggregationBuilder = AggregationBuilders.nested("nestedByMatchOdds","matchOdds");
        nestedAggregationBuilder.subAggregation(AggregationBuilders.terms("groupByplayCateId").field("matchOdds.playCateId").size(1000)
                .subAggregation(AggregationBuilders.reverseNested("findParents")
                        .subAggregation(AggregationBuilders.sum("sum").field("totalAmount"))
                )
        );
        BoolQueryBuilder bqb = QueryBuilders.boolQuery();

        SearchResponse response = template.getClient().prepareSearch(new String[]{"cx_sport_bet_20210120"})
                .setQuery(bqb)
                .addAggregation(nestedAggregationBuilder)
                .execute().actionGet();
        Aggregations aggS = response.getAggregations();

    }
}

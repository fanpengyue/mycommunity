package com.fpy.community.service;

import com.alibaba.fastjson.JSON;
import com.fpy.community.dto.PaginationDTO;
import com.fpy.community.dto.QuestionDTO;
import com.fpy.community.dto.QuestionQueryDTO;
import com.fpy.community.exception.CustomizeErrorCode;
import com.fpy.community.exception.CustomizeException;
import com.fpy.community.mapper.QuestionExtMapper;
import com.fpy.community.mapper.QuestionMapper;
import com.fpy.community.mapper.UserMapper;
import com.fpy.community.model.Article;
import com.fpy.community.model.Question;
import com.fpy.community.model.QuestionExample;
import com.fpy.community.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.lucene.queryparser.xml.QueryBuilder;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionService {

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QuestionExtMapper questionExtMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    public PaginationDTO list(String search,Integer page, Integer size) {

        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }

        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);

        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);
        Integer offset = page<1 ? 0 : size * (page - 1);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);

        //size*(page-1)
        Integer offset = size * (page - 1);
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            // 创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        } else {
            // 更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }

    public List<Question> selectAll(){
        List<Question> questions = questionMapper.selectByExample(null);
        return questions;
    }

    public PaginationDTO highLightQuery(String search, Integer page, Integer size) {
        if(page < 1){
            page = 1;
        }
        SearchRequest searchRequest = new SearchRequest("article");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //分页
        builder.from(page);
        builder.size(size);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(StringUtils.isNotBlank(search)){
            boolQueryBuilder.must(QueryBuilders.matchQuery("title",search));
        }
        builder.query(boolQueryBuilder);
        builder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //高亮的字段
        highlightBuilder.field("title");
        //是否多个字段都高亮
        highlightBuilder.requireFieldMatch(false);
        //前缀后缀
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        builder.highlighter(highlightBuilder);

        //执行搜索
        searchRequest.source(builder);
        SearchResponse response = null;
        try{
            response = elasticsearchTemplate.getClient().search(searchRequest).actionGet();
        }catch (Exception e){
            e.printStackTrace();
        }

        //解析结果
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            //解析高亮的字段
            //获取高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
//            System.out.println("=========="+highlightFields);
            HighlightField content = highlightFields.get("title");
//            System.out.println("==content=="+content);
            // 原来的结果
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //将原来的字段替换为高亮字段即可
            if (content!=null){
                Text[] fragments = content.fragments();
                String newTitle = "";
                for (Text text : fragments) {
                    newTitle +=text;
                }
                //替换掉原来的内容
                sourceAsMap.put("title",newTitle);
            }
            QuestionDTO questionDTO = new QuestionDTO();
            Question question = questionMapper.selectByPrimaryKey(Long.valueOf(String.valueOf(sourceAsMap.get("id"))));
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            question.setTitle(String.valueOf(sourceAsMap.get("title")));
//            log.info("newTitle:"+question.getTitle());
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage = Integer.valueOf(String.valueOf(response.getHits().getTotalHits())) / size;

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage, page);
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    public void createOrUpdateToEs(Question question) {
        List<IndexQuery> indexQueryList = new ArrayList<>();
        Article article = new Article();
        article.setId(String.valueOf(question.getId()));
        article.setTitle(question.getTitle());
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setId(article.getId());
        indexQuery.setIndexName("article");
        indexQuery.setType("article");
        indexQuery.setSource(JSON.toJSONString(article));
        indexQueryList.add(indexQuery);
        elasticsearchTemplate.bulkIndex(indexQueryList);

    }
}
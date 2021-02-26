package com.fpy.community.controller;

import com.alibaba.fastjson.JSON;
import com.fpy.community.dto.PaginationDTO;
import com.fpy.community.mapper.UserMapper;
import com.fpy.community.model.Article;
import com.fpy.community.model.Question;
import com.fpy.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class IndexController {

    @Resource
    private QuestionService questionService;
    @Resource
    private UserMapper userMapper;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "5")Integer size,
                        @RequestParam(name = "search",required = false)String search){
        PaginationDTO paginationDTO = null;
        if(search ==""||search == null){
            search=null;
            paginationDTO = questionService.list(search,page,size);
        }else {
            paginationDTO = questionService.highLightQuery(search, page, size);
        }
        model.addAttribute("pagination",paginationDTO);
        model.addAttribute("search", search);
        return "index";
    }

    /**
     * 将mysql数据导入到es
     * @return
     */
    @GetMapping(value = "/import")
    @ResponseBody
    public String importData(){
        int count = 0;
        List<IndexQuery> indexQueryList = new ArrayList<>();
        List<Question> questions = questionService.selectAll();
        for(Question question : questions){
            Article article = new Article();
            article.setId(String.valueOf(question.getId()));
            article.setTitle(question.getTitle());
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(article.getId());
            indexQuery.setIndexName("article");
            indexQuery.setType("article");
            indexQuery.setSource(JSON.toJSONString(article));
            indexQueryList.add(indexQuery);
            if(count != 0 && count % 1000 == 0){
                elasticsearchTemplate.bulkIndex(indexQueryList);
                indexQueryList.clear();
                log.info("bulkIndex counter : " + count);
            }
            count++;
        }
        if(indexQueryList.size() > 0){
            elasticsearchTemplate.bulkIndex(indexQueryList);
            log.info("bulkIndex counter : " + count);
        }
        return "import ok";
    }


}

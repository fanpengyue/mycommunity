package com.fpy.community.service;

import com.fpy.community.dto.PaginationDTO;
import com.fpy.community.dto.QuestionDTO;
import com.fpy.community.mapper.QuestionMapper;
import com.fpy.community.mapper.UserMapper;
import com.fpy.community.model.Question;
import com.fpy.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private UserMapper userMapper;


    public PaginationDTO list(Integer page, Integer size) {
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();

        Integer totalPage;
        if(totalCount % size == 0 ){
            totalPage = totalCount / size ;
        }else{
            totalPage = totalCount / size + 1 ;
        }


        if(page < 1){
            page =1;
        }
        if(page>totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);

        Integer offset = size * (page - 1);
        List<Question> questionList = questionMapper.list(offset,size);

        for(Question question : questionList){
            User user = userMapper.findUserById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //作用：快速的将我们question的属性赋值给questionDTO
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO listByUsserId(Integer id, Integer page, Integer size) {
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.countByUserId(id);
        Integer totalPage;
        if(totalCount % size == 0 ){
            totalPage = totalCount / size ;
        }else{
            totalPage = totalCount / size + 1 ;
        }


        if(page < 1){
            page =1;
        }
        if(page>totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);

        Integer offset = size * (page - 1);
        List<Question> questionList = questionMapper.listByUsserId(id,offset,size);

        for(Question question : questionList){
            User user = userMapper.findUserById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //作用：快速的将我们question的属性赋值给questionDTO
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }
}

package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.search.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;
import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class SearchService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SearchDao searchDao;
    private final SearchProvider searchProvider;
    private final JwtService jwtService;

    @Autowired
    public SearchService(SearchDao searchDao, SearchProvider searchProvider, JwtService jwtService) {
        this.searchDao = searchDao;
        this.searchProvider = searchProvider;
        this.jwtService = jwtService;

    }

    /**키워드 등록*/
    public String createKeyword(PostKeywordReq postKeywordReq) throws BaseException {
        if(searchDao.checkKeywordProduct(postKeywordReq.getKeyword())==0 && searchDao.checkKeywordStore(postKeywordReq.getKeyword())==0){
            throw new BaseException(FAILED_TO_SEARCH_REGISTER);
        }
        if(searchDao.checkKeyword(postKeywordReq)==1){
            throw new BaseException(FAILED_TO_SEARCH_REGISTER);
        }
        try{
            int result = searchDao.createKeyword(postKeywordReq);
            String msg ="";
            if(result == 0){
                msg = "등록 실패";
            }
            else{
                msg = "등록 성공";
            }
            return msg;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**내가 찾아봤던 키워드 삭제*/
    public String deleteKeywords(int userIdx) throws BaseException {
        if(searchDao.getKeywords(userIdx).isEmpty()){
            throw new BaseException(DELETE_FAIL_KEYWORD);
        }
        try{
            int result = searchDao.deleteKeywords(userIdx);
            String msg ="";
            if(result == 0){
                msg = "삭제 실패";
            }
            else{
                msg = "삭제 성공";
            }
            return new String(msg);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

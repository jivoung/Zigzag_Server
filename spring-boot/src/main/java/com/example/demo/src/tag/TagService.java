package com.example.demo.src.tag;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.tag.model.*;
import com.example.demo.src.user.model.PostFavoriteStoreReq;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class TagService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TagDao tagDao;
    private final TagProvider tagProvider;
    private final JwtService jwtService;


    @Autowired
    public TagService(TagDao tagDao, TagProvider tagProvider, JwtService jwtService) {
        this.tagDao = tagDao;
        this.tagProvider = tagProvider;
        this.jwtService = jwtService;
    }

    /**태그 등록*/
    public String createTag(PostTagReq postTagReq) throws BaseException {
        if(tagDao.checkStoreTag(postTagReq)==1){
            throw new BaseException(FAILED_TO_TAG_REGISTER);
        }
        if(tagDao.checkStoreTagNum(postTagReq)==5){
            throw new BaseException(FAILED_TO_TAG_REGISTER);
        }
        try{
            int result = tagDao.createTag(postTagReq);
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

    /**태그 삭제*/
    public String deleteTag(PatchTagReq patchTagReq) throws BaseException {
        if(tagDao.checkTagKeyword(patchTagReq) == 0){
            throw new BaseException(DELETE_FAIL_TAG);
        }
        try{
            int result = tagDao.deleteTag(patchTagReq);
            String msg ="";
            if(result == 0){
                msg = "삭제 실패";
            }
            else{
                msg = "삭제 성공";
            }
            return msg;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

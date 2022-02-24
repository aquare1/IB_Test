package com.aquare.service;

import com.aquare.common.JwtUtils;
import com.aquare.common.ResultCode;
import com.aquare.common.ResultDTO;
import com.aquare.dao.PersonDao;
import com.aquare.domain.ResponseUserToken;
import com.aquare.domain.UserDetail;
import com.aquare.entity.PersonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dengtao aquare@163.com
 * @createAt 2022/2/23
 */
@Service
public class PersonService {

    @Resource
    PersonDao personDao;


    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private JwtUtils jwtTokenUtil;

    public ResultDTO findById(String id){
        try {
            PersonEntity person = new PersonEntity();
            person.setId(id);
            List<PersonEntity> configList = personDao.findAll(person);
            if(configList.size() == 1){
                return ResultDTO.success(configList.get(0));
            } else {
                return ResultDTO.success();
            }

        }catch (Exception e){
            return ResultDTO.failure(e);
        }
    }

    public ResultDTO save(PersonEntity person){
        try {
            personDao.save(person);
            return ResultDTO.success(person);
        }catch (Exception e){
            return ResultDTO.failure(e);
        }
    }

    public ResultDTO delete(String id){
        try {
            PersonEntity person = new PersonEntity();
            person.setId(id);
            personDao.deleteByAll(person);
            return ResultDTO.success();
        }catch (Exception e){
            return ResultDTO.failure(e);
        }
    }

    public ResultDTO login(){
        try{

            final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("admin", "admin123"));
            UserDetail userDetail = (UserDetail)authentication.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtTokenUtil.generateAccessToken(userDetail);
            jwtTokenUtil.putToken(userDetail.getUsername(), token);
            return ResultDTO.success(new ResponseUserToken(token));
        } catch (DisabledException | BadCredentialsException e) {
            return  ResultDTO.failure(e);
        }
    }

}
